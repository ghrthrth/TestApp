package app;

import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

@Getter @Setter
public class Connector implements Closeable {

    public static final Logger LOG = Logger.getLogger(Connector.class);

    public Connection con;

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

            System.out.println("writing USER: ");
            setUser(in.nextLine());
            System.out.println("writing PASS: ");
            setPass(in.nextLine());
            System.out.println("written URL: ");
            setUrl(in.nextLine());

            if (getUser() != null && getPass() != null && getUrl() != null) {
                DBWorker.createStatement(db.connect(getUrl(), getUser(), getPass()));
            }

        } catch (Exception e) {
            LOG.error(e);
        }
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