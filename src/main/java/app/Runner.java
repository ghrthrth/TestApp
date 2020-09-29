package app;

public class Runner {

    public static void main(String[] args) {
        try (final Connector connector = new Connector()){
            connector.createConnection();
        }
    }}