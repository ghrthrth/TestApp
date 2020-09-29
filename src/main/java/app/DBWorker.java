package app;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static app.Connector.LOG;

public class DBWorker {
    public DBWorker() {
    }

    public static void createStatement(final Connector connector) {
        final Connection connection = connector.getCon();
        final String quitWord = "quit";
        StringBuilder query = new StringBuilder();
        LOG.info("Create statement");
        LOG.info("Enter query: ");
        try (Statement statement = connection.createStatement();
             Scanner in = new Scanner(System.in)) {
            while (!statement.isClosed()) {
                query.append(in.nextLine());
                if (query == null) {
                    LOG.info("Statement is null!");
                } else if (query.toString().equals(quitWord)) {
                    LOG.info("Close program");
                    statement.close();
                    break;
                } else {
                    statement.execute(String.valueOf(query));
                }

                if (query.charAt(query.length() - 1) == ';') {
                    query = new StringBuilder();
                    LOG.info("Success! Enter next query or type 'quit' to stop the program");
                }
            }
        } catch (final SQLException e) {
            LOG.error("Execute error! Check the syntax!", e);
        }
    }
}