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
package org.orbisgis.sif.components.actions;

import javax.swing.Action;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * A class that hold swing Actions. Used by plugin system helper {@link MenuItemServiceTracker}.
 * @author Nicolas Fortin
 */
public interface ActionsHolder {
        public static final String PROP_ACTIONS = "actions";

        /**
         * Add action and show in registered control.
         * @param action action to show
         */
        void addAction(Action action);
        /**
         * Add action list and show in all registered controls.
         * @param newActions Action list
         */
        void addActions(List<Action> newActions);

        /**
         * Add actions using this action factory component
         * @param factory New action factory
         * @param targetComponent New actions will be related to this instance. {@link org.orbisgis.sif.components
         * .actions.ActionFactoryService#createActions(Object)}
         * @param <TargetComponent> Class related to actions.
         * @throws java.lang.IllegalArgumentException If this factory is already used by this actions holder.
         */
        <TargetComponent> void addActionFactory(ActionFactoryService<TargetComponent> factory,
                                                TargetComponent targetComponent) throws IllegalArgumentException;


        /**
         * Remove actions related to this action factory service
         * @param actionFactoryService Action factory already added to this actions holder
         */
        <TargetComponent> void removeActionFactory(ActionFactoryService<TargetComponent> actionFactoryService);

        /**
         * Remove this action list of all registered controls.
         * PropertyChange listeners of action will remove all related menu items.
         * @param action action to remove
         * @return True if the action has been successfully removed
         */
        public boolean removeAction(Action action) ;
        /**
         * Remove this action list of all registered controls.
         * PropertyChange listeners of action will remove all related menu items.
         * @param actionList
         */
        public void removeActions(List<Action> actionList);


        /**
         * Add a property-change listener for all properties.
         * The listener is called for all properties.
         * @param listener The PropertyChangeListener instance
         * @note Use EventHandler.create to build the PropertyChangeListener instance
         */
        public void addPropertyChangeListener(PropertyChangeListener listener);

        /**
         * Add a property-change listener for a specific property.
         * The listener is called only when there is a change to
         * the specified property.
         * @param prop The static property name PROP_..
         * @param listener The PropertyChangeListener instance
         * @note Use EventHandler.create to build the PropertyChangeListener instance
         */
        public void addPropertyChangeListener(String prop,PropertyChangeListener listener);
        /**
         * Remove the specified listener from the list
         * @param listener The listener instance
         */
        public void removePropertyChangeListener(PropertyChangeListener listener);

        /**
         * Remove the specified listener for a specified property from the list
         * @param prop The static property name PROP_..
         * @param listener The listener instance
         */
        public void removePropertyChangeListener(String prop,PropertyChangeListener listener);
}
