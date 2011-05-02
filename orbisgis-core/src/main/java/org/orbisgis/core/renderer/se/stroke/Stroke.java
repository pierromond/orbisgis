package org.orbisgis.core.renderer.se.stroke;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.IOException;
import javax.xml.bind.JAXBElement;
import org.orbisgis.core.renderer.persistance.se.StrokeType;
import org.gdms.data.feature.Feature;
import org.orbisgis.core.map.MapTransform;
import org.orbisgis.core.renderer.persistance.se.CompoundStrokeType;
import org.orbisgis.core.renderer.persistance.se.GraphicStrokeType;
import org.orbisgis.core.renderer.persistance.se.PenStrokeType;
import org.orbisgis.core.renderer.persistance.se.TextStrokeType;
import org.orbisgis.core.renderer.se.SeExceptions.InvalidStyle;
import org.orbisgis.core.renderer.se.SymbolizerNode;
import org.orbisgis.core.renderer.se.UomNode;
import org.orbisgis.core.renderer.se.common.Uom;
import org.orbisgis.core.renderer.se.parameter.ParameterException;

/**
 * Style description for linear features (Area or Line)
 *
 * @todo create subclasses : TextStroke, CompoundStroke and StrokeReference
 * @author maxence
 */
public abstract class Stroke implements SymbolizerNode, UomNode {

	protected Uom uom;
    protected SymbolizerNode parent;

    /**
     * Create a new stroke based on the jaxbelement
     *
     * @param s XML Stroke
     * @return Java Stroke
     */
    public static Stroke createFromJAXBElement(JAXBElement<? extends StrokeType> s) throws InvalidStyle{
        if (s.getDeclaredType() == PenStrokeType.class){
            return new PenStroke((JAXBElement<PenStrokeType>)s);
        } else if (s.getDeclaredType() == GraphicStrokeType.class){
            return new GraphicStroke((JAXBElement<GraphicStrokeType>)s);
        }else if (s.getDeclaredType() == CompoundStrokeType.class){
			return new CompoundStroke((JAXBElement<CompoundStrokeType>)s);
        }else if (s.getDeclaredType() == TextStrokeType.class){
			return new TextStroke((JAXBElement<TextStrokeType>)s);
		}

        // TODO Shoudl never occurs !
        return null;
    }

	@Override
    public void setUom(Uom uom) {
        this.uom = uom;
    }

	@Override
	public Uom getOwnUom(){
		return uom;
	}

    @Override
    public Uom getUom() {
        if (uom == null) {
            return parent.getUom();
        } else {
            return uom;
        }
    }

    @Override
    public SymbolizerNode getParent() {
        return parent;
    }

    @Override
    public void setParent(SymbolizerNode node) {
        parent = node;
    }

    /**
     * Return the max width of the underlaying stroke
     * @param ds
     * @param fid
     * @return
     */
    public abstract double getMaxWidth(Feature feat, MapTransform mt) throws ParameterException, IOException;

    //public abstract void getStroke(DataSource ds, int fid);
    /**
     *
     * @param g2 draw within this graphics2d
     * @param shp stroke this shape (note this is note a LiteShape, because
     *        stroke can be used to delineate graphics (such as MarkGraphic,
     *        PieChart or AxisChart)
	 * @param feat the feature contains attribute
     * @throws ParameterException
     * @throws IOException
     */
    public abstract void draw(Graphics2D g2, Shape shp, Feature feat, boolean selected, MapTransform mt) throws ParameterException, IOException;

    public abstract JAXBElement<? extends StrokeType> getJAXBElement();

    protected void setJAXBProperties(StrokeType s) {
		/*
        if (postGap != null) {
            s.setPostGap(postGap.getJAXBParameterValueType());
        }
        if (preGap != null) {
            s.setPreGap(preGap.getJAXBParameterValueType());
        }*/
        if (uom != null) {
            s.setUnitOfMeasure(uom.toURN());
        }
    }

    public abstract boolean dependsOnFeature();
}