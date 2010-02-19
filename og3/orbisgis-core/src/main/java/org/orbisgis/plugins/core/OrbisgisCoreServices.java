/**
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. OrbisGIS is
 * distributed under GPL 3 license. It is produced by the geo-informatic team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/>, CNRS FR 2488: Erwan BOCHER,
 * scientific researcher, Pierre-Yves FADET, computer engineer. Previous
 * computer developer : Thomas LEDUC, scientific researcher, Fernando GONZALEZ
 * CORTES, computer engineer.
 * 
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 * 
 * This file is part of OrbisGIS.
 * 
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 * 
 * For more information, please consult: <http://orbisgis.cerma.archi.fr/>
 * <http://sourcesup.cru.fr/projects/orbisgis/>
 * 
 * or contact directly: erwan.bocher _at_ ec-nantes.fr Pierre-Yves.Fadet _at_
 * ec-nantes.fr
 **/

package org.orbisgis.plugins.core;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.gdms.data.DataSourceFactory;
import org.gdms.data.InitializationException;
import org.gdms.data.WarningListener;
import org.grap.lut.LutGenerator;
import org.orbisgis.plugins.core.configuration.BasicConfiguration;
import org.orbisgis.plugins.core.configuration.DefaultBasicConfiguration;
import org.orbisgis.plugins.core.geocognition.DefaultGeocognition;
import org.orbisgis.plugins.core.geocognition.Geocognition;
import org.orbisgis.plugins.core.javaManager.DefaultJavaManager;
import org.orbisgis.plugins.core.javaManager.JavaManager;
import org.orbisgis.plugins.core.map.export.DefaultMapExportManager;
import org.orbisgis.plugins.core.map.export.MapExportManager;
import org.orbisgis.plugins.core.map.export.RectanglesScale;
import org.orbisgis.plugins.core.map.export.SingleLineScale;
import org.orbisgis.plugins.core.renderer.legend.RasterLegend;
import org.orbisgis.plugins.core.renderer.legend.carto.DefaultLegendManager;
import org.orbisgis.plugins.core.renderer.legend.carto.LegendFactory;
import org.orbisgis.plugins.core.renderer.legend.carto.LegendManager;
import org.orbisgis.plugins.core.renderer.symbol.ArrowSymbol;
import org.orbisgis.plugins.core.renderer.symbol.DefaultSymbolManager;
import org.orbisgis.plugins.core.renderer.symbol.Shading3DPolygon;
import org.orbisgis.plugins.core.renderer.symbol.SymbolFactory;
import org.orbisgis.plugins.core.renderer.symbol.SymbolManager;
import org.orbisgis.plugins.core.workspace.DefaultOGWorkspace;
import org.orbisgis.plugins.core.workspace.OGWorkspace;
import org.orbisgis.plugins.core.workspace.Workspace;
import org.orbisgis.plugins.errorManager.ErrorManager;

public class OrbisgisCoreServices {

	private static final String SOURCES_DIR_NAME = "sources";
	private final static Logger logger = Logger
			.getLogger(OrbisgisCoreServices.class);

	/**
	 * Installs all the OrbisGIS core services
	 */
	public static void installServices() {
		// Error service must be installed
		if (Services.getService(ErrorManager.class) == null) {
			throw new IllegalStateException("Error service must be installed "
					+ "before initializing OrbisGIS services");
		}
		if (Services.getService(ErrorManager.class) == null) {
			throw new IllegalStateException(
					"Workspace service must be installed "
							+ "before initializing OrbisGIS services");
		}

		installApplicationInfoServices();

		installWorkspaceServices();

		installConfigurationService();

		installSymbologyServices();

		installGeocognitionService();

		installExportServices();

		try {
			installJavaServices();
		} catch (IOException e) {
			throw new InitializationException("Cannot initialize Java manager",
					e);
		}
	}

	private static void installApplicationInfoServices() {
		if (Services.getService(ApplicationInfo.class) == null) {
			Services.registerService(ApplicationInfo.class,
					"Gets information about the application: "
							+ "name, version, etc.",
					new OrbisGISApplicationInfo());
		}
	}

	private static void installExportServices() {
		DefaultMapExportManager mem = new DefaultMapExportManager();
		Services.registerService(MapExportManager.class,
				"Manages the export of MapContexts to different formats.", mem);
		mem.registerScale(SingleLineScale.class);
		mem.registerScale(RectanglesScale.class);
	}

	/**
	 * Installs services that depend on the workspace such as the
	 * {@link DataManager}
	 */
	public static void installWorkspaceServices() {
		Workspace workspace = Services.getService(Workspace.class);

		DefaultOGWorkspace defaultOGWorkspace = new DefaultOGWorkspace();
		Services.registerService(OGWorkspace.class,
				"Gives access to directories inside the workspace."
						+ " You can use the temporal folder in "
						+ "the workspace through this service. It lets "
						+ "the access to the results folder",
				defaultOGWorkspace);

		File sourcesDir = workspace.getFile(SOURCES_DIR_NAME);
		if (!sourcesDir.exists()) {
			sourcesDir.mkdirs();
		}

		OGWorkspace ews = Services.getService(OGWorkspace.class);

		DataSourceFactory dsf = new DataSourceFactory(sourcesDir
				.getAbsolutePath(), ews.getTempFolder().getAbsolutePath());
		dsf.setResultDir(ews.getResultsFolder());

		// Pipeline the warnings in gdms to the warning system in the
		// application
		dsf.setWarninglistener(new WarningListener() {

			public void throwWarning(String msg) {
				Services.getService(ErrorManager.class).warning(msg, null);
			}

			public void throwWarning(String msg, Throwable t, Object source) {
				Services.getService(ErrorManager.class).warning(msg, t);
			}

		});

		// Installation of the service
		Services
				.registerService(
						DataManager.class,
						"Access to the sources, to its properties (indexes, etc.) and its contents, either raster or vectorial",
						new DefaultDataManager(dsf));
	}

	public static void installGeocognitionService() {
		DefaultGeocognition dg = new DefaultGeocognition();
		Services
				.registerService(
						Geocognition.class,
						"Registry containing all the artifacts produced and shared by the users",
						dg);
	}

	public static void installSymbologyServices() {
		DefaultSymbolManager sm = new DefaultSymbolManager();
		Services.registerService(SymbolManager.class,
				"Manages the list of available symbol types", sm);

		DefaultLegendManager lm = new DefaultLegendManager();
		Services.registerService(LegendManager.class,
				"Manages the list of available legend types", lm);

		sm.addSymbol(SymbolFactory.createPointCircleSymbol(Color.black, 1,
				Color.red, 10, false));
		sm.addSymbol(SymbolFactory.createPointSquareSymbol(Color.black, 1,
				Color.red, 10, false));
		sm.addSymbol(SymbolFactory.createVertexCircleSymbol(Color.black, 1,
				Color.red, 10, false));
		sm.addSymbol(SymbolFactory.createVertexSquareSymbol(Color.black, 1,
				Color.red, 10, false));
		sm.addSymbol(SymbolFactory.createPolygonCentroidSquareSymbol(
				Color.black, 1, Color.red, 10, false));
		sm.addSymbol(SymbolFactory.createPolygonCentroidCircleSymbol(
				Color.black, 1, Color.red, 10, false));
		sm.addSymbol(SymbolFactory.createPolygonSymbol());
		sm.addSymbol(SymbolFactory.createLineSymbol(Color.black, 1));
		sm.addSymbol(SymbolFactory.createImageSymbol());
		sm.addSymbol(new ArrowSymbol(8, 6, Color.red, Color.black, 1));
		sm.addSymbol(new Shading3DPolygon(Color.black, 1, Color.gray));

		lm.addLegend(LegendFactory.createUniqueSymbolLegend());
		lm.addLegend(LegendFactory.createUniqueValueLegend());
		lm.addLegend(LegendFactory.createIntervalLegend());
		lm.addLegend(LegendFactory.createProportionalLegend());
		lm.addLegend(LegendFactory.createLabelLegend());
		lm.addLegend(new RasterLegend(LutGenerator.colorModel("gray"), 1));
	}

	private static void installConfigurationService() {
		BasicConfiguration bc = new DefaultBasicConfiguration();
		Services.registerService(BasicConfiguration.class,
				"Manages the basic configurations (key, value)", bc);
		bc.load();
	}

	public static void installJavaServices() throws IOException {
		HashSet<File> buildPath = new HashSet<File>();
		ClassLoader cl = OrbisgisCoreServices.class.getClassLoader();
		while (cl != null) {
			if (cl instanceof URLClassLoader) {
				File cacheFolder = Services.getService(Workspace.class)
						.getFile("org.orbisgis.JavaManagerCache");
				FileCache fc = new FileCache(cacheFolder, ".jar");
				URLClassLoader loader = (URLClassLoader) cl;
				URL[] urls = loader.getURLs();
				for (URL url : urls) {
					try {
						File file;
						if (url.getProtocol().equals("file")) {
							file = new File(url.toURI());
						} else {
							file = fc.getFile(url);
						}
						buildPath.add(file);
					} catch (URISyntaxException e) {
						logger.error("Cannot add classpath url: " + url, e);
					}
				}
			}
			cl = cl.getParent();
		}

		DefaultJavaManager javaManager = new DefaultJavaManager();
		Services.registerService(JavaManager.class,
				"Execution of java code and java scripts", javaManager);
		javaManager.addFilesToClassPath(Arrays.asList(buildPath
				.toArray(new File[0])));

	}
}