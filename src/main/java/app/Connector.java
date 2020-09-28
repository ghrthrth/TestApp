package app;

import org.apache.log4j.Logger;

import java.util.Scanner;

public class Connector {
    public static final Logger LOG = Logger.getLogger(Connector.class);

    private static String url;
    private static String user;
    private static String pass;

    public static void createConnection() {
        fillConnectionData();
    }

    private static void fillConnectionData() {
        try (Scanner in = new Scanner(System.in);
             Database db = new Database()) {

            System.out.println("writing USER: ");
            setUser(in.nextLine());
            System.out.println("writing PASS: ");
            setPass(in.nextLine());
            System.out.println("written URL: ");
            setUrl(in.nextLine());

            if (getUser() != null && getPass() != null && url != null) {
                DBWorker.createStatement(db.connect(getUrl(), getPass(), getUser()));
            }

        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        Connector.url = url;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        Connector.user = user;
    }

    public static String getPass() {
        return pass;
    }

    public static void setPass(String pass) {
        Connector.pass = pass;
    }
}