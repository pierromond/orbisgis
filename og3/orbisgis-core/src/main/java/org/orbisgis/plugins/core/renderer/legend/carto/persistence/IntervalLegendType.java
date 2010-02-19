//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.04.14 at 03:50:39 PM CEST 
//

package org.orbisgis.plugins.core.renderer.legend.carto.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for interval-legend-type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;interval-legend-type&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{org.orbisgis.legend}classified-legend-type&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref=&quot;{org.orbisgis.legend}interval-classification&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "interval-legend-type", propOrder = { "intervalClassification" })
public class IntervalLegendType extends ClassifiedLegendType {

	@XmlElement(name = "interval-classification", namespace = "org.orbisgis.legend")
	protected List<IntervalClassification> intervalClassification;

	/**
	 * Gets the value of the intervalClassification property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the intervalClassification property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getIntervalClassification().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link IntervalClassification }
	 * 
	 * 
	 */
	public List<IntervalClassification> getIntervalClassification() {
		if (intervalClassification == null) {
			intervalClassification = new ArrayList<IntervalClassification>();
		}
		return this.intervalClassification;
	}

}