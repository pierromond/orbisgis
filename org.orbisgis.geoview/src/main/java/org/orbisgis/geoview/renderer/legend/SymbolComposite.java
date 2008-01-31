package org.orbisgis.geoview.renderer.legend;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.gdms.driver.DriverException;
import org.orbisgis.geoview.renderer.RenderPermission;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

public class SymbolComposite implements Symbol {

	private Symbol[] symbols;

	public SymbolComposite(Symbol[] symbols) {
		this.symbols = symbols;
	}

	public Envelope draw(Graphics2D g, Geometry geom, AffineTransform at,
			RenderPermission permission) throws DriverException {
		Envelope ret = null;
		for (Symbol symbol : symbols) {
			Envelope area = symbol.draw(g, geom, at, permission);
			if (ret == null) {
				ret = area;
			} else {
				ret.expandToInclude(area);
			}
		}

		return ret;
	}

	public boolean willDraw(Geometry geom) {
		for (Symbol symbol : symbols) {
			if (symbol.willDraw(geom)) {
				return true;
			}
		}
		return false;
	}

	public int getSymbolCount() {
		return symbols.length;
	}

	public Symbol getSymbol(int i) {
		return symbols[i];
	}

}
