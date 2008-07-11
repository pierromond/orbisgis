package org.orbisgis.editorViews.toc.actions.cui.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.gdms.data.types.GeometryConstraint;
import org.gdms.data.types.TypeFactory;
import org.gdms.data.values.Value;
import org.gdms.data.values.ValueFactory;
import org.gdms.driver.DriverException;
import org.orbisgis.Services;
import org.orbisgis.editorViews.toc.actions.cui.components.widgets.ColorPicker;
import org.orbisgis.editorViews.toc.actions.cui.gui.table.IntervalLegendTableModel;
import org.orbisgis.layerModel.ILayer;
import org.orbisgis.renderer.classification.RangeMethod;
import org.orbisgis.renderer.legend.Legend;
import org.orbisgis.renderer.legend.carto.Interval;
import org.orbisgis.renderer.legend.carto.IntervalLegend;
import org.orbisgis.renderer.legend.carto.LegendFactory;
import org.orbisgis.renderer.symbol.Symbol;
import org.orbisgis.renderer.symbol.SymbolFactory;
import org.sif.CRFlowLayout;
import org.sif.CarriageReturn;
import org.sif.UIFactory;

public class PnlIntervalLegend extends PnlAbstractClassifiedLegend {

	private javax.swing.JComboBox cmbIntervalType;
	private javax.swing.JComboBox cmbIntervalCount;
	private JLabel lblFirstColor;
	private JLabel lblSecondColor;
	private javax.swing.JComboBox cmbFieldNames;

	private IntervalLegend legend;

	public PnlIntervalLegend(LegendContext legendContext) {
		super(legendContext, new IntervalLegendTableModel(), LegendFactory
				.createIntervalLegend());
	}

	@Override
	protected JPanel getTopPanel() {
		cmbFieldNames = new javax.swing.JComboBox();
		JPanel pnl = new JPanel();
		CRFlowLayout flowLayout = new CRFlowLayout();
		flowLayout.setAlignment(CRFlowLayout.LEFT);
		pnl.setLayout(flowLayout);
		pnl.add(new JLabel("Classification field:"));

		cmbFieldNames.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					legend.setClassificationField((String) cmbFieldNames
							.getSelectedItem(), legendContext.getLayer()
							.getDataSource());
				} catch (DriverException e1) {
					Services.getErrorManager().error(
							"Cannot access the type of the field", e1);
				}
			}

		});
		pnl.add(cmbFieldNames);

		pnl.add(new JLabel("Type of interval: "));
		cmbIntervalCount = new javax.swing.JComboBox();
		cmbIntervalType = new javax.swing.JComboBox();
		cmbIntervalType.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				intervalTypeChanged(evt);
			}
		});
		cmbIntervalType.setModel(new DefaultComboBoxModel(new Object[] {
				"Quantiles", "Equivalences", "Moyennes", "Standard" }));
		cmbIntervalType.setSelectedIndex(0);
		pnl.add(cmbIntervalType);

		pnl.add(new CarriageReturn());

		pnl.add(new JLabel("Number of intervals:"));
		pnl.add(cmbIntervalCount);

		pnl.add(new JLabel("First color:"));
		lblFirstColor = new JLabel();
		lblFirstColor.setOpaque(true);
		lblFirstColor.setMaximumSize(new java.awt.Dimension(19, 19));
		lblFirstColor.setMinimumSize(new java.awt.Dimension(19, 19));
		lblFirstColor.setPreferredSize(new java.awt.Dimension(19, 19));
		lblFirstColor.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				jButtonFirstColorActionPerformed();
			}
		});
		lblFirstColor.setBackground(Color.BLUE);
		pnl.add(lblFirstColor);

		pnl.add(new JLabel("Final color:"));
		lblSecondColor = new JLabel();
		lblSecondColor.setOpaque(true);
		lblSecondColor.setMaximumSize(new java.awt.Dimension(19, 19));
		lblSecondColor.setMinimumSize(new java.awt.Dimension(19, 19));
		lblSecondColor.setPreferredSize(new java.awt.Dimension(19, 19));
		lblSecondColor.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				jButtonSecondColorActionPerformed();
			}
		});
		lblSecondColor.setBackground(Color.RED);
		pnl.add(lblSecondColor);

		return pnl;
	}

	private void jButtonFirstColorActionPerformed() {
		ColorPicker picker = new ColorPicker();
		if (UIFactory.showDialog(picker)) {
			Color color = picker.getColor();
			lblFirstColor.setBackground(color);
		}
	}

	private void jButtonSecondColorActionPerformed() {
		ColorPicker picker = new ColorPicker();
		if (UIFactory.showDialog(picker)) {
			Color color = picker.getColor();
			lblSecondColor.setBackground(color);
		}
	}

	/**
	 * Creates the number of intervals according to the type
	 *
	 * @param evt
	 */
	private void intervalTypeChanged(ActionEvent evt) {
		int idx = cmbIntervalType.getSelectedIndex();
		ArrayList<Integer> mod = new ArrayList<Integer>();
		switch (idx) {
		case 0: // Quantiles
		case 1: // Equivalences
			for (int i = 2; i < 12; i++) {
				mod.add(i);
			}
			break;
		case 2: // Moyennes
			mod.add(2);
			mod.add(4);
			mod.add(8);
			break;
		case 3: // Standard
			mod.add(3);
			mod.add(5);
			mod.add(7);
			break;
		}

		cmbIntervalCount.setModel(new DefaultComboBoxModel(mod
				.toArray(new Integer[0])));
	}

	public ILegendPanelUI newInstance(LegendContext legendContext) {
		return new PnlIntervalLegend(legendContext);
	}

	public void setLegend(Legend legend) {
		this.legend = (IntervalLegend) legend;
		syncFieldsWithLegend();
		super.setLegend(legend);
	}

	private void syncFieldsWithLegend() {

		ArrayList<String> validFieldNames = new ArrayList<String>();
		try {
			ILayer layer = legendContext.getLayer();
			int numFields = layer.getDataSource().getFieldCount();
			for (int i = 0; i < numFields; i++) {
				int fieldType = layer.getDataSource().getFieldType(i)
						.getTypeCode();
				if (TypeFactory.isNumerical(fieldType)) {
					validFieldNames.add(layer.getDataSource().getFieldName(i));
				}
			}
		} catch (DriverException e) {
			Services.getErrorManager().error("Cannot access layer fields", e);
		}

		cmbFieldNames.setModel(new DefaultComboBoxModel(validFieldNames
				.toArray(new String[0])));

		String field = legend.getClassificationField();
		if (field != null) {
			cmbFieldNames.setSelectedItem(field);
		} else if (validFieldNames.size() > 0) {
			cmbFieldNames.setSelectedIndex(0);
		}
	}

	@Override
	protected void addAllAction() {
		RangeMethod rm = null;
		try {
			rm = new RangeMethod(legendContext.getLayer().getDataSource(),
					(String) cmbFieldNames.getSelectedItem(),
					(Integer) cmbIntervalCount.getSelectedItem());

			int typeOfIntervals = cmbIntervalType.getSelectedIndex();

			switch (typeOfIntervals) {
			case 0:
				rm.disecEquivalences();
				break;
			case 1:
				rm.disecQuantiles();
				break;
			case 2:
				rm.disecMean();
				break;
			case 3:
				rm.disecStandard();
				break;
			}

		} catch (DriverException e) {
			Services.getErrorManager().error("Cannot calculate intervals", e);
			return;
		}

		Interval[] intervals = rm.getIntervals();

		legend.clear();

		int numberOfIntervals = intervals.length;

		int r1 = lblFirstColor.getBackground().getRed();
		int g1 = lblFirstColor.getBackground().getGreen();
		int b1 = lblFirstColor.getBackground().getBlue();

		int r2 = lblSecondColor.getBackground().getRed();
		int g2 = lblSecondColor.getBackground().getGreen();
		int b2 = lblSecondColor.getBackground().getBlue();

		int incR = (r2 - r1) / numberOfIntervals;
		int incG = (g2 - g1) / numberOfIntervals;
		int incB = (b2 - b1) / numberOfIntervals;

		int r = r1;
		int g = g1;
		int b = b1;

		Color color = new Color(r, g, b);

		for (int i = 0; i < intervals.length; i++) {
			Interval inter = intervals[i];

			Symbol s = createSymbol(color);

			legend.addInterval(inter.getMinValue(), inter.isMinIncluded(),
					inter.getMaxValue(), inter.isMaxIncluded(), s, inter
							.getIntervalString());

			r += incR;
			g += incG;
			b += incB;
			if (i != intervals.length - 2) {
				color = new Color(r, g, b);
			} else {
				color = new Color(r2, g2, b2);
			}

		}
		getTableModel().setLegend(legend);
	}

	private IntervalLegendTableModel getTableModel() {
		return (IntervalLegendTableModel) tableModel;
	}

	/**
	 * Creates the symbol with the specified fillColor and with a black outline
	 *
	 * @param constraint
	 * @param fillColor
	 * @return Symbol
	 */
	private Symbol createSymbol(Color fillColor) {
		Symbol s;

		Color outline = Color.black;

		Symbol lineSymbol = SymbolFactory.createLineSymbol(fillColor, 1);
		Symbol pointSymbol = SymbolFactory.createCirclePointSymbol(outline,
				fillColor, 10);
		Symbol polygonSymbol = SymbolFactory.createPolygonSymbol(outline,
				fillColor);
		GeometryConstraint geometryConstraint = legendContext
				.getGeometryConstraint();
		if (geometryConstraint == null) {
			s = SymbolFactory.createSymbolComposite(polygonSymbol, lineSymbol,
					pointSymbol);
		} else {
			int geometry = geometryConstraint.getGeometryType();
			switch (geometry) {
			case GeometryConstraint.LINESTRING:
			case GeometryConstraint.MULTI_LINESTRING:
				s = lineSymbol;
				break;
			case GeometryConstraint.POINT:
			case GeometryConstraint.MULTI_POINT:
				s = pointSymbol;
				break;
			case GeometryConstraint.POLYGON:
			case GeometryConstraint.MULTI_POLYGON:
				s = polygonSymbol;
				break;
			default:
				throw new RuntimeException("bug!");
			}
		}
		return s;
	}

	@Override
	protected void addOneAction() {
		int rowCount = tableModel.getRowCount();

		if (rowCount < 32) {
			Symbol sym = createRandomSymbol();
			Value minValue = ValueFactory.createNullValue();
			Value maxValue = ValueFactory.createNullValue();
			String label = "Rest of values";
			if (rowCount > 0) {
				sym = (Symbol) tableModel.getValueAt(0, 0);
				minValue = (Value) tableModel.getValueAt(0, 1);
				maxValue = (Value) tableModel.getValueAt(0, 2);
				label = (String) tableModel.getValueAt(0, 3);
			}
			getTableModel().insertRow(sym, minValue, maxValue, label);
		} else {
			JOptionPane.showMessageDialog(this,
					"Cannot have more than 32 classifications");
		}
	}

	@Override
	protected boolean canAdd() {
		return cmbFieldNames.getSelectedIndex() != -1;
	}

	public String getLegendTypeName() {
		return IntervalLegend.NAME;
	}

}
