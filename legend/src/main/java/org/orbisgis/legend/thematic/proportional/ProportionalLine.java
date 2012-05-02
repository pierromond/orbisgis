/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.legend.thematic.proportional;

import org.orbisgis.core.renderer.se.LineSymbolizer;
import org.orbisgis.core.renderer.se.parameter.ParameterException;
import org.orbisgis.core.renderer.se.stroke.PenStroke;
import org.orbisgis.core.renderer.se.stroke.Stroke;
import org.orbisgis.legend.LegendStructure;
import org.orbisgis.legend.analyzer.PenStrokeAnalyzer;
import org.orbisgis.legend.structure.stroke.ProportionalStrokeLegend;
import org.orbisgis.legend.thematic.ConstantColorAndDashesLine;

/**
 * A {@code ProportionalLine} represents a {@code LineSymbolizer} containing a
 * {@code PenStroke} whose only varying parameter is the width of the line.
 * This width is defined thanks to a linear interpolation made directly on the
 * raw value (i.e. we don't apply any mathematical function to the input values).
 * @author alexis
 */
public class ProportionalLine extends ConstantColorAndDashesLine {

    private ProportionalStrokeLegend strokeLegend;

    /**
     * Tries to build a new {@code ProportionalLine} from the given {@code
     * LineSymbolizer}. If a {@code ProportionalLine} can't be built, an {@code
     * IllegalArgumentException} is thrown.
     * @param symbolizer
     * @code IllegalArgumentException
     */
    public ProportionalLine(LineSymbolizer symbolizer) {
        super(symbolizer);
        Stroke gr = ((LineSymbolizer)getSymbolizer()).getStroke();
        if(gr instanceof PenStroke){
            LegendStructure mgl = new PenStrokeAnalyzer((PenStroke) gr).getLegend();
            if(mgl instanceof ProportionalStrokeLegend){
                strokeLegend = (ProportionalStrokeLegend) mgl;
            }  else {
                throw new IllegalArgumentException("A unique symbol must be a  "
                        + "constant.");
            }
        }
    }

    /**
     * Build a new {@code ProportionalLine} instance from the given symbolizer
     * and legend. As the inner analysis is given directly with the symbolizer,
     * we won't check they match. It is up to the caller to check they do.
     * @param symbolizer
     * @param legend
     */
    public ProportionalLine(LineSymbolizer symbolizer, ProportionalStrokeLegend legend) {
        super(symbolizer);
        strokeLegend = legend;
    }

    @Override
    public LegendStructure getStrokeLegend() {
        return strokeLegend;
    }

    /**
     * Get the data of the first interpolation point
     * @return
     */
    public double getFirstData() {
        return strokeLegend.getFirstData();
    }

    /**
     * Get the data of the second interpolation point
     * @return
     */
    public double getSecondData() {
        return strokeLegend.getSecondData();
    }

    /**
     * Set the data of the first interpolation point
     * @param d
     */
    public void setFirstData(double d) {
        strokeLegend.setFirstData(d);
    }

    /**
     * Set the data of the second interpolation point
     * @param d
     */
    public void setSecondData(double d) {
        strokeLegend.setSecondData(d);
    }

    /**
     * Get the value of the first interpolation point, as a {@code double}. The
     * interpolation value is supposed to be a {@code RealLiteral} instance. If
     * it is not, an exception should have been thrown at initialization.
     * @return
     */
    public double getFirstValue() throws ParameterException {
        return strokeLegend.getFirstValue();
    }

    /**
     * Set the value of the first interpolation point, as a {@code double}.
     * @param d
     */
    public void setFirstValue(double d) {
        strokeLegend.setFirstValue(d);
    }

    /**
     * Get the value of the second interpolation point, as a {@code double}. The
     * interpolation value is supposed to be a {@code RealLiteral} instance. If
     * it is not, an exception should have been thrown at initialization.
     * @return
     */
    public double getSecondValue() throws ParameterException {
        return strokeLegend.getSecondValue();
    }

    /**
     * Set the value of the second interpolation point, as a {@code double}.
     * @param d
     */
    public void setSecondValue(double d) {
        strokeLegend.setSecondValue(d);
    }

    @Override
    public String getLegendTypeName() {
        return "Proportional Line";
    }

}