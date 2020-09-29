package app;

import static app.Connector.LOG;

public class Runner {

    public static void main(String[] args) {
        try (final Connector connector = new Connector()){
            connector.createConnection();
        } catch (Exception e) {
            LOG.error(e);
        }
    }}