package org.orbisgis.geoview.renderer.style.sld;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.orbisgis.pluginManager.VTD;

public class LineSymbolizer implements Symbolizer {

	/** to be complete
	 * 
	 *
	 */
	
	private VTD vtd;
	private String rootXpathQuery;
	
	private String type= "sld:LineSymbolizer";

	/** SLD tags
	 * 
	 * <sld:LineSymbolizer>
                        <sld:Stroke>
                            <sld:CssParameter name="stroke">
                                <ogc:Literal>#9ECAE1</ogc:Literal>
                            </sld:CssParameter>
                            <sld:CssParameter name="stroke-linecap">
                                <ogc:Literal>butt</ogc:Literal>
                            </sld:CssParameter>
                            <sld:CssParameter name="stroke-linejoin">
                                <ogc:Literal>miter</ogc:Literal>
                            </sld:CssParameter>
                            <sld:CssParameter name="stroke-opacity">
                                <ogc:Literal>1.0</ogc:Literal>
                            </sld:CssParameter>
                            <sld:CssParameter name="stroke-width">
                                <ogc:Literal>1.0</ogc:Literal>
                            </sld:CssParameter>
                            <sld:CssParameter name="stroke-dashoffset">
                                <ogc:Literal>0.0</ogc:Literal>
                            </sld:CssParameter>
                        </sld:Stroke>
                    </sld:LineSymbolizer>
	 * 
	 */
	
	public LineSymbolizer(VTD vtd, String rootXpathQuery){
		
		this.vtd = vtd;
		this.rootXpathQuery = rootXpathQuery;
		
		
				
	}
	
	
	public Stroke getStroke(){
		return new Stroke(vtd, rootXpathQuery
				+ "/sld:Stroke");
		
	}


	public String getType() {
		return type;
	}

	

}
