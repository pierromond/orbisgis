/*
 * The GDMS library (Generic Datasources Management System)
 * is a middleware dedicated to the management of various kinds of
 * data-sources such as spatial vectorial data or alphanumeric. Based
 * on the JTS library and conform to the OGC simple feature access
 * specifications, it provides a complete and robust API to manipulate
 * in a SQL way remote DBMS (PostgreSQL, H2...) or flat files (.shp,
 * .csv...). GDMS is produced  by the geomatic team of the IRSTV
 * Institute <http://www.irstv.cnrs.fr/>, CNRS FR 2488:
 *    Erwan BOCHER, scientific researcher,
 *    Thomas LEDUC, scientific researcher,
 *    Fernando GONZALES CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALES CORTES, Thomas LEDUC
 *
 * This file is part of GDMS.
 *
 * GDMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GDMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GDMS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://orbisgis.cerma.archi.fr/>
 *    <http://sourcesup.cru.fr/projects/orbisgis/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-developers/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-users/>
 *
 * or contact directly:
 *    erwan.bocher _at_ ec-nantes.fr
 *    fergonco _at_ gmail.com
 *    thomas.leduc _at_ cerma.archi.fr
 */
package org.urbsat.custom;

import org.gdms.data.DataSource;
import org.gdms.data.DataSourceFactory;
import org.gdms.data.ExecutionException;
import org.gdms.data.values.Value;
import org.gdms.driver.DriverException;
import org.gdms.spatial.SpatialDataSourceDecorator;
import org.gdms.sql.customQuery.CustomQuery;
import org.orbisgis.core.windows.EPWindowHelper;
import org.orbisgis.geoview.GeoView2D;
import org.orbisgis.geoview.views.geomark.GeomarkPanel;
import org.orbisgis.tools.Rectangle2DDouble;

import com.vividsolutions.jts.geom.Envelope;

public class Geomark implements CustomQuery {
	public DataSource evaluate(DataSourceFactory dsf, DataSource[] tables,
			Value[] values) throws ExecutionException {
		if (tables.length != 1) {
			throw new ExecutionException(
					"Geomark only operates on a single spatial table");
		}
		if (values.length != 0) {
			throw new ExecutionException(
					"Geomark does not accept any field name");
		}

		final GeoView2D geoview = (GeoView2D) EPWindowHelper
				.getWindows("org.orbisgis.geoview.Window")[0];
		final GeomarkPanel geomark = (GeomarkPanel) geoview
				.getView("org.orbisgis.geoview.GeoMark");
		final String prefix = tables[0].getName() + "-";

		try {
			final SpatialDataSourceDecorator sds = new SpatialDataSourceDecorator(
					tables[0]);
			sds.open();
			final int rowCount = (int) sds.getRowCount();
			for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
				final Envelope envelope = sds.getGeometry(rowIndex)
						.getEnvelopeInternal();
				final String key = prefix + rowIndex;
				final Rectangle2DDouble r = new Rectangle2DDouble(envelope
						.getMinX(), envelope.getMinY(), envelope.getWidth(),
						envelope.getHeight());
				geomark.listModel.addElement(key);
				geomark.geomarksMap.put(key, r);
			}
			sds.cancel();
			return null;
		} catch (DriverException e) {
			throw new ExecutionException(e);
		}
	}

	public String getName() {
		return "Geomark";
	}

	public String getSqlOrder() {
		return "select Geomark() from myTable;";
	}

	public String getDescription() {
		return "Stores each spatial field envelope as a new geomark.";
	}
}