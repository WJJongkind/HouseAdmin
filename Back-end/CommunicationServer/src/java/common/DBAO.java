package common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * This class forms the basis for all DataBase Access Objects (DBAO) used for effective communication
 * with databases.
 *
 * @author Wessel Jongkind
 * @version 27-07-2017
 */
public abstract class DBAO {

    /**
     * Attempts to close all the given connections.
     *
     * @param closeables The connections that should be closed.
     */
    protected static void closeQuietly(AutoCloseable... closeables) {
        for (AutoCloseable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Sets the default configuration which should be used for most connections
     * to a database. It turns off autocommit and makes transactions
     * serializable.
     *
     * @param connection The connection that should be configured.
     * @throws SQLException When the connection cannot be configured properly.
     */
    protected static void configureConnection(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    }

    public abstract void update() throws Exception;

    public abstract void delete() throws Exception;

    public abstract void create() throws Exception;
}
