package org.orbisgis.plugins.core.geocognition;

public interface GeocognitionFilter {

	/**
	 * Return true if the element should be filtered
	 * 
	 * @param element
	 * @return
	 */
	boolean accept(GeocognitionElement element);
}