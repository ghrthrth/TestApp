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
@Getter @Setter
@NoArgsConstructor
public class Connector implements Closeable {

    public static final Logger LOG = Logger.getLogger(Connector.class);

    Connection con;

    String url;
    String user;
    String pass;

    public Connection connect(final String url, final String user, final String pass) throws Exception {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (final Throwable e) {
            LOG.error("No database", e);
        }

        return DriverManager.getConnection(url, user, pass);
    }

    public void createConnection() throws Exception {
        Connector connector = new Connector();
        fillConnectionData(connector);
        connector.setCon(connect(connector.getUrl(), connector.getUser(), connector.getPass()));
        DBWorker.createStatement(connector);
    }

    private void fillConnectionData(final Connector connector) {
        try {
            Scanner in = new Scanner(System.in);
            LOG.info("writing USER: ");
            connector.setUser(in.nextLine());
            LOG.info("writing PASS: ");
            connector.setPass(in.nextLine());
            LOG.info("written URL: ");
            connector.setUrl(in.nextLine());

            if (!isNull(connector.getUrl(), connector.getPass(), connector.getUser())
                    && !isEmpty(connector.getUrl(), connector.getPass(), connector.getUser())) {
                LOG.info("Data successfully filled");
            }

        } catch (final Exception e) {
            LOG.error(e);
        }
    }

    public static boolean isEmpty(String url, String pass, String user) {
        return url.isEmpty() && pass.isEmpty() && user.isEmpty();
    }

    public static boolean isNull(String url, String pass, String user) {
        return user == null && pass == null && url == null;
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