package capstone_feast.dao;

import capstone_feast.config.DatabaseConnection;
import capstone_feast.model.Produk;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdukDao {
    private Connection connection;

    public ProdukDao() {
        this.connection = DatabaseConnection.getConnection();
    }

    public List<Produk> getAllProduk() {
        List<Produk> list = new ArrayList<>();
        String query = "SELECT * FROM produk ORDER BY id_produk ASC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Produk item = new Produk(
                    rs.getInt("id_produk"),
                    rs.getInt("id_admin"),
                    rs.getString("nama_produk"),
                    rs.getString("satuan_produk"),
                    rs.getDouble("harga_jual"),
                    rs.getString("status_produk")
                );
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // mengecek duplikasi berdasarkan nama produk
    public Produk getProdukByNama(String namaProduk) {
        String query = "SELECT * FROM produk WHERE LOWER(nama_produk) = LOWER(?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, namaProduk.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Produk(
                    rs.getInt("id_produk"),
                    rs.getInt("id_admin"),
                    rs.getString("nama_produk"),
                    rs.getString("satuan_produk"),
                    rs.getDouble("harga_jual"),
                    rs.getString("status_produk")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(Produk produk) {
        String query = "INSERT INTO produk (id_admin, nama_produk, satuan_produk, harga_jual, status_produk) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, produk.getIdAdmin());
            ps.setString(2, produk.getNamaProduk());
            ps.setString(3, produk.getSatuanProduk());
            ps.setDouble(4, produk.getHargaJual());
            ps.setString(5, produk.getStatusProduk());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Produk produk) {
        String query = "UPDATE produk SET nama_produk = ?, satuan_produk = ?, harga_jual = ?, status_produk = ? WHERE id_produk = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, produk.getNamaProduk());
            ps.setString(2, produk.getSatuanProduk());
            ps.setDouble(3, produk.getHargaJual());
            ps.setString(4, produk.getStatusProduk());
            ps.setInt(5, produk.getIdProduk());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int idProduk) {
        // Mengubah status produk menjadi 'Nonaktif' 
        String query = "UPDATE produk SET status_produk = 'Nonaktif' WHERE id_produk = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idProduk);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // mengaktifkan kembali produk yang berstatus Nonaktif
    public boolean aktifkanProduk(int idProduk) {
        String query = "UPDATE produk SET status_produk = 'Aktif' WHERE id_produk = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idProduk);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}