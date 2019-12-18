package de.swt1.database;

import java.io.*;
import java.sql.*;
import java.util.Arrays;

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

    private byte[] readFile(String file) {
        ByteArrayOutputStream bos = null;
        try {
            File f = new File(file);
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;
    }

    private void handleDB() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS objekt;");
            stmt.executeUpdate("CREATE TABLE objekt (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL DEFAULT '0'," +
                " name varchar(50) NOT NULL, etage varchar(50) DEFAULT NULL," +
                " textfeld varchar(200) DEFAULT NULL, bild blob);");

            //stmt.execute("INSERT INTO objekt (name, etage, textfeld, bild) VALUES ('TH-Lübeck', '13. Etage', 'Tolles Gebäude', null)");
            PreparedStatement ps = connection
                .prepareStatement("INSERT INTO objekt (name, etage, textfeld, bild) VALUES (?, ?, ?, ?);");

            ps.setString(1, "TH-Lübeck");
            ps.setString(2, "13. Etage");
            ps.setString(3, "Tolles Gebäude");

            // Todo not working correct right now, also my be outsourced to own file util
            // Every other entry also has the same Byte Array
            try {
                ps.setBytes(4, readFile(System.getProperty("user.dir") + "\\" + "example.jpg"));
            } catch (Exception e) { e.printStackTrace(); }
            ps.addBatch();

            ps.setString(1, "Willy-Brandt-Sporthalle");
            ps.setString(2, "");
            ps.setString(3, "Sport!!11");
            //ps.setBlob(4, (Blob)null);
            ps.addBatch();

            ps.setString(1, "McDonald's Lübeck");
            ps.setString(2, "");
            ps.setString(3, "Tolles Ambiente mit leckeren Burgern!");
            //ps.setBlob(4, (Blob)null);
            ps.addBatch();

            ps.setString(1, "Bowling World Lübeck");
            ps.setString(2, "2. Etage");
            ps.setString(3, "Hier bringt kugeln Spaß");
            //ps.setBlob(4, (Blob)null);
            ps.addBatch();

            connection.setAutoCommit(false);
            ps.executeBatch();
            connection.setAutoCommit(true);

            ResultSet rs = stmt.executeQuery("SELECT * FROM objekt;");
            while (rs.next()) {
                System.out.println("ID = " + rs.getInt("id"));
                System.out.println("Name = " + rs.getString("name"));
                System.out.println("Etage = " + rs.getString("etage"));
                System.out.println("Beschreibung = " + rs.getString("textfeld"));
                // Todo not working correct right now
                System.out.println("Bild = " + Arrays.toString(rs.getBytes("bild")));
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
