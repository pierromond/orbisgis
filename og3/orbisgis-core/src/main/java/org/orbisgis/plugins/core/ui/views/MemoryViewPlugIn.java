package org.orbisgis.plugins.core.ui.views;

import java.util.Observable;

import javax.swing.JMenuItem;

import org.orbisgis.plugins.core.ui.PlugInContext;
import org.orbisgis.plugins.core.ui.ViewPlugIn;
import org.orbisgis.plugins.core.ui.workbench.Names;

public class MemoryViewPlugIn extends ViewPlugIn {

	private ViewPanel panel;
	private JMenuItem menuItem;

	public MemoryViewPlugIn() {

	}

	public void initialize(PlugInContext context) throws Exception {
		panel = new ViewPanel();
		menuItem = context.getFeatureInstaller().addMainMenuItem(this,
				new String[] { Names.VIEW }, Names.MEMORY, true,
				getIcon(Names.MEMORY_ICON), null, panel, null, null,
				context.getWorkbenchContext());
	}

	public boolean execute(PlugInContext context) throws Exception {
		getUpdateFactory().loadView(getId());
		return true;
	}

	public void update(Observable o, Object arg) {
		setSelected();
	}

	public void setSelected() {
		menuItem.setSelected(isVisible());
	}

	public boolean isVisible() {
		return getUpdateFactory().viewIsOpen(getId());
	}

}