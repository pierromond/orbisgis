//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.0 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.02 at 05:11:18 PM CEST 
//


package org.orbisgis.editorViews.toc.actions.cui.persistence;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element ref="{}simple-symbol"/>
 *           &lt;element ref="{}symbol-collection"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "simpleSymbolOrSymbolCollection"
})
@XmlRootElement(name = "symbol-collection")
public class SymbolCollection {

    @XmlElements({
        @XmlElement(name = "simple-symbol", type = SimpleSymbol.class),
        @XmlElement(name = "symbol-collection", type = SymbolCollection.class)
    })
    protected List<Object> simpleSymbolOrSymbolCollection;

    /**
     * Gets the value of the simpleSymbolOrSymbolCollection property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the simpleSymbolOrSymbolCollection property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSimpleSymbolOrSymbolCollection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SimpleSymbol }
     * {@link SymbolCollection }
     * 
     * 
     */
    public List<Object> getSimpleSymbolOrSymbolCollection() {
        if (simpleSymbolOrSymbolCollection == null) {
            simpleSymbolOrSymbolCollection = new ArrayList<Object>();
        }
        return this.simpleSymbolOrSymbolCollection;
    }

}
