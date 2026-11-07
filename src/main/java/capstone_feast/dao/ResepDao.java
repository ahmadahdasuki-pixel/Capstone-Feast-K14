package capstone_feast.dao;

import capstone_feast.config.DatabaseConnection;
import capstone_feast.model.DetailResep;
import capstone_feast.model.Produk;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResepDao {
    public static class BahanItem {
        private int idBahan;
        private String namaBahan;
        private String satuan;

        public BahanItem(int idBahan, String namaBahan, String satuan) {
            this.idBahan = idBahan;
            this.namaBahan = namaBahan;
            this.satuan = satuan;
        }

        public int getIdBahan() { return idBahan; }
        public String getNamaBahan() { return namaBahan; }
        public String getSatuan() { return satuan; }

        @Override
        public String toString() {
            return namaBahan + " (" + satuan + ")";
        }
    }

    // 1. Mengambil seluruh daftar produk  
    public List<Produk> getAllProdukForCombo() {
        List<Produk> list = new ArrayList<>();
        String query = "SELECT id_produk, id_admin, nama_produk, satuan_produk, harga_jual, status_produk FROM produk";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Produk p = new Produk();
                p.setIdProduk(rs.getInt("id_produk"));
                p.setIdAdmin(rs.getInt("id_admin")); 
                p.setNamaProduk(rs.getString("nama_produk"));
                p.setSatuanProduk(rs.getString("satuan_produk"));
                
                p.setHargaJual(rs.getBigDecimal("harga_jual") != null ? rs.getBigDecimal("harga_jual").doubleValue() : 0.0);
                
                p.setStatusProduk(rs.getString("status_produk"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Mengambil daftar detail resep berdasarkan id_produk yang dipilih
    public List<DetailResep> getResepByProduk(int idProduk) {
        List<DetailResep> list = new ArrayList<>();
        String query = "SELECT dr.id_detail_resep, dr.id_produk, dr.id_bahan, dr.jumlah_bahan, " +
                       "b.nama_bahan, b.satuan " +
                       "FROM detail_resep dr " +
                       "JOIN bahan_baku b ON dr.id_bahan = b.id_bahan " +
                       "WHERE dr.id_produk = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, idProduk);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                DetailResep dr = new DetailResep();
                dr.setIdDetailResep(rs.getInt("id_detail_resep"));
                dr.setIdProduk(rs.getInt("id_produk"));
                dr.setIdBahan(rs.getInt("id_bahan"));
                dr.setJumlahBahan(rs.getDouble("jumlah_bahan"));
                dr.setNamaBahan(rs.getString("nama_bahan"));
                dr.setSatuan(rs.getString("satuan"));
                list.add(dr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Mengambil daftar seluruh bahan baku 
    public List<BahanItem> getListBahanItem() {
        List<BahanItem> list = new ArrayList<>();
        String query = "SELECT id_bahan, nama_bahan, satuan FROM bahan_baku";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                list.add(new BahanItem(
                    rs.getInt("id_bahan"), 
                    rs.getString("nama_bahan"), 
                    rs.getString("satuan")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 4. Menyimpan komposisi resep baru ke tabel detail_resep
    public boolean insertDetailResep(int idProduk, int idBahan, double jumlahBahan) {
        String query = "INSERT INTO detail_resep (id_produk, id_bahan, jumlah_bahan) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, idProduk);
            ps.setInt(2, idBahan);
            ps.setDouble(3, jumlahBahan);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Mengubah/memperbarui jumlah takaran resep berdasarkan id_detail_resep
    public boolean updateTakaranResep(int idDetailResep, double jumlahBaru) {
        String query = "UPDATE detail_resep SET jumlah_bahan = ? WHERE id_detail_resep = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setDouble(1, jumlahBaru);
            ps.setInt(2, idDetailResep);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 6. Menghapus bahan dari resep berdasarkan id_detail_resep
    public boolean deleteResep(int idDetailResep) {
        String query = "DELETE FROM detail_resep WHERE id_detail_resep = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, idDetailResep);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}