//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.09.02 at 10:59:16 AM CEST 
//


package net.opengis.se._2_0.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PointLabelType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PointLabelType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/se/2.0/core}LabelType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/se/2.0/core}Rotation" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/se/2.0/core}ExclusionZone" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PointLabelType", propOrder = {
    "rotation",
    "exclusionZone"
})
public class PointLabelType
    extends LabelType
{

    @XmlElement(name = "Rotation")
    protected ParameterValueType rotation;
    @XmlElementRef(name = "ExclusionZone", namespace = "http://www.opengis.net/se/2.0/core", type = JAXBElement.class)
    protected JAXBElement<? extends ExclusionZoneType> exclusionZone;

    /**
     * Gets the value of the rotation property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterValueType }
     *     
     */
    public ParameterValueType getRotation() {
        return rotation;
    }

    /**
     * Sets the value of the rotation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterValueType }
     *     
     */
    public void setRotation(ParameterValueType value) {
        this.rotation = value;
    }

    /**
     * Gets the value of the exclusionZone property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ExclusionRadiusType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExclusionRectangleType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExclusionZoneType }{@code >}
     *     
     */
    public JAXBElement<? extends ExclusionZoneType> getExclusionZone() {
        return exclusionZone;
    }

    /**
     * Sets the value of the exclusionZone property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ExclusionRadiusType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExclusionRectangleType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExclusionZoneType }{@code >}
     *     
     */
    public void setExclusionZone(JAXBElement<? extends ExclusionZoneType> value) {
        this.exclusionZone = ((JAXBElement<? extends ExclusionZoneType> ) value);
    }

}
