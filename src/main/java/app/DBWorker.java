package app;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static app.Connector.LOG;

public class DBWorker {

    private final static String QUIT_WORD = "quit";

    public DBWorker() {
    }

    public static void createStatement(final Connector connector) {
        LOG.info("Create statement");
        LOG.info("Enter query: ");
        try (final Statement statement = connector.getCon().createStatement();
             Scanner in = new Scanner(System.in)) {
            while (!statement.isClosed()) {
                StringBuilder query = new StringBuilder();
                query.append(in.nextLine());
                if (query.toString().equals(QUIT_WORD)) {
                    LOG.info("Close program");
                    break;
                } else {
                    statement.execute(String.valueOf(query));
                }

                if (query.charAt(query.length() - 1) == ';') {
                    LOG.info("Success! Enter next query or type 'quit' to stop the program");
                }
            }
        } catch (final SQLException e) {
            LOG.error("Execute error! Check the syntax!", e);
        }
    }
}