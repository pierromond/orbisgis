/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.core.renderer.se.label;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.IOException;
import javax.xml.bind.JAXBElement;
import org.gdms.data.SpatialDataSourceDecorator;
import net.opengis.se._2_0.core.LabelType;

import org.orbisgis.core.map.MapTransform;
import org.orbisgis.core.renderer.RenderContext;

import net.opengis.se._2_0.core.LineLabelType;
import net.opengis.se._2_0.core.PointLabelType;
import org.orbisgis.core.renderer.se.SeExceptions.InvalidStyle;
import org.orbisgis.core.renderer.se.SymbolizerNode;
import org.orbisgis.core.renderer.se.UomNode;
import org.orbisgis.core.renderer.se.common.Uom;
import org.orbisgis.core.renderer.se.parameter.ParameterException;

/**
 *
 * @author maxence
 */
public abstract class Label implements SymbolizerNode, UomNode {

    public enum HorizontalAlignment {

        LEFT, CENTER, RIGHT;

        public static HorizontalAlignment fromString(String token) {
            if (token.equals("left")) {
                return LEFT;
            }

            if (token.equals("center")) {
                return CENTER;
            }

            return RIGHT; // default value
        }

        public static String[] getList() {
            String[] list = new String[values().length];
            for (int i = 0; i < values().length; i++) {
                list[i] = values()[i].name();
            }
            return list;
        }
    }

    public enum VerticalAlignment {

        TOP, MIDDLE, BASELINE, BOTTOM;

        public static VerticalAlignment fromString(String token) {
            if (token.equals("bottom")) {
                return BOTTOM;
            }
            if (token.equals("middle")) {
                return MIDDLE;
            }
            if (token.equals("baseline")) {
                return BASELINE;
            }

            return TOP;
        }

        public static String[] getList() {
            String[] list = new String[values().length];
            for (int i = 0; i < values().length; i++) {
                list[i] = values()[i].name();
            }
            return list;
        }
    }
    protected SymbolizerNode parent;
    protected Uom uom;
    protected StyledText label;
    protected HorizontalAlignment hAlign;
    protected VerticalAlignment vAlign;

    public static Label createLabelFromJAXBElement(JAXBElement<? extends LabelType> l) throws InvalidStyle {
        if (l.getDeclaredType() == PointLabelType.class) {
            return new PointLabel((JAXBElement<PointLabelType>) l);
        } else if (l.getDeclaredType() == LineLabelType.class) {
            return new LineLabel((JAXBElement<LineLabelType>) l);
        }

        return null;
    }

    protected Label() {
        setLabel(new StyledText());
    }

    protected Label(LabelType t) throws InvalidStyle {
        if (t.getUom() != null) {
            this.uom = Uom.fromOgcURN(t.getUom());
        }

        if (t.getStyledText() != null) {
            this.setLabel(new StyledText(t.getStyledText()));
        }
    }

    protected Label(JAXBElement<? extends LabelType> l) throws InvalidStyle {
        this(l.getValue());
    }

    @Override
    public Uom getOwnUom() {
        return uom;
    }

    @Override
    public Uom getUom() {
        if (uom != null) {
            return uom;
        } else {
            return parent.getUom();
        }
    }

    @Override
    public void setUom(Uom uom) {
        this.uom = uom;
    }

    @Override
    public SymbolizerNode getParent() {
        return parent;
    }

    @Override
    public void setParent(SymbolizerNode node) {
        parent = node;
    }

    public StyledText getLabel() {
        return label;
    }

    public final void setLabel(StyledText label) {
        this.label = label;
        label.setParent(this);
    }

    public HorizontalAlignment gethAlign() {
        return hAlign;
    }

    public void sethAlign(HorizontalAlignment hAlign) {
        if (hAlign != null) {
            this.hAlign = hAlign;
        }
    }

    public VerticalAlignment getvAlign() {
        return vAlign;
    }

    public void setvAlign(VerticalAlignment vAlign) {
        if (vAlign != null) {
            this.vAlign = vAlign;
        }
    }

    public abstract void draw(Graphics2D g2, SpatialDataSourceDecorator sds, long fid,
            Shape shp, boolean selected, MapTransform mt, RenderContext perm)
            throws ParameterException, IOException;

    public abstract JAXBElement<? extends LabelType> getJAXBElement();

    public abstract String dependsOnFeature();
}