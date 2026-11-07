package capstone_feast.dao;

import capstone_feast.config.DatabaseConnection;
import capstone_feast.model.ProduksiModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduksiDao {

    public static class ProdukItem {
        private int id;
        private String nama;
        private String status; 

        public ProdukItem(int id, String nama, String status) {
            this.id = id;
            this.nama = nama;
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public String getNama() {
            return nama;
        }

        public String getStatus() {
            return status;
        }

        public String getStatusBatch() {
            return status;
        }

        @Override
        public String toString() {
            return nama; 
        }
    }

    public static class BahanBakuModel {
        private String namaBahan;
        private double jumlahPerUnit;
        private double stokSaatIni;
        private String satuan;
        private String statusBahan; 

        public String getNamaBahan() { return namaBahan; }
        public void setNamaBahan(String namaBahan) { this.namaBahan = namaBahan; }

        public double getJumlahPerUnit() { return jumlahPerUnit; }
        public void setJumlahPerUnit(double jumlahPerUnit) { this.jumlahPerUnit = jumlahPerUnit; }

        public double getStokSaatIni() { return stokSaatIni; }
        public void setStokSaatIni(double stokSaatIni) { this.stokSaatIni = stokSaatIni; }

        public String getSatuan() { return satuan; }
        public void setSatuan(String satuan) { this.satuan = satuan; }

        public String getStatusBahan() { return statusBahan; }
        public void setStatusBahan(String statusBahan) { this.statusBahan = statusBahan; }
    }

    // 1. Mengambil daftar produk yang aktif 
    public List<ProdukItem> getListProdukItem() {
        List<ProdukItem> list = new ArrayList<>();
        String query = "SELECT id_produk, nama_produk, status_produk FROM produk WHERE status_produk = 'Aktif' ORDER BY nama_produk ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                list.add(new ProdukItem(
                    rs.getInt("id_produk"), 
                    rs.getString("nama_produk"), 
                    rs.getString("status_produk")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Mengambil daftar bahan baku berdasarkan resep produk 
    public List<BahanBakuModel> getBahanBakuByProduk(int idProduk) {
        List<BahanBakuModel> list = new ArrayList<>();
        
        String query = "SELECT b.nama_bahan, dr.jumlah_bahan AS jumlah_per_unit, b.stok_saat_ini, b.satuan, b.status_bahan_baku " +
                       "FROM detail_resep dr JOIN bahan_baku b ON dr.id_bahan = b.id_bahan " +
                       "WHERE dr.id_produk = ? AND b.status_bahan_baku = 'Aktif'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, idProduk);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                BahanBakuModel b = new BahanBakuModel();
                b.setNamaBahan(rs.getString("nama_bahan"));
                b.setJumlahPerUnit(rs.getDouble("jumlah_per_unit"));
                b.setStokSaatIni(rs.getDouble("stok_saat_ini"));
                b.setSatuan(rs.getString("satuan"));
                b.setStatusBahan(rs.getString("status_bahan_baku")); 
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Menyimpan data produksi baru
    public boolean insertProduksi(int idProduk, int idStaff, String tglProduksi, String tglKadaluarsa, int jumlahProduksi) {
        String queryInsertBatch = "INSERT INTO produksi_batch (id_produk, id_staff, tanggal_produksi, tanggal_kadaluarsa, jumlah_produksi, sisa_stok_batch, status_batch) VALUES (?, ?, ?, ?, ?, ?, 'Di Proses')";
        String queryGetResep = "SELECT dr.id_bahan, dr.jumlah_bahan FROM detail_resep dr JOIN bahan_baku b ON dr.id_bahan = b.id_bahan WHERE dr.id_produk = ? AND b.status_bahan_baku = 'Aktif'";
        String queryUpdateStok = "UPDATE bahan_baku SET stok_saat_ini = stok_saat_ini - ? WHERE id_bahan = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); 
            try (PreparedStatement psResep = conn.prepareStatement(queryGetResep)) {
                psResep.setInt(1, idProduk);
                try (ResultSet rsResep = psResep.executeQuery()) {
                    boolean adaResep = false;
                    while (rsResep.next()) {
                        adaResep = true;
                        int idBahan = rsResep.getInt("id_bahan");
                        double jumlahPerUnit = rsResep.getDouble("jumlah_bahan");
                        double totalDibutuhkan = jumlahPerUnit * jumlahProduksi;
                        
                        try (PreparedStatement psUpdateStok = conn.prepareStatement(queryUpdateStok)) {
                            psUpdateStok.setDouble(1, totalDibutuhkan);
                            psUpdateStok.setInt(2, idBahan);
                            psUpdateStok.executeUpdate();
                        }
                    }
                    
                    if (!adaResep) {
                        conn.rollback();
                        return false; 
                    }
                }
            }
            
            try (PreparedStatement psBatch = conn.prepareStatement(queryInsertBatch)) {
                psBatch.setInt(1, idProduk);
                psBatch.setInt(2, idStaff);
                psBatch.setString(3, tglProduksi);
                psBatch.setString(4, tglKadaluarsa);
                psBatch.setInt(5, jumlahProduksi);
                psBatch.setInt(6, jumlahProduksi); 
                psBatch.executeUpdate();
            }
            
            conn.commit(); 
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Mengubah status produksi (Menjadi "Selesai" atau "Gagal")
    public boolean updateStatusProduksi(int idBatch, String statusBaru) {
        String query = "UPDATE produksi_batch SET status_batch = ? WHERE id_batch = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, statusBaru);
            ps.setInt(2, idBatch);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Mengambil seluruh data produksi atau berdasarkan status dengan pengurutan FEFO
    public List<ProduksiModel> getDataByStatusSortedByFEFO(String status) {
        List<ProduksiModel> list = new ArrayList<>();
        String query;
        
        if ("Semua".equalsIgnoreCase(status) || status == null) {
            query = "SELECT pb.*, p.nama_produk FROM produksi_batch pb " +
                    "JOIN produk p ON pb.id_produk = p.id_produk " +
                    "ORDER BY pb.tanggal_kadaluarsa ASC, pb.id_batch ASC";
        } else {
            query = "SELECT pb.*, p.nama_produk FROM produksi_batch pb " +
                    "JOIN produk p ON pb.id_produk = p.id_produk " +
                    "WHERE REPLACE(LOWER(pb.status_batch), ' ', '') = REPLACE(LOWER(?), ' ', '') " +
                    "ORDER BY pb.tanggal_kadaluarsa ASC, pb.id_batch ASC";
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            if (!"Semua".equalsIgnoreCase(status) && status != null) {
                ps.setString(1, status.trim());
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProduksiModel pb = new ProduksiModel();
                pb.setIdBatch(rs.getInt("id_batch"));
                pb.setNamaProduk(rs.getString("nama_produk"));
                pb.setNomorBatch("BAT-00" + rs.getInt("id_batch"));
                
                try {
                    pb.setJumlahProduksi(rs.getInt("jumlah_produksi"));
                } catch (SQLException e) {
                    try {
                        pb.setJumlahProduksi(rs.getInt("jumlah"));
                    } catch (SQLException ex) {
                        pb.setJumlahProduksi(0);
                    }
                }

                pb.setTanggalProduksi(rs.getDate("tanggal_produksi") != null ? rs.getDate("tanggal_produksi").toString() : "");
                pb.setTanggalKadaluarsa(rs.getDate("tanggal_kadaluarsa") != null ? rs.getDate("tanggal_kadaluarsa").toString() : "");
                
                try {
                    pb.setStatusBatch(rs.getString("status_batch"));
                } catch (SQLException e) {
                    pb.setStatusBatch("");
                }

                list.add(pb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ProduksiModel> getAllProduksi() {
        return getDataByStatusSortedByFEFO("Semua");
    }
}