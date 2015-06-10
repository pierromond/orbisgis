package org.orbisgis.corejdbc;

import org.h2gis.utilities.SpatialResultSet;
import org.orbisgis.commons.progress.ProgressMonitor;

import javax.sql.rowset.JdbcRowSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.locks.Lock;

/**
 * A ReadRowSet can be initialized using {@link JdbcRowSet#setCommand(String)}
 * The rowset is state-full then it is advised to use {@link #getReadLock()} with
 * {@link Lock#tryLock(long, java.util.concurrent.TimeUnit)} in order to avoid dead locks.
 * @author Nicolas Fortin
 */
public interface ReadRowSet extends JdbcRowSet , SpatialResultSet {
    /**
     * @return Number of rows inside the table
     */
    long getRowCount() throws SQLException;

    /**
     * Initialize this row set. Same code as {@link #execute()}.
     * @param tableIdentifier Table identifier [[catalog.]schema.]table]
     * @param pk_name Primary key name to use with
     * @param pm Progress monitor Progression of primary key caching
     */
    public void initialize(String tableIdentifier,String pk_name, ProgressMonitor pm) throws SQLException;

    /**
     * Call this after {@link #setCommand(String)}. Cache the default primary key values then execute the command.
     * @param pm Progress monitor Progression of primary key caching
     */
    public void execute(ProgressMonitor pm) throws SQLException;

    /**
     * @return The table identifier [[catalog.]schema.]table
     */
    public String getTable();

    /**
     * @return The primary key column name used to map filtered row to row line number. Empty string if there is no primary key.
     */
    public String getPkName();

    /**
     * @return The read lock on this result set
     */
    Lock getReadLock();

    /**
     * Set the close delay of releasing of resources (0 ms by default)
     * @param milliseconds Time in milliseconds
     */
    void setCloseDelay(int milliseconds);

    /**
     * @return The numeric, simple primary key of the current row. Used to identify a row.
     */
    long getPk() throws SQLException;

    /**
     * Fetch row number using primary key values.
     * @param pkSet Primary key set {@link #getPk()}
     * @return Row identifier set {@link #getRow()}
     */
    SortedSet<Integer> getRowNumberFromRowPk(SortedSet<Long> pkSet) throws SQLException;
}
