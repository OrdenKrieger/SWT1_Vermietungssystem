package de.swt1.database;

import java.sql.*;

public class DBController {
    private static DBController dbcontroller;
    private static Connection connection;
    private static final String dbPath = System.getProperty("user.dir") + "\\" + "vermietungssystem.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) { e.printStackTrace(); }
    }

    private DBController(){
    }

    public static DBController getInstance(){
        if (dbcontroller == null) dbcontroller = new DBController();
        return dbcontroller;
    }

    private void initDBConnection() {
        try {
            if (connection != null)
                return;
            System.out.println("Creating Connection to Database...");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            if (!connection.isClosed())
                System.out.println("...Connection established");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    if (!connection.isClosed() && connection != null) {
                        connection.close();
                        if (connection.isClosed())
                            System.out.println("Connection to Database closed");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleDB() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS books;");
            stmt.executeUpdate("CREATE TABLE books (author, title, pages, price);");
            stmt.execute("INSERT INTO books (author, title, pages, price) VALUES ('Paulchen Paule', 'Paul der Penner', '1234', '5.67')");

            PreparedStatement ps = connection
                .prepareStatement("INSERT INTO books VALUES (?, ?, ?, ?);");

            ps.setString(1, "Willi Winzig");
            ps.setString(2, "Willi's Wille");
            ps.setInt(3, 432);
            ps.setDouble(4, 32.95);
            ps.addBatch();

            ps.setString(1, "Anton Antonius");
            ps.setString(2, "Anton's Alarm");
            ps.setInt(3, 123);
            ps.setDouble(4, 98.76);
            ps.addBatch();

            connection.setAutoCommit(false);
            ps.executeBatch();
            connection.setAutoCommit(true);

            ResultSet rs = stmt.executeQuery("SELECT * FROM books;");
            while (rs.next()) {
                System.out.println("Autor = " + rs.getString("author"));
                System.out.println("Titel = " + rs.getString("title"));
                System.out.println("Seiten = " + rs.getInt("pages"));
                System.out.println("Preis = " + rs.getDouble("price"));
            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Couldn't handle DB-Query");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();
        dbc.handleDB();
    }
}
