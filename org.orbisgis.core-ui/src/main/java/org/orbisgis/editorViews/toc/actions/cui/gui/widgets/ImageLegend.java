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
package org.orbisgis.editorViews.toc.actions.cui.gui.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.gdms.data.types.GeometryConstraint;
import org.gdms.driver.DriverException;
import org.orbisgis.renderer.RenderPermission;
import org.orbisgis.renderer.legend.Legend;
import org.orbisgis.renderer.legend.carto.Interval;
import org.orbisgis.renderer.legend.carto.IntervalLegend;
import org.orbisgis.renderer.legend.carto.LabelLegend;
import org.orbisgis.renderer.legend.carto.ProportionalLegend;
import org.orbisgis.renderer.legend.carto.UniqueSymbolLegend;
import org.orbisgis.renderer.legend.carto.UniqueValueLegend;
import org.orbisgis.renderer.symbol.CircleSymbol;
import org.orbisgis.renderer.symbol.LineSymbol;
import org.orbisgis.renderer.symbol.PolygonSymbol;
import org.orbisgis.renderer.symbol.Symbol;
import org.orbisgis.renderer.symbol.SymbolFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

public class ImageLegend {
	private BufferedImage[] ims;

	public ImageLegend(Legend[] leg) {
		ims = new BufferedImage[leg.length];
		createImage(leg);
	}

	/**
	 * The first method to call. Creates one image for each legend in the array
	 *
	 * @param leg
	 */
	private void createImage(Legend[] leg) {

		BufferedImage imageGarbage = new BufferedImage(10, 10,
				BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < leg.length; i++) {
			Dimension dimFinal = getDimension(leg[i], imageGarbage
					.getGraphics());

			BufferedImage im = new BufferedImage(dimFinal.width,
					dimFinal.height, BufferedImage.TYPE_INT_ARGB);

			paintImage(leg[i], 0, im);

			ims[i] = im;

		}

	}

	/**
	 * paint the image of the legend starting from the selected point in te im
	 * object
	 *
	 * @param leg
	 *            the legend to paint
	 * @param end
	 *            the point from start to paint (the end of the previous image)
	 * @param im
	 *            the buffered image to paint in.
	 * @return int
	 */
	private int paintImage(Legend leg, int end, BufferedImage im) {
		Graphics g = im.getGraphics();
		Graphics2D g2 = null;
		if (g instanceof Graphics2D) {
			g2 = (Graphics2D) g;
		} else {
			return -1;
		}

		if (leg instanceof UniqueSymbolLegend) {
			UniqueSymbolLegend usl = (UniqueSymbolLegend) leg;

			paintSymbol(usl.getSymbol(), end, g);
			setText(usl.getSymbol().getName(), end, g);

			end += 30;
		}

		if (leg instanceof UniqueValueLegend) {
			UniqueValueLegend uvl = (UniqueValueLegend) leg;
			int numberOfClas = uvl.getClassificationCount();
			for (int i = 0; i < numberOfClas; i++) {
				paintSymbol(uvl.getSymbol(i), end, g);
				setText(uvl.getLabel(i), end, g);
				end += 30;
			}
			if (!(uvl.getDefaultSymbol() == null)) {
				paintSymbol(uvl.getDefaultSymbol(), end, g);
				setText("Default", end, g);
				end += 30;
			}

		}

		if (leg instanceof IntervalLegend) {
			IntervalLegend il = (IntervalLegend) leg;
			int numberOfInterv = il.getIntervals().size();
			ArrayList<Interval> inters = il.getIntervals();
			for (int i = 0; i < numberOfInterv; i++) {
				paintSymbol(il.getSymbol(inters.get(i)), end, g);
				setText(il.getSymbol(inters.get(i)).getName(), end, g);
				end += 30;
			}
			if (!(il.getDefaultSymbol() == null)) {
				paintSymbol(il.getDefaultSymbol(), end, g);
				setText("Default", end, g);
				end += 30;
			}
		}

		if (leg instanceof ProportionalLegend) {
			ProportionalLegend pl = (ProportionalLegend) leg;
			paintProportionalLegend(pl.getFillColor(), pl.getOutlineColor(),
					end, g);
			setText("Proportional", end, g);
			end += 30;
		}

		if (leg instanceof LabelLegend) {
			g2.setColor(Color.BLUE);
			g2.drawLine(5, end + 1, 45, end + 1);
			end += 30;
		}

		return end;
	}

	/**
	 * If the legend is proportional the draw is not equals than the others.
	 * then we'll call to this function.
	 *
	 * @param fillColor
	 *            the fill color
	 * @param outline
	 *            the outline color
	 * @param end
	 *            the point to start painting (the end of the previous draw)
	 * @param g
	 *            the graphics object on where we will paint.
	 */
	private void paintProportionalLegend(Color fillColor, Color outline,
			int end, Graphics g) {
		Symbol s1 = SymbolFactory.createCirclePointSymbol(outline, fillColor,
				28);
		Symbol s2 = SymbolFactory.createCirclePointSymbol(outline, fillColor,
				10);
		GeometryFactory gf = new GeometryFactory();
		Geometry geom = null;
		Geometry geom2 = null;

		try {
			geom = gf.createPoint(new Coordinate(15, end + 15));

			s1.draw((Graphics2D) g, geom, new AffineTransform(),
					new RenderPermission() {

						public boolean canDraw(Envelope env) {
							return true;
						}

					});

			geom2 = gf.createPoint(new Coordinate(15, end + 24));

			s2.draw((Graphics2D) g, geom2, new AffineTransform(),
					new RenderPermission() {

						public boolean canDraw(Envelope env) {
							return true;
						}

					});
		} catch (DriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * draws the specified text on the right of a symbol.
	 *
	 * @param name
	 * @param end
	 * @param g
	 */
	private void setText(String name, int end, Graphics g) {
		((Graphics2D) g).setColor(Color.black);
		((Graphics2D) g).drawString(name, 55, end + 17);
	}

	/**
	 *
	 * @param sym
	 * @return
	 */
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
		if (sym.acceptsChildren()) {
			return null;
		}
		return null;
	}

	/**
	 * Paints a symbol in the specified graphics and starting from the previous
	 * "end" position.
	 *
	 * @param s
	 * @param end
	 * @param g
	 */
	private void paintSymbol(Symbol s, int end, Graphics g) {
		Integer constr = getConstraint(s);

		try {
			GeometryFactory gf = new GeometryFactory();
			Geometry geom = null;

			if (constr == null) {
				Symbol sym;
				int numberOfSymbols = s.getSymbolCount();
				for (int i = 0; i < numberOfSymbols; i++) {
					sym = s.getSymbol(i);
					if (sym instanceof LineSymbol) {
						geom = gf.createLineString(new Coordinate[] {
								new Coordinate(5, end + 15),
								new Coordinate(45, end + 15) });

						sym.draw((Graphics2D) g, geom, new AffineTransform(),
								new RenderPermission() {

									public boolean canDraw(Envelope env) {
										return true;
									}

								});
					}

					if (sym instanceof CircleSymbol) {
						geom = gf.createPoint(new Coordinate(25, end + 15));

						sym.draw((Graphics2D) g, geom, new AffineTransform(),
								new RenderPermission() {

									public boolean canDraw(Envelope env) {
										return true;
									}

								});
					}

					if (sym instanceof PolygonSymbol) {
						Coordinate[] coordsP = { new Coordinate(5, end + 2),
								new Coordinate(45, end + 2),
								new Coordinate(45, end + 28),
								new Coordinate(5, end + 28),
								new Coordinate(5, end + 2) };
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
							new Coordinate(5, end + 15),
							new Coordinate(45, end + 15) });

					s.draw((Graphics2D) g, geom, new AffineTransform(),
							new RenderPermission() {

								public boolean canDraw(Envelope env) {
									return true;
								}

							});

					break;
				case GeometryConstraint.POINT:
				case GeometryConstraint.MULTI_POINT:
					geom = gf.createPoint(new Coordinate(25, end + 15));

					s.draw((Graphics2D) g, geom, new AffineTransform(),
							new RenderPermission() {

								public boolean canDraw(Envelope env) {
									return true;
								}

							});

					break;
				case GeometryConstraint.POLYGON:
				case GeometryConstraint.MULTI_POLYGON:
					Coordinate[] coords = { new Coordinate(5, end + 2),
							new Coordinate(45, end + 2),
							new Coordinate(45, end + 28),
							new Coordinate(5, end + 28),
							new Coordinate(5, end + 2) };
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

	/**
	 * gets the future dimension of the canvas on where to paint.
	 *
	 * @param leg
	 *            The legend for wich we want know the dimension
	 * @param dg
	 *            The graphics on where we will paint the legend.
	 * @return Dimension
	 */

	private Dimension getDimension(Legend leg, Graphics dg) {
		int height = 0;
		int initWidth = 60;
		int width = 0;

		if (leg instanceof UniqueSymbolLegend) {
			height = 30;
			String str = ((UniqueSymbolLegend) leg).getSymbol().getName();
			FontMetrics fm = dg.getFontMetrics();
			width = initWidth + fm.stringWidth(str);
		}

		if (leg instanceof UniqueValueLegend) {
			UniqueValueLegend uvl = (UniqueValueLegend) leg;
			int numberOfClas = uvl.getClassificationCount();

			height = 30 * numberOfClas;

			for (int i = 0; i < numberOfClas; i++) {
				String str = uvl.getLabel(i);
				FontMetrics fm = dg.getFontMetrics();
				int widthStr = fm.stringWidth(str);

				if (initWidth + widthStr > width) {
					width = initWidth + widthStr;
				}

			}

			if (!(uvl.getDefaultSymbol() == null)) {
				height += 30;

				String str = "Default";
				FontMetrics fm = dg.getFontMetrics();
				int widthStr = fm.stringWidth(str);

				if (initWidth + widthStr > width) {
					width = initWidth + widthStr;
				}
			}

		}

		if (leg instanceof IntervalLegend) {
			IntervalLegend il = (IntervalLegend) leg;
			int numberOfInterv = il.getIntervals().size();

			height = 30 * numberOfInterv;

			ArrayList<Interval> inters = il.getIntervals();
			for (int i = 0; i < numberOfInterv; i++) {
				String str = il.getSymbol(inters.get(i)).getName();
				FontMetrics fm = dg.getFontMetrics();
				int widthStr = fm.stringWidth(str);

				if (initWidth + widthStr > width) {
					width = initWidth + widthStr;
				}
			}

			if (!(il.getDefaultSymbol() == null)) {
				height += 30;

				String str = "Default";
				FontMetrics fm = dg.getFontMetrics();
				int widthStr = fm.stringWidth(str);

				if (initWidth + widthStr > width) {
					width = initWidth + widthStr;
				}
			}
		}

		if (leg instanceof ProportionalLegend) {
			String str = "Default";
			FontMetrics fm = dg.getFontMetrics();
			width = initWidth + fm.stringWidth(str);
			height = 30;
		}

		if (leg instanceof LabelLegend) {
			height = 30;
		}

		if (width == 0)
			width = 1;
		if (height == 0)
			height = 1;

		return new Dimension(width, height);
	}

	/**
	 * returns the collection of images of the legends
	 *
	 * @return BufferedImage[]
	 */
	public BufferedImage[] getIm() {
		return ims;
	}

	public void setLeg(Legend[] leg) {
		createImage(leg);
	}

}
