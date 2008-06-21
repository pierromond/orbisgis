/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able
 * to manipulate and create vector and raster spatial information. OrbisGIS
 * is distributed under GPL 3 license. It is produced  by the geo-informatic team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/>, CNRS FR 2488:
 *    Erwan BOCHER, scientific researcher,
 *    Thomas LEDUC, scientific researcher,
 *    Fernando GONZALEZ CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OrbisGIS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://orbisgis.cerma.archi.fr/>
 *    <http://sourcesup.cru.fr/projects/orbisgis/>
 *
 * or contact directly:
 *    erwan.bocher _at_ ec-nantes.fr
 *    fergonco _at_ gmail.com
 *    thomas.leduc _at_ cerma.archi.fr
 */
package org.orbisgis.editorViews.toc.actions.cui.gui.widgets.table;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import org.gdms.data.types.GeometryConstraint;
import org.gdms.driver.DriverException;
import org.orbisgis.renderer.RenderPermission;
import org.orbisgis.renderer.legend.CircleSymbol;
import org.orbisgis.renderer.legend.LineSymbol;
import org.orbisgis.renderer.legend.PolygonSymbol;
import org.orbisgis.renderer.legend.Symbol;
import org.orbisgis.renderer.legend.SymbolComposite;
import org.orbisgis.renderer.legend.SymbolFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

/**
 * Is the canvas object that we will put in the tables. You can set a symbol to it
 * and it will show you drawed.
 * @author david
 *
 */
public class ButtonCanvas extends JPanel {

	Symbol s;
	Integer constraint;
	boolean isSelected = false;

	public ButtonCanvas() {
		super();
		s = SymbolFactory.createNullSymbol();
		constraint = null;
		this.setSize(150, 25);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(1, 1, 149, 24);

		GeometryFactory gf = new GeometryFactory();
		Geometry geom = null;
		// constraint=getConstraint(s);

		Integer constr = getConstraint(s);

		try {
			Stroke st = new BasicStroke();
			if (isSelected) {
				g.setColor(Color.BLUE);
				st = ((Graphics2D) g).getStroke();
				((Graphics2D) g).setStroke(new BasicStroke(new Float(2.0)));
			} else {
				g.setColor(Color.GRAY);
			}
			// g.drawRect(1, 1, 149, 14); //Painting a Rectangle for the
			// presentation and selection

			((Graphics2D) g).setStroke(st);

			if (constr == null) {
				SymbolComposite comp = (SymbolComposite) s;
				Symbol sym;
				int numberOfSymbols = comp.getSymbolCount();
				for (int i = 0; i < numberOfSymbols; i++) {
					sym = comp.getSymbol(i);
					if (sym instanceof LineSymbol) {
						geom = gf
								.createLineString(new Coordinate[] {
										new Coordinate(28, 12),
										new Coordinate(126, 12) });

						sym.draw((Graphics2D) g, geom, new AffineTransform(),
								new RenderPermission() {

									public boolean canDraw(Envelope env) {
										return true;
									}

								});
					}

					if (sym instanceof CircleSymbol) {
						geom = gf.createPoint(new Coordinate(75, 12));

						sym.draw((Graphics2D) g, geom, new AffineTransform(),
								new RenderPermission() {

									public boolean canDraw(Envelope env) {
										return true;
									}

								});
					}

					if (sym instanceof PolygonSymbol) {
						Coordinate[] coordsP = { new Coordinate(21, 6),
								new Coordinate(129, 6),
								new Coordinate(129, 20),
								new Coordinate(21, 20), new Coordinate(21, 6) };
						CoordinateArraySequence seqP = new CoordinateArraySequence(
								coordsP);
						geom = gf.createPolygon(new LinearRing(seqP, gf), null);

						sym.draw((Graphics2D) g, geom, new AffineTransform(),
								new RenderPermission() {

									public boolean canDraw(Envelope env) {
										return true;
									}

								});
					}

				}
			} else {
				switch (constr) {
				case GeometryConstraint.LINESTRING:
				case GeometryConstraint.MULTI_LINESTRING:
					geom = gf.createLineString(new Coordinate[] {
							new Coordinate(28, 12), new Coordinate(126, 12) });

					s.draw((Graphics2D) g, geom, new AffineTransform(),
							new RenderPermission() {

								public boolean canDraw(Envelope env) {
									return true;
								}

							});

					break;
				case GeometryConstraint.POINT:
				case GeometryConstraint.MULTI_POINT:
					geom = gf.createPoint(new Coordinate(75, 12));

					s.draw((Graphics2D) g, geom, new AffineTransform(),
							new RenderPermission() {

								public boolean canDraw(Envelope env) {
									return true;
								}

							});

					break;
				case GeometryConstraint.POLYGON:
				case GeometryConstraint.MULTI_POLYGON:
					Coordinate[] coords = { new Coordinate(21, 6),
							new Coordinate(129, 6), new Coordinate(129, 20),
							new Coordinate(21, 20), new Coordinate(21, 6) };
					CoordinateArraySequence seq = new CoordinateArraySequence(
							coords);
					geom = gf.createPolygon(new LinearRing(seq, gf), null);

					s.draw((Graphics2D) g, geom, new AffineTransform(),
							new RenderPermission() {

								public boolean canDraw(Envelope env) {
									return true;
								}

							});

					break;

				}
			}

		} catch (DriverException e) {
			((Graphics2D) g).drawString("Cannot generate preview", 0, 0);
		} catch (NullPointerException e) {
			((Graphics2D) g).drawString("Cannot generate preview: ", 0, 0);
			System.out.println(e.getMessage());
		}
	}

	public void setLegend(Symbol sym, Integer constraint) {
		this.s = sym;
		this.constraint = constraint;
	}

	public Integer getConstraint(Symbol sym) {
		if (sym instanceof LineSymbol) {
			return GeometryConstraint.LINESTRING;
		}
		if (sym instanceof CircleSymbol) {
			return GeometryConstraint.POINT;
		}
		if (sym instanceof PolygonSymbol) {
			return GeometryConstraint.POLYGON;
		}
		return null;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public Symbol getSymbol() {
		return s;
	}
}