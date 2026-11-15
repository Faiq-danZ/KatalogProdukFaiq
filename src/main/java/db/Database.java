package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:katalog_produk.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Membuat tabel produk jika belum tersedia di database
    public static void init() {
        String sql = "CREATE TABLE IF NOT EXISTS produk ("
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                   + "nama TEXT NOT NULL, "
                   + "harga INTEGER NOT NULL, "
                   + "foto TEXT NOT NULL"
                   + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}