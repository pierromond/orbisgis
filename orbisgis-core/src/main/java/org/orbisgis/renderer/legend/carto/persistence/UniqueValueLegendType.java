//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-600 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.07 at 05:35:03 PM CEST 
//


package org.orbisgis.renderer.legend.carto.persistence;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for unique-value-legend-type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="unique-value-legend-type">
 *   &lt;complexContent>
 *     &lt;extension base="{org.orbisgis.legend}classified-legend-type">
 *       &lt;sequence>
 *         &lt;element ref="{org.orbisgis.legend}value-classification" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "unique-value-legend-type", propOrder = {
    "valueClassification"
})
public class UniqueValueLegendType
    extends ClassifiedLegendType
{

    @XmlElement(name = "value-classification", namespace = "org.orbisgis.legend")
    protected List<ValueClassification> valueClassification;

    /**
     * Gets the value of the valueClassification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the valueClassification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValueClassification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValueClassification }
     * 
     * 
     */
    public List<ValueClassification> getValueClassification() {
        if (valueClassification == null) {
            valueClassification = new ArrayList<ValueClassification>();
        }
        return this.valueClassification;
    }

}
