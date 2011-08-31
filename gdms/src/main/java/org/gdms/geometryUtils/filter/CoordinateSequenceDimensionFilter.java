package org.gdms.geometryUtils.filter;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFilter;

/**
 *
 * @author ebocher
 */
public class CoordinateSequenceDimensionFilter implements CoordinateSequenceFilter {

        private boolean isDone = false;
        int dimension = 0;
        int lastDimen = 0;
        public static int XY = 2;
        public static int XYZ = 3;
        public static int XYZM = 4;
        public int MAXDim = XYZM;

        @Override
        public void filter(CoordinateSequence seq, int i) {
                double firstZ = seq.getOrdinate(i, CoordinateSequence.Z);
                if (!Double.isNaN(firstZ)) {
                        double firstM = seq.getOrdinate(i, CoordinateSequence.M);
                        if (!Double.isNaN(firstM)) {
                                dimension = XYZM;
                        } else {
                                dimension = XYZ;
                        }
                } else {
                        dimension = XY;
                }
                if (dimension > lastDimen){
                        lastDimen = dimension;
                }
                if (i == seq.size() || lastDimen >= MAXDim) {
                        isDone = true;
                }
        }

        public int getDimension() {
                return lastDimen;
        }

        public void setMAXDim(int MAXDim) {
                this.MAXDim = MAXDim;
        }

        @Override
        public boolean isDone() {
                return isDone;
        }

        @Override
        public boolean isGeometryChanged() {
                return false;
        }
}
