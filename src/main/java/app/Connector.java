package app;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class Connector implements Closeable {

    public static final Logger LOG = Logger.getLogger(Connector.class);

    Connection con;

    String url;
    String user;
    String pass;
    String select;

    private static boolean isEmpty(String url, String pass, String user) {
        return url.isEmpty() && pass.isEmpty() && user.isEmpty();
    }

    private static boolean isNull(String url, String pass, String user) {
        return user == null && pass == null && url == null;
    }

    public Connection setDBCConnection(final String url, final String user, final String pass, final String select)
            throws ClassNotFoundException, LinkageError, SQLException {
        try {
            if (select.equals("1")) {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } else {
                Class.forName("org.postgresql.Driver");
            }
        } catch (final ClassNotFoundException | LinkageError e) {
            LOG.error("No database", e);
            throw e;
        } catch (final Exception e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            throw e;
        }

        return DriverManager.getConnection(url, user, pass);
    }

    public void createConnection(final Connector connector) throws Exception {
        fillConnectionData(connector);
        connector.setCon(setDBCConnection(connector.url, connector.user, connector.pass, connector.select));
    }

    private void fillConnectionData(final Connector connector) {
        try {
            Scanner in = new Scanner(System.in);
            LOG.info("Written number 1 if you want to select MySQL," +
                    "or any number if you want to select PostgreSQL");
            connector.setSelect(in.nextLine());
            LOG.info("written URL: ");
            connector.setUrl(in.nextLine());
            while (connector.getSelect().equals("1") && connector.getUrl().contains("jdbc:postgresql")) {
                LOG.warn("Wrong Url, write again");
                connector.setUrl(in.nextLine());
            }
            LOG.info("writing USER: ");
            connector.setUser(in.nextLine());
            LOG.info("writing PASS: ");
            connector.setPass(in.nextLine());

            if (!isNull(connector.url, connector.pass, connector.user)
                    && !isEmpty(connector.url, connector.pass, connector.user)) {
                LOG.info("Data successfully filled");
            }

        } catch (final Exception e) {
            LOG.error(e);
        }
    }

    @Override
    public void close() {
        if (getCon() != null) {
            try {
                getCon().close();
            } catch (final SQLException e) {
                LOG.error(e);
            }
        }
    }
}