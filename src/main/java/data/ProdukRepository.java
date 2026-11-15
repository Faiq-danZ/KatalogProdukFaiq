package data;

import db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Produk;

public class ProdukRepository {

    public List<Produk> findAll() {
        List<Produk> list = new ArrayList<>();
        String sql = "SELECT id, nama, harga, foto FROM produk ORDER BY id DESC";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Produk(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getInt("harga"),
                    rs.getString("foto")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(Produk p) {
        String sql = "INSERT INTO produk (nama, harga, foto) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getNama());
            pstmt.setInt(2, p.getHarga());
            pstmt.setString(3, p.getFoto());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Produk p) {
        String sql = "UPDATE produk SET nama = ?, harga = ?, foto = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getNama());
            pstmt.setInt(2, p.getHarga());
            pstmt.setString(3, p.getFoto());
            pstmt.setInt(4, p.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM produk WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}