//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.09.02 at 10:59:16 AM CEST 
//


package net.opengis.se._2_0.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FormatNumberType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FormatNumberType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/se/2.0/core}FunctionType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/se/2.0/core}NumericValue"/>
 *         &lt;element ref="{http://www.opengis.net/se/2.0/core}Pattern"/>
 *         &lt;element ref="{http://www.opengis.net/se/2.0/core}NegativePattern" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="decimalPoint" type="{http://www.w3.org/2001/XMLSchema}string" default="." />
 *       &lt;attribute name="groupingSeparator" type="{http://www.w3.org/2001/XMLSchema}string" default="," />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FormatNumberType", propOrder = {
    "numericValue",
    "pattern",
    "negativePattern"
})
public class FormatNumberType
    extends FunctionType
{

    @XmlElement(name = "NumericValue", required = true)
    protected ParameterValueType numericValue;
    @XmlElement(name = "Pattern", required = true)
    protected String pattern;
    @XmlElement(name = "NegativePattern")
    protected String negativePattern;
    @XmlAttribute
    protected String decimalPoint;
    @XmlAttribute
    protected String groupingSeparator;

    /**
     * Gets the value of the numericValue property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterValueType }
     *     
     */
    public ParameterValueType getNumericValue() {
        return numericValue;
    }

    /**
     * Sets the value of the numericValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterValueType }
     *     
     */
    public void setNumericValue(ParameterValueType value) {
        this.numericValue = value;
    }

    /**
     * Gets the value of the pattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Sets the value of the pattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPattern(String value) {
        this.pattern = value;
    }

    /**
     * Gets the value of the negativePattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNegativePattern() {
        return negativePattern;
    }

    /**
     * Sets the value of the negativePattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNegativePattern(String value) {
        this.negativePattern = value;
    }

    /**
     * Gets the value of the decimalPoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDecimalPoint() {
        if (decimalPoint == null) {
            return ".";
        } else {
            return decimalPoint;
        }
    }

    /**
     * Sets the value of the decimalPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDecimalPoint(String value) {
        this.decimalPoint = value;
    }

    /**
     * Gets the value of the groupingSeparator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupingSeparator() {
        if (groupingSeparator == null) {
            return ",";
        } else {
            return groupingSeparator;
        }
    }

    /**
     * Sets the value of the groupingSeparator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupingSeparator(String value) {
        this.groupingSeparator = value;
    }

}
