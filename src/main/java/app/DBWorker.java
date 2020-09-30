package app;

import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static app.Connector.LOG;

@NoArgsConstructor
public class DBWorker {

    private final static String QUIT_WORD = "quit";

    public static void createStatement(final Connector connector) {
        LOG.info("Create statement");
        LOG.info("Enter query: ");
        try (final Statement statement = connector.getCon().createStatement()) {
            try (Scanner in = new Scanner(System.in)) {
                while (!statement.isClosed()) {
                    StringBuilder query = new StringBuilder();
                    query.append(in.nextLine());
                    if (query.toString().equals(QUIT_WORD)) {
                        LOG.info("Close program");
                        break;
                    }
                    if (query.length() >= 150) {
                        LOG.warn("request must not exceed 150 characters");
                        query.delete(0, query.length());
                        query.append(in.nextLine());
                    }
                    if (query.charAt(query.length() - 1) == ';') {
                        statement.execute(String.valueOf(query));
                        LOG.info("Success! Enter next query or type 'quit' to stop the program");
                    }
                }
            }
        } catch (final SQLException e) {
            LOG.error("Execute error! Check the syntax!", e);
        }
    }
}