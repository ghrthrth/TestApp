package app;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static app.Connector.LOG;

public class Database implements Closeable {
    public Connection con;

    public Connection connect(final String URL, final String USER, final String PASS) throws Exception {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (final Throwable e) {
            LOG.error("No database", e);
        }

        return DriverManager.getConnection(URL, USER, PASS);
    }

    @Override
    public void close() {
        if (con != null) {
            try {
                con.close();
            } catch (final SQLException e) {
                LOG.error(e);
            }
        }
    }
}
