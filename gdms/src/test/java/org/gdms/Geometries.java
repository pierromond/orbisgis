/*
 * The GDMS library (Generic Datasources Management System)
 * is a middleware dedicated to the management of various kinds of
 * data-sources such as spatial vectorial data or alphanumeric. Based
 * on the JTS library and conform to the OGC simple feature access
 * specifications, it provides a complete and robust API to manipulate
 * in a SQL way remote DBMS (PostgreSQL, H2...) or flat files (.shp,
 * .csv...). GDMS is produced  by the geomatic team of the IRSTV
 * Institute <http://www.irstv.cnrs.fr/>, CNRS FR 2488:
 *    Erwan BOCHER, scientific researcher,
 *    Thomas LEDUC, scientific researcher,
 *    Fernando GONZALES CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALES CORTES, Thomas LEDUC
 *
 * This file is part of GDMS.
 *
 * GDMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GDMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GDMS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://orbisgis.cerma.archi.fr/>
 *    <http://sourcesup.cru.fr/projects/orbisgis/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-developers/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-users/>
 *
 * or contact directly:
 *    erwan.bocher _at_ ec-nantes.fr
 *    fergonco _at_ gmail.com
 *    thomas.leduc _at_ cerma.archi.fr
 */
package org.gdms;

import org.gdms.data.types.GeometryConstraint;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class Geometries {

	private static GeometryFactory gf = new GeometryFactory();

	public static LineString getLinestring() {
		return gf.createLineString(new Coordinate[] { new Coordinate(0, 0),
				new Coordinate(10, 0), new Coordinate(110, 0),
				new Coordinate(10, 240), });
	}

	public static LinearRing getLinearRing() {
		return gf.createLinearRing(new Coordinate[] { new Coordinate(0, 0),
				new Coordinate(10, 0), new Coordinate(110, 0),
				new Coordinate(10, 240), new Coordinate(0, 0) });
	}

	public static MultiLineString getMultilineString() {
		return gf.createMultiLineString(new LineString[] { getLinestring() });
	}

	public static Geometry getPolygon() {
		return gf.createPolygon(getLinearRing(), new LinearRing[0]);
	}

	public static Geometry getPoint() {
		return gf.createPoint(new Coordinate(10, 10));
	}

	public static Point getPoint3D() {
		return gf.createPoint(new Coordinate(10, 10, 50));
	}

	public static Geometry getGeometry(int geometryType) {
		switch (geometryType) {
		case GeometryConstraint.LINESTRING_2D:
			return getLinestring();
		case GeometryConstraint.MULTI_LINESTRING_2D:
			return getMultilineString();
		case GeometryConstraint.POINT_2D:
			return getPoint();
		case GeometryConstraint.POINT_3D:
			return getPoint3D();
		case GeometryConstraint.POLYGON_2D:
			return getPolygon();
		case GeometryConstraint.MULTI_POLYGON_2D:
			return getMultiPolygon2D();
		case GeometryConstraint.MIXED:
			return getPoint();
		default:
			throw new RuntimeException("To implement");
		}
	}

	public static LineString getLineString3D() {
		return gf.createLineString(new Coordinate[] { new Coordinate(0, 0, 0),
				new Coordinate(10, 2, 5), new Coordinate(110, 0, 4),
				new Coordinate(10, 240, 10), });
	}

	public static Polygon getPolygon3D() {
		return gf.createPolygon(getLinearRing3D(), new LinearRing[0]);
	}

	private static LinearRing getLinearRing3D() {
		return gf.createLinearRing(new Coordinate[] { new Coordinate(0, 2, 0),
				new Coordinate(10, 2, 0), new Coordinate(110, 22, 0),
				new Coordinate(10, 62, 240), new Coordinate(0, 2, 0) });
	}

	public static Geometry getMultiPoint3D() {
		Point[] points = new Point[2];
		points[0] = getPoint3D();
		points[1] = gf.createPoint(new Coordinate(23, 325, 74));
		return gf.createMultiPoint(points);
	}

	public static Geometry getMultilineString3D() {
		return gf.createMultiLineString(new LineString[] { getLineString3D(),
				getLineString3D() });
	}

	public static Geometry getMultiPolygon3D() {
		return gf.createMultiPolygon(new Polygon[] { getPolygon3D(),
				getPolygon3D() });
	}

	public static Geometry getMultiPolygon2D() {
		return gf.createMultiPolygon(new Polygon[] { (Polygon) getPolygon() });
	}

}
