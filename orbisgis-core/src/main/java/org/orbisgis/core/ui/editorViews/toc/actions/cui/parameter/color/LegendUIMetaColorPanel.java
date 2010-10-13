/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. OrbisGIS is
 * distributed under GPL 3 license. It is produced by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/> CNRS FR 2488.
 *
 *
 *  Team leader Erwan BOCHER, scientific researcher,
 *
 *  User support leader : Gwendall Petit, geomatic engineer.
 *
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * Copyright (C) 2010 Erwan BOCHER, Pierre-Yves FADET, Alexis GUEGANNO, Maxence LAURENT
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
 *
 * or contact directly:
 * erwan.bocher _at_ ec-nantes.fr
 * gwendall.petit _at_ ec-nantes.fr
 */
package org.orbisgis.core.ui.editorViews.toc.actions.cui.parameter.color;

import org.orbisgis.core.ui.editorViews.toc.actions.cui.type.LegendUIPropertyNameType;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.type.LegendUICategorizeType;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.type.LegendUIColorLiteralType;
import javax.swing.Icon;
import org.orbisgis.core.images.OrbisGISIcon;
import org.orbisgis.core.renderer.se.parameter.Categorize;
import org.orbisgis.core.renderer.se.parameter.PropertyName;
import org.orbisgis.core.renderer.se.parameter.Recode;
import org.orbisgis.core.renderer.se.parameter.color.Categorize2Color;
import org.orbisgis.core.renderer.se.parameter.color.ColorAttribute;
import org.orbisgis.core.renderer.se.parameter.color.ColorLiteral;
import org.orbisgis.core.renderer.se.parameter.color.ColorParameter;
import org.orbisgis.core.renderer.se.parameter.color.Recode2Color;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.type.LegendUIEmptyPanelType;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.LegendUIAbstractMetaPanel;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.LegendUIComponent;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.LegendUIController;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.LegendUIEmptyPanel;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.parameter.LegendUICategorizePanel;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.parameter.LegendUIPropertyNamePanel;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.parameter.LegendUIRecodePanel;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.type.LegendUIType;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.type.LegendUIRecodeType;

/**
 *
 * @author maxence
 */
public abstract class LegendUIMetaColorPanel extends LegendUIAbstractMetaPanel {

	private ColorParameter color;

	private LegendUIType initialType;
	private LegendUIComponent initialPanel;
	private LegendUIType[] types;


	public LegendUIMetaColorPanel(String name, LegendUIController controller, LegendUIComponent parent, ColorParameter c) {
		super(name, controller, parent, 0);

		this.color = c;

		types = new LegendUIType[5];

		types[0] = new LegendUIEmptyPanelType("no " + name, controller);
		types[1] = new LegendUIColorLiteralType("Constant " + name, controller);
		types[2] = new LegendUIPropertyNameType("Attribute " + name, controller, ColorAttribute.class);
		types[3] = new LegendUICategorizeType("Categorized " + name, controller, Categorize2Color.class);
		types[4] = new LegendUIRecodeType("UniqueValue mapping " + name, controller, Recode2Color.class);

		if (this.color instanceof ColorLiteral) {
			initialType = types[1];
			initialPanel = new LegendUIColorLiteralPanel("Constant " + name, controller, this, (ColorLiteral) color);
		} else if (this.color instanceof PropertyName) {
			initialType = types[2];
			initialPanel = new LegendUIPropertyNamePanel("Attribute " + name, controller, this, (ColorAttribute)c);
		} else if (this.color instanceof Categorize) {
			initialType = types[3];
			initialPanel = new LegendUICategorizePanel("Categorized " + name, controller, this, (Categorize) this.color);
		} else if (this.color instanceof Recode) {
			initialType = types[4];
			initialPanel = new LegendUIRecodePanel("UniqueValue map " + name, controller, this, (Recode) this.color);
		}else{
			initialType = types[0];
			initialPanel = new LegendUIEmptyPanel("no " + name, controller, this);
		}
	}

	@Override
	public void init(){
		init(types, initialType, initialPanel);
	}

	@Override
	protected void switchTo(LegendUIType type, LegendUIComponent comp) {
		this.color = ((LegendUIColorComponent)comp).getColorParameter();
		this.colorChanged(color);
	}

	@Override
	public Icon getIcon() {
		return OrbisGISIcon.PALETTE;
	}
	
	public abstract void colorChanged(ColorParameter newColor);
}