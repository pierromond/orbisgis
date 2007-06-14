package org.gdms.sql.instruction;

import java.util.Iterator;

import org.gdms.data.DataSource;
import org.gdms.data.values.Value;
import org.gdms.driver.DriverException;
import org.gdms.sql.strategies.Row;

public class SpatialIndexHint extends IndexHint {

	public SpatialIndexHint(String fieldTable, String fieldName, Expression filteringExpression) {
		super(fieldTable, fieldName, filteringExpression);
	}

	@Override
	public Iterator<Row> getRowIterator(DataSource ds, Value filteringValue) throws DriverException {
		if (ds.getAlias().equals(super.table)) {
			return null;//TODO new SpatialIterator(ds, filteringValue, field);
		} else {
			return null;
		}
	}

}
