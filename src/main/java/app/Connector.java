package app;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

@Getter @Setter
@NoArgsConstructor
public class Connector implements Closeable {

    public static final Logger LOG = Logger.getLogger(Connector.class);

    private Connection con;

    private String url;
    private String user;
    private String pass;

    public Connection connect(final String url, final String user, final String pass) throws Exception {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (final Throwable e) {
            LOG.error("No database", e);
        }

        return DriverManager.getConnection(url, user, pass);
    }

    public void createConnection() {
        fillConnectionData();
    }

    public void fillConnectionData() {
        try (Scanner in = new Scanner(System.in);
             Connector db = new Connector()) {

            LOG.info("writing USER: ");
            db.setUser(in.nextLine());
            LOG.info("writing PASS: ");
            db.setPass(in.nextLine());
            LOG.info("written URL: ");
            db.setUrl(in.nextLine());

            if (db.getUser() != null && db.getPass() != null && db.getUrl() != null) {
                db.setCon(db.connect(db.getUrl(), db.getUser(), db.getPass()));
                DBWorker.createStatement(db);
                LOG.info("create statement....");
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