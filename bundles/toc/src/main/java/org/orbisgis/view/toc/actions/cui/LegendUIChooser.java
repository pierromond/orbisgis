/**
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the 
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
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
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.view.toc.actions.cui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.h2gis.utilities.GeometryTypeCodes;
import org.h2gis.utilities.SFSUtilities;
import org.h2gis.utilities.TableLocation;
import org.orbisgis.coremap.layerModel.ILayer;
import org.orbisgis.legend.thematic.categorize.CategorizedArea;
import org.orbisgis.legend.thematic.categorize.CategorizedLine;
import org.orbisgis.legend.thematic.categorize.CategorizedPoint;
import org.orbisgis.legend.thematic.constant.UniqueSymbolArea;
import org.orbisgis.legend.thematic.constant.UniqueSymbolLine;
import org.orbisgis.legend.thematic.constant.UniqueSymbolPoint;
import org.orbisgis.legend.thematic.proportional.ProportionalLine;
import org.orbisgis.legend.thematic.proportional.ProportionalPoint;
import org.orbisgis.legend.thematic.recode.RecodedArea;
import org.orbisgis.legend.thematic.recode.RecodedLine;
import org.orbisgis.legend.thematic.recode.RecodedPoint;
import org.orbisgis.sif.UIFactory;
import org.orbisgis.sif.UIPanel;
import org.orbisgis.view.toc.Toc;
import org.orbisgis.view.toc.actions.cui.legend.ILegendPanel;
import org.orbisgis.view.toc.actions.cui.legend.ILegendPanelFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.sql.DataSource;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Displays a UI with a list of the legends supported by the layer being
 * edited in the SimpleStyleEditor and a preview of each legend.
 *
 * @author Erwan Bocher
 * @author Adam Gouge
 */
public class LegendUIChooser implements UIPanel {

    private static final I18n I18N = I18nFactory.getI18n(Toc.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(LegendUIChooser.class);

    /**
     * SimpleStyleEditor.
     */
    private LegendContext editor;
    /**
     * Possible Legend names.
     */
    private String[] names;
    /**
     * The list of analyses in the UI.
     */
    private JList lst;
    /**
     * The UI.
     */
    private JPanel mainPanel;
    /**
     * Illustration of the analysis in the UI.
     */
    private JLabel imageLabel;

    /**
     * Construct a {@link LegendUIChooser} for the layer being edited
     * in the given {@link SimpleStyleEditor} instance.
     *
     * @param editor SimpleStyleEditor
     */
    public LegendUIChooser(LegendContext editor) {
        this.editor = editor;
        initNamesList(editor.getLayer());
        initUI();
    }

    /**
     * Populates the names array with the names of legends that can be applied
     * to the given layer.
     *
     * @param layer Layer
     */
    private void initNamesList(ILayer layer) {
        // Recover the geometry type of this ILayer.
        DataSource ds = layer.getDataManager().getDataSource();
        int geometryColumnsCount = 0;
        int geomType = GeometryTypeCodes.GEOMETRY;
        try(Connection connection = ds.getConnection()) {
            TableLocation tableLocation = TableLocation.parse(layer.getTableReference());
            geomType = SFSUtilities.getGeometryType(connection, tableLocation, "");
            geometryColumnsCount = SFSUtilities.getGeometryFields(connection, tableLocation).size();
        } catch (SQLException e) {
            LOGGER.warn("Could not determine the specific geometry type for " +
                    "this layer.");
        }
        // Convert to a simple geometry.
        int simpleGeomType = SimpleGeometryType.getSimpleType(geomType);
        // Fill the names array.
        ArrayList<String> typeNames = new ArrayList<>();
        final int lineOrPolygon =
                SimpleGeometryType.LINE| SimpleGeometryType.POLYGON;
        if ((simpleGeomType & SimpleGeometryType.ALL) != 0) {
            typeNames.add(UniqueSymbolPoint.NAME);
            if ((simpleGeomType & lineOrPolygon) != 0) {
                typeNames.add(UniqueSymbolLine.NAME);
            }
            if ((simpleGeomType & SimpleGeometryType.POLYGON) != 0) {
                typeNames.add(UniqueSymbolArea.NAME);
            }
            //Do not display the thematic maps that need an attribute if there is
            //only one geometry
            if(geometryColumnsCount>-1){
            typeNames.add(RecodedPoint.NAME);
            if ((simpleGeomType & lineOrPolygon) != 0) {
                typeNames.add(RecodedLine.NAME);
            }
            if ((simpleGeomType & SimpleGeometryType.POLYGON) != 0) {
                typeNames.add(RecodedArea.NAME);
            }
            typeNames.add(ProportionalPoint.NAME);
            if ((simpleGeomType & lineOrPolygon) != 0) {
                typeNames.add(ProportionalLine.NAME);
            }
            typeNames.add(CategorizedPoint.NAME);
            if ((simpleGeomType & lineOrPolygon) != 0) {
                typeNames.add(CategorizedLine.NAME);
            }
            if ((simpleGeomType & SimpleGeometryType.POLYGON) != 0) {
                typeNames.add(CategorizedArea.NAME);
            }
            }
        }
        names = typeNames.toArray(new String[typeNames.size()]);
    }

    /**
     * Builds the UI.
     */
    private void initUI() {
        // Initialize the JList
        DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < names.length; i++) {
            model.addElement(names[i]);
        }
        lst = new JList(model);
        // Add a selection listener
        lst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lst.addListSelectionListener(
            new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent lse) {
                    onThumbnailChange(lse);
                }
            });
        lst.setSelectedIndex(0); // calls onThumbnailChange(lse)
        // Add components.
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(lst), BorderLayout.WEST);
        mainPanel.add(imageLabel, BorderLayout.CENTER);
    }

    /**
     * Change the thumbnail according the type of legend.
     */
    public void onThumbnailChange(ListSelectionEvent lse) {
        if (imageLabel == null) {
            imageLabel = new JLabel();
        }
        imageLabel.setIcon(getThumbnail());
    }

    /**
     * Create the thumbnail image
     *
     * @return
     */
    public ImageIcon getThumbnail() {
        String legendName = names[lst.getSelectedIndex()];
        if (legendName.equals(UniqueSymbolPoint.NAME)) {
            return createImageIcon("thumbnail_unique_point.png");
        } else if (legendName.equals(UniqueSymbolLine.NAME)) {
            return createImageIcon("thumbnail_unique_line.png");
        } else if (legendName.equals(UniqueSymbolArea.NAME)) {
            return createImageIcon("thumbnail_unique_area.png");
        } else if (legendName.equals(ProportionalPoint.NAME)) {
            return createImageIcon("thumbnail_proportionnal_point.png");
        } else if (legendName.equals(ProportionalLine.NAME)) {
            return createImageIcon("thumbnail_proportionnal_line.png");
        } else if (legendName.equals(RecodedPoint.NAME)) {
            return createImageIcon("thumbnail_recoded_point.png");
        } else if (legendName.equals(RecodedLine.NAME)) {
            return createImageIcon("thumbnail_recoded_line.png");
        } else if (legendName.equals(RecodedArea.NAME)) {
            return createImageIcon("thumbnail_recoded_area.png");
        } else if (legendName.equals(CategorizedPoint.NAME)) {
            return createImageIcon("thumbnail_interval_point.png");
        } else if (legendName.equals(CategorizedLine.NAME)) {
            return createImageIcon("thumbnail_interval_line.png");
        } else if (legendName.equals(CategorizedArea.NAME)) {
            return createImageIcon("thumbnail_interval_area.png");
        } else {
            return createImageIcon("thumbnail_not_found.png");
        }
    }

    /**
     * Returns a new {@link ILegendPanel} corresponding to the legend selected
     * in the JList, initialized according to the local {@link LegendContext}.
     *
     * @return Panel corresponding to selected legend, initialized appropriately.
     */
    public ILegendPanel getSelectedPanel() {
        String selectedLegendTypeName = names[lst.getSelectedIndex()];
        ILegendPanel panel =
                ILegendPanelFactory.getPanelForLegendUIChooser(
                        editor, selectedLegendTypeName);
        return panel;
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = LegendUIChooser.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            LOGGER.error(I18N.tr("Couldn't find file") + ": " + path);
            return null;
        }
    }

    // *********************** UI Panel methods ******************************

    @Override
    public URL getIconURL() {
        return UIFactory.getDefaultIcon();
    }

    @Override
    public String getTitle() {
        return I18N.tr("Select Legend");
    }

    @Override
    public String validateInput() {
        if (lst.getSelectedIndex() == -1) {
            return UIFactory.getI18n().tr("You must select a legend.");
        }
        return null;
    }

    @Override
    public Component getComponent() {
        return mainPanel;
    }
}
