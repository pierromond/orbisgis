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
package org.orbisgis.legend.analyzer.symbolizers;

import org.orbisgis.coremap.renderer.se.PointSymbolizer;
import org.orbisgis.coremap.renderer.se.graphic.Graphic;
import org.orbisgis.coremap.renderer.se.graphic.GraphicCollection;
import org.orbisgis.coremap.renderer.se.parameter.Categorize;
import org.orbisgis.coremap.renderer.se.parameter.Recode;
import org.orbisgis.coremap.renderer.se.parameter.SeParameter;
import org.orbisgis.coremap.renderer.se.parameter.UsedAnalysis;
import org.orbisgis.coremap.renderer.se.parameter.real.Interpolate2Real;
import org.orbisgis.coremap.renderer.se.parameter.real.RealAttribute;
import org.orbisgis.coremap.renderer.se.parameter.real.RealFunction;
import org.orbisgis.coremap.renderer.se.parameter.real.RealParameter;
import org.orbisgis.legend.LegendStructure;
import org.orbisgis.legend.thematic.categorize.CategorizedPoint;
import org.orbisgis.legend.thematic.constant.UniqueSymbolPoint;
import org.orbisgis.legend.thematic.proportional.ProportionalPoint;
import org.orbisgis.legend.thematic.recode.RecodedPoint;

import java.util.List;

/**
 * This {@code Analyzer} realization is dedicated to the study of {@code
 * PointSymbolizer} instances. It's basically searching for the configurations
 * that can be found in its inner {@code Graphic} instance.
 * @author Alexis Guéganno
 */
public class PointSymbolizerAnalyzer extends SymbolizerTypeAnalyzer {

    /**
     * Build a new {@code Analyzer} from the given PointSymbolizer.
     * @param symbolizer
     */
    public PointSymbolizerAnalyzer(PointSymbolizer symbolizer){
        setLegend(analyze(symbolizer));
    }

    private LegendStructure analyze(PointSymbolizer sym) {
        //We validate the graphic
        GraphicCollection gc = sym.getGraphicCollection();
        if (gc.getNumGraphics() != 1) {
            throw new UnsupportedOperationException("We don't manage mixed graphic yet.");
        }
        Graphic g = gc.getGraphic(0);
        if (validateGraphic(g)) {
            analyzeParameters(sym);
            boolean b = isAnalysisLight() && isAnalysisUnique() && isFieldUnique();
            if(b){
                    //We know we can recognize the analysis. We just have to check
                    //there is something that is not a literal...
                    UsedAnalysis ua = getUsedAnalysis();
                    List<SeParameter> an = ua.getAnalysis();
                    if(an.isEmpty()){
                            //Unique Symbol
                            return new UniqueSymbolPoint(sym);
                    } else {
                            SeParameter p = an.get(0);
                            if(p instanceof Recode){
                                    return new RecodedPoint(sym);
                            } else if(p instanceof Categorize){
                                return new CategorizedPoint(sym);
                            } else if(p instanceof RealParameter && validateInterpolateForProportionalPoint((RealParameter) p)){
                                    //We need to analyze the ViewBox and its Interpolate instance(s)
                                    return new ProportionalPoint(sym);
                            }
                    }
            } else {
                throw new UnsupportedOperationException(getStatus());
            }
        }
        throw new UnsupportedOperationException("We can only work with MarkGraphic instances for now.");
    }

    /**
     * Checks that the given RealParameter is an instance of {@link Interpolate2Real} that can be used to build a
     * proportional point, ie that it is made on the square root of a numeric attribute.
     * @param rp
     * @return
     */
    public boolean validateInterpolateForProportionalPoint(RealParameter rp){
        if(rp instanceof Interpolate2Real){
            RealParameter look =  ((Interpolate2Real)rp).getLookupValue();
            if(look instanceof RealFunction){
                RealFunction rf = (RealFunction) look;
                List<RealParameter> ops = rf.getOperands();
                if(!ops.isEmpty() && rf.getOperator().equals(RealFunction.Operators.SQRT)
                                && ops.size() == 1 && ops.get(0) instanceof RealAttribute){
                    return true;
                }
            }
        }
        return false;
    }

}
