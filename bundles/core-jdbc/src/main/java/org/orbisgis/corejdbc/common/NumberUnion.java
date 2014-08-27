package org.orbisgis.corejdbc.common;

import java.io.Serializable;
import java.util.List;
import java.util.SortedSet;

/**
 * @author Nicolas Fortin
 */
public interface NumberUnion<T extends Number> extends SortedSet<T>, Serializable {

    /**
     * Return the internal container
     *
     * @return intervals ex: 0,0,50,60 for [0] and [50-60]
     */
    List<T> getValueRanges();
}
