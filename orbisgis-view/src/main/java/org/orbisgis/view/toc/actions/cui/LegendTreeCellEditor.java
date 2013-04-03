/**
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information.
 *
 * OrbisGIS is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2007-2012 IRSTV (FR CNRS 2488)
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

import org.orbisgis.view.toc.actions.cui.legend.ILegendPanel;
import org.orbisgis.view.toc.actions.cui.legends.PnlRule;
import org.orbisgis.view.toc.wrapper.RuleWrapper;
import org.orbisgis.view.toc.wrapper.StyleWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * @author alexis
 */
public class LegendTreeCellEditor extends DefaultCellEditor {
    private JTextField field = new JTextField(20);
    Object obj;

    public LegendTreeCellEditor(){
        this(new JTextField(20));
    }

    private LegendTreeCellEditor(JTextField field){
        super(field);
        this.field = field;
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        obj = value;
        if(value instanceof StyleWrapper){
            StyleWrapper sw = (StyleWrapper) value;
            return super.getTreeCellEditorComponent(tree, sw.getStyle().getName(), isSelected, expanded, leaf, row);
        } else if(value instanceof RuleWrapper){
            RuleWrapper sw = (RuleWrapper) value;
            return super.getTreeCellEditorComponent(tree, sw.getRule().getName(), isSelected, expanded, leaf, row);
        } else if(value  instanceof ILegendPanel){
            ILegendPanel ilp = (ILegendPanel) value;
            return super.getTreeCellEditorComponent(tree, ilp.getLegend().getName(), isSelected, expanded, leaf, row);
        }
        return null;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent){
		if (anEvent instanceof MouseEvent) {
            MouseEvent me = (MouseEvent) anEvent;
             return me.getClickCount() >= 2;
		}
        return true;
    }

    @Override
    public Object getCellEditorValue() {
        if(obj instanceof StyleWrapper){
            StyleWrapper sw = (StyleWrapper) obj;
            sw.getStyle().setName(field.getText());
        } else if(obj instanceof RuleWrapper){
            RuleWrapper sw = (RuleWrapper) obj;
            sw.getRule().setName(field.getText());
            PnlRule pr = (PnlRule) sw.getPanel();
            pr.setTextFieldContent(field.getText());
        } else if(obj  instanceof ILegendPanel){
            ILegendPanel ilp = (ILegendPanel) obj;
            ilp.getLegend().setName(field.getText());
        }
        return obj;
    }
}
