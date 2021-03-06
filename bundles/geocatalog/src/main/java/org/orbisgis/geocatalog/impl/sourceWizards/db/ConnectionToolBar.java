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
package org.orbisgis.geocatalog.impl.sourceWizards.db;

import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToolBar;
import org.orbisgis.commons.events.EventException;
import org.orbisgis.commons.events.ListenerContainer;
import org.orbisgis.commons.events.OGVetoableChangeSupport;
import org.orbisgis.frameworkapi.CoreWorkspace;
import org.orbisgis.geocatalog.icons.GeocatalogIcon;
import org.orbisgis.sif.UIFactory;
import org.orbisgis.sif.multiInputPanel.MultiInputPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

/**
 * The ConnectionToolBar class is used to manage the connection to a database
 * and a list of connection from several database. This list are stored on the
 * current OrbisGIS folder with the name db_connexions.properties.
 *
 * @author ebocher
 */
public class ConnectionToolBar extends JToolBar {

        private static final I18n I18N = I18nFactory.getI18n(ConnectionToolBar.class);
        private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionToolBar.class);
        private static final String DB_PROPERTIES_FILE = "db_connexions.properties";
        private Properties dbProperties = new Properties();
        private JComboBox cmbDataBaseUri;
        private JButton btnConnect;
        private JButton btnDisconnect;
        private JButton btnAddConnection;
        private JButton btnEditConnection;
        private JButton btnRemoveConnection;
        private ListenerContainer<DBMessageEvents> messagesEvents = new ListenerContainer<>();
        private boolean connected = false;
        public static final String PROP_CONNECTED = "connected";
        List<Driver> jdbcDrivers = new ArrayList<>();
        private CoreWorkspace ws;

        /*
         * Create the toolbar with all components : combobox, buttons
         */
        public ConnectionToolBar() {
                init();
        }

        /**
         * User preferencies are saved in app folder.
         * @param coreWorkspace Instance of coreWorkspace or null.
         */
        public void setCoreWorkspace(CoreWorkspace coreWorkspace) {
                this.ws = coreWorkspace;
        }

        /**
         * Add DataSourceFactory used to show the list of supported JDBC Drivers
         * @param driver DataSourceFactory implementation
         */
        public void addDriver(Driver driver) {
            jdbcDrivers.add(driver);
        }

        /**
         * Remove DataSourceFactory used to show the list of supported JDBC Drivers
         * @param driver DataSourceFactory implementation
         */
        public boolean removeDriver(Driver driver) {
            return jdbcDrivers.remove(driver);
        }

        /**
         * A listener to manage some event messages
         *
         * @return
         */
        public ListenerContainer<DBMessageEvents> getMessagesEvents() {
                return messagesEvents;
        }

        /**
         * Create all components
         */
        private void init() {
                setFloatable(false);
                setOpaque(false);
                loadDBProperties();
                Object[] dbKeys = dbProperties.keySet().toArray();
                boolean btnStatus = dbKeys.length > 0;
                cmbDataBaseUri = new JComboBox(dbKeys);
                cmbDataBaseUri.setEditable(false);
                //Connection button
                btnConnect = new JButton(GeocatalogIcon.getIcon("database_connect"));
                btnConnect.setToolTipText(I18N.tr("Connect to the database"));
                btnConnect.setEnabled(btnStatus);
                btnConnect.addActionListener(EventHandler.create(ActionListener.class, this, "onConnect"));
                btnConnect.setBorderPainted(false);
                //Button for disconnecting.
                btnDisconnect = new JButton(GeocatalogIcon.getIcon("disconnect"));
                btnDisconnect.setToolTipText(I18N.tr("Disconnect"));
                btnDisconnect.setEnabled(false);
                btnDisconnect.addActionListener(EventHandler.create(ActionListener.class, this, "onDisconnect"));
                btnDisconnect.setBorderPainted(false);
                //Button to add a conection.
                btnAddConnection = new JButton(GeocatalogIcon.getIcon("database_add"));
                btnAddConnection.setToolTipText(I18N.tr("Add a new connection"));
                btnAddConnection.addActionListener(EventHandler.create(ActionListener.class, this, "onAddConnection"));
                btnAddConnection.setBorderPainted(false);
                //button to edit a connection
                btnEditConnection = new JButton(GeocatalogIcon.getIcon("database_edit"));
                btnEditConnection.setToolTipText(I18N.tr("Edit the connection"));
                btnEditConnection.setEnabled(btnStatus);
                btnEditConnection.addActionListener(EventHandler.create(ActionListener.class, this, "onEditConnection"));
                btnEditConnection.setBorderPainted(false);
                //button to remove a connection
                btnRemoveConnection = new JButton(GeocatalogIcon.getIcon("database_delete"));
                btnRemoveConnection.setToolTipText(I18N.tr("Remove the connection"));
                btnRemoveConnection.setEnabled(btnStatus);
                btnRemoveConnection.addActionListener(EventHandler.create(ActionListener.class, this, "onRemoveConnection"));
                btnRemoveConnection.setBorderPainted(false);
                //The toolbar that contains these buttons.
                add(cmbDataBaseUri);
                add(btnConnect);
                add(btnDisconnect);
                add(btnAddConnection);
                add(btnEditConnection);
                add(btnRemoveConnection);
        }

        /**
         * Load the connection properties file.
         */
        private void loadDBProperties() {
                if(ws != null) {
                        try {
                                File propertiesFile = new File(ws.getWorkspaceFolder() + File.separator + DB_PROPERTIES_FILE);


                                if (propertiesFile.exists()) {
                                        dbProperties.load(new FileInputStream(propertiesFile));
                                }
                        } catch (IOException e) {
                                LOGGER.error(e.getLocalizedMessage(), e);
                        }
                }
        }

        /**
         * Save the connection properties file
         */
        public void saveProperties() {
                if(ws != null) {
                        try {
                                dbProperties.store(new FileOutputStream(ws.getWorkspaceFolder() + File.separator + DB_PROPERTIES_FILE), I18N.tr("Saved with the OrbisGIS database panel"));
                        } catch (IOException ex) {
                                LOGGER.error(ex.getLocalizedMessage(), ex);
                        }
                }
        }

        /**
         * Return the combobox component that contains all string connection
         * parameters
         *
         * @return
         */
        public JComboBox getCmbDataBaseUri() {
                return cmbDataBaseUri;
        }

        /**
         * Return all string connection parameters stored in the properties
         * file.
         *
         * @return
         */
        public Properties getDbProperties() {
                return dbProperties;
        }

        /**
         * Get the value of connected
         *
         * @return the value of connected
         */
        public boolean isConnected() {
                return connected;
        }

        /**
         * Set the value of connected
         *
         * @param connected new value of connected
         * @throws java.beans.PropertyVetoException
         */
        public void setConnected(boolean connected) throws java.beans.PropertyVetoException {
                boolean oldConnected = this.connected;
                vetoableChangeSupport.fireVetoableChange(PROP_CONNECTED, oldConnected, connected);
                fireVetoableChange(PROP_CONNECTED, oldConnected, connected);
                this.connected = connected;
                firePropertyChange(PROP_CONNECTED, oldConnected, connected);
        }
        private transient final VetoableChangeSupport vetoableChangeSupport = new OGVetoableChangeSupport(this);

        /**
         * Add a VetoableChangeListener for a specific property.
         *
         * @param listener
         */
        public void addVetoableChangeListener(String property, VetoableChangeListener listener) {
                vetoableChangeSupport.addVetoableChangeListener(property, listener);
        }

        /**
         * Connect to the database
         */
        public void onConnect() {
                try {
                        setConnected(true);
                } catch (PropertyVetoException ex) {
                        return;
                }
                onUserSelectionChange();

        }

        /**
         * Disconnect from the database and update all buttons
         */
        public void onDisconnect() {
                try {
                        setConnected(false);
                } catch (PropertyVetoException ex) {
                        return;
                }
                onUserSelectionChange();

        }

        /**
         * Change the status of the components
         */
        private void onUserSelectionChange() {
            boolean isCmbEmpty = getCmbDataBaseUri().getItemCount() == 0;

            btnConnect.setEnabled(!isCmbEmpty && !isConnected());
            btnDisconnect.setEnabled(!isCmbEmpty && isConnected());
            btnAddConnection.setEnabled(isCmbEmpty && !isConnected());
            btnEditConnection.setEnabled(!isCmbEmpty && !isConnected());
            btnRemoveConnection.setEnabled(!isCmbEmpty && !isConnected());
            cmbDataBaseUri.setEnabled(isCmbEmpty && !isConnected());
        }

        /**
         * Add a new connection
         */
        public void onAddConnection() throws EventException {
                MultiInputPanel mip = DBUIFactory.getConnectionPanel(jdbcDrivers);
                if (UIFactory.showDialog(mip)) {
                        String connectionName = mip.getInput(DBUIFactory.CONNAME);
                        if (!getDbProperties().containsKey(connectionName)) {
                                StringBuilder sb = new StringBuilder();
                                sb.append(mip.getInput(DBUIFactory.DBTYPE));
                                sb.append(",");
                                sb.append(mip.getInput(DBUIFactory.HOST));
                                sb.append(",");
                                sb.append(mip.getInput(DBUIFactory.PORT));
                                sb.append(",");
                                sb.append(mip.getInput(DBUIFactory.SSL));
                                sb.append(",");
                                sb.append(mip.getInput(DBUIFactory.DBNAME));
                                getDbProperties().setProperty(connectionName, sb.toString());
                                getCmbDataBaseUri().addItem(connectionName);
                                getCmbDataBaseUri().setSelectedItem(connectionName);
                                onUserSelectionChange();
                                saveProperties();

                        } else {
                                messagesEvents.callListeners(new DBMessageEvents(I18N.tr("There is already a connection with this name."), this));

                        }

                }

        }

        /**
         * Edit a connection to change its parameters
         */
        public void onEditConnection() {
                String dataBaseUri = getCmbDataBaseUri().getSelectedItem().toString();
                if (!dataBaseUri.isEmpty()) {
                        String property = getDbProperties().getProperty(dataBaseUri);
                        MultiInputPanel mip = DBUIFactory.getEditConnectionPanel(dataBaseUri, property, jdbcDrivers);
                        if (UIFactory.showDialog(mip)) {
                                String connectionName = mip.getInput(DBUIFactory.CONNAME);
                                StringBuilder sb = new StringBuilder();
                                sb.append(mip.getInput(DBUIFactory.DBTYPE));
                                sb.append(",");
                                sb.append(mip.getInput(DBUIFactory.HOST));
                                sb.append(",");
                                sb.append(mip.getInput(DBUIFactory.PORT));
                                sb.append(",");
                                sb.append(mip.getInput(DBUIFactory.SSL));
                                sb.append(",");
                                sb.append(mip.getInput(DBUIFactory.DBNAME));
                                getDbProperties().setProperty(connectionName, sb.toString());
                                getCmbDataBaseUri().addItem(connectionName);
                                getCmbDataBaseUri().setSelectedItem(connectionName);
                                saveProperties();
                        }
                }
        }

        /**
         * Remove a connection
         */
        public void onRemoveConnection() {
                String dataBaseUri = getCmbDataBaseUri().getSelectedItem().toString();
                if (!dataBaseUri.isEmpty()) {
                        getCmbDataBaseUri().removeItem(dataBaseUri);
                        getDbProperties().remove(dataBaseUri);
                        saveProperties();
                }
                onUserSelectionChange();
        }
}
