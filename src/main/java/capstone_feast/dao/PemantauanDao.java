package capstone_feast.dao;

import capstone_feast.config.DatabaseConnection;
import capstone_feast.model.PemantauanModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PemantauanDao {
    private Connection connection;

    public PemantauanDao() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Mengambil data stok bahan baku dari database
    public List<PemantauanModel> getAllBahanBaku() {
        List<PemantauanModel> list = new ArrayList<>();
        String query = "SELECT nama_bahan, satuan, stok_saat_ini, stok_minimum FROM bahan_baku";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String nama = rs.getString("nama_bahan");
                String satuan = rs.getString("satuan");
                double stok = rs.getDouble("stok_saat_ini");
                double min = rs.getDouble("stok_minimum");
                
                String status = (stok <= min) ? "Minimum" : "Cukup";

                list.add(new PemantauanModel(nama, satuan, stok, min, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Mengambil data stok batch produksi dari database
    public List<PemantauanModel> getAllBatchProduksi() {
        List<PemantauanModel> list = new ArrayList<>();
        String query = "SELECT pb.id_batch, p.nama_produk, pb.tanggal_produksi, pb.tanggal_kadaluarsa, pb.sisa_stok_batch, pb.status_batch " +
                       "FROM produksi_batch pb " +
                       "JOIN produk p ON pb.id_produk = p.id_produk";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new PemantauanModel(
                    rs.getInt("id_batch"),
                    rs.getString("nama_produk"),
                    rs.getString("tanggal_produksi"),
                    rs.getString("tanggal_kadaluarsa"),
                    rs.getDouble("sisa_stok_batch"),
                    rs.getString("status_batch")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean buangStokBatch(int idBatch, String alasan) {
    if (alasan != null && alasan.length() > 15) {
        alasan = alasan.substring(0, 15);
    }
    
    String query = "UPDATE produksi_batch SET sisa_stok_batch = 0, status_batch = ? WHERE id_batch = ?";
    try (PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setString(1, "Dibuang"); 
        ps.setInt(2, idBatch);
        ps.executeUpdate();
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
}