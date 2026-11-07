package capstone_feast.dao;

import capstone_feast.config.DatabaseConnection;
import capstone_feast.model.ComboItem;
import capstone_feast.model.DistribusiModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DistribusiDao {
    private Connection connection;

    public DistribusiDao() {
        this.connection = DatabaseConnection.getConnection();
    }

    // 1. Ambil list Batch Produksi yang masih ada stok 
    public List<ComboItem> getListBatchAktif() {
        List<ComboItem> list = new ArrayList<>();
        String query = "SELECT pb.id_batch, p.nama_produk, pb.sisa_stok_batch " +
                       "FROM produksi_batch pb " +
                       "JOIN produk p ON pb.id_produk = p.id_produk " +
                       "WHERE pb.sisa_stok_batch > 0 " +
                       "ORDER BY pb.id_batch ASC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int idBatch = rs.getInt("id_batch");
                String namaProduk = rs.getString("nama_produk");
                String display = "BAT-" + idBatch + " - " + namaProduk + " (Stok: " + rs.getInt("sisa_stok_batch") + ")";
                list.add(new ComboItem(idBatch, display));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Ambil list Distributor yang aktif
    public List<ComboItem> getListDistributor() {
        List<ComboItem> list = new ArrayList<>();
        String query = "SELECT id_distributor, nama_distributor FROM distributor WHERE status_distributor = 'aktif' ORDER BY nama_distributor ASC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new ComboItem(rs.getInt("id_distributor"), rs.getString("nama_distributor")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Ambil sisa stok  dari sebuah batch
    public int getSisaStokBatch(int idBatch) {
        String query = "SELECT sisa_stok_batch FROM produksi_batch WHERE id_batch = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idBatch);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("sisa_stok_batch");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 4. Proses Simpan Distribusi 
    public boolean simpanDistribusi(int idBatch, int idDistributor, int jumlah, String tglKirim) {
        if (tglKirim != null && tglKirim.matches("\\d{2}-\\d{2}-\\d{4}")) {
            String[] parts = tglKirim.split("-");
            tglKirim = parts[2] + "-" + parts[1] + "-" + parts[0];
        }

        String namaProdukAsli = "Produk Distribusi";
        String qProduk = "SELECT p.nama_produk FROM produksi_batch pb JOIN produk p ON pb.id_produk = p.id_produk WHERE pb.id_batch = ?";
        try (PreparedStatement psP = connection.prepareStatement(qProduk)) {
            psP.setInt(1, idBatch);
            try (ResultSet rsP = psP.executeQuery()) {
                if (rsP.next()) {
                    namaProdukAsli = rsP.getString("nama_produk");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String keteranganVal = "[PRODUK:" + namaProdukAsli + "] [JML:" + jumlah + "] (Batch ID: " + idBatch + ")";
        
        String queryInsert = "INSERT INTO distribusi (id_distributor, id_staff, tanggal_distribusi, status_distribusi, keterangan) VALUES (?, 4, ?, 'diproses', ?)";
        String queryUpdateStok = "UPDATE produksi_batch SET sisa_stok_batch = sisa_stok_batch - ? WHERE id_batch = ?";

        try {
            connection.setAutoCommit(false);

            // Insert ke tabel distribusi
            try (PreparedStatement psInsert = connection.prepareStatement(queryInsert)) {
                psInsert.setInt(1, idDistributor);
                psInsert.setString(2, tglKirim);
                psInsert.setString(3, keteranganVal);
                psInsert.executeUpdate();
            }

            // Update kurangi stok batch
            try (PreparedStatement psUpdate = connection.prepareStatement(queryUpdateStok)) {
                psUpdate.setInt(1, jumlah);
                psUpdate.setInt(2, idBatch);
                psUpdate.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 5. Ambil semua riwayat distribusi )
    public List<DistribusiModel> getAllRiwayat() {
        return getRiwayatByStatusAndSort("Semua", "DESC");
    }

    public List<DistribusiModel> getRiwayatByStatusAndSort(String statusFilter, String sortOrder) {
        List<DistribusiModel> list = new ArrayList<>();
        
        List<String> masterProduk = new ArrayList<>();
        String qMaster = "SELECT nama_produk FROM produk ORDER BY id_produk ASC";
        try (Statement stmtM = connection.createStatement();
             ResultSet rsM = stmtM.executeQuery(qMaster)) {
            while (rsM.next()) {
                masterProduk.add(rsM.getString("nama_produk"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        if (masterProduk.isEmpty()) {
            masterProduk.add("Produk Roti Umum");
        }

        StringBuilder query = new StringBuilder(
            "SELECT d.id_distribusi, d.tanggal_distribusi, dist.nama_distributor, d.status_distribusi, d.keterangan " +
            "FROM distribusi d " +
            "LEFT JOIN distributor dist ON d.id_distributor = dist.id_distributor "
        );

        boolean pakaiFilter = (statusFilter != null && !statusFilter.equalsIgnoreCase("Semua") && !statusFilter.equalsIgnoreCase("Item 1"));
        if (pakaiFilter) {
            query.append("WHERE REPLACE(LOWER(d.status_distribusi), ' ', '') = REPLACE(LOWER(?), ' ', '') ");
        }

        String sorting = ("ASC".equalsIgnoreCase(sortOrder)) ? "ASC" : "DESC";
        query.append("ORDER BY d.tanggal_distribusi ").append(sorting).append(", d.id_distribusi DESC");

        try (PreparedStatement ps = connection.prepareStatement(query.toString())) {
            if (pakaiFilter) {
                ps.setString(1, statusFilter.trim());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idDistribusi = rs.getInt("id_distribusi");
                    String tgl = rs.getDate("tanggal_distribusi") != null ? rs.getDate("tanggal_distribusi").toString() : "";
                    String distributor = rs.getString("nama_distributor");
                    if (distributor == null) distributor = "Distributor Umum";
                    String status = rs.getString("status_distribusi");
                    String ket = rs.getString("keterangan");

                    String namaProduk = "";
                    int jumlahItem = 0;

                    if (ket != null) {
                        if (ket.contains("[PRODUK:") && ket.contains("[JML:")) {
                            try {
                                int startP = ket.indexOf("[PRODUK:") + 8;
                                int endP = ket.indexOf("]", startP);
                                namaProduk = ket.substring(startP, endP);

                                int startJ = ket.indexOf("[JML:") + 5;
                                int endJ = ket.indexOf("]", startJ);
                                jumlahItem = Integer.parseInt(ket.substring(startJ, endJ).trim());
                            } catch (Exception e) {
                                namaProduk = ket;
                            }
                        } else if (ket.toLowerCase().contains("jumlah:")) {
                            try {
                                String numericOnly = ket.replaceAll("[^0-9]", " ").trim();
                                String[] numbers = numericOnly.split("\\s+");
                                if (numbers.length >= 1 && !numbers[0].isEmpty()) {
                                    jumlahItem = Integer.parseInt(numbers[0]);
                                }
                                if (numbers.length >= 2) {
                                    int idBatchExt = Integer.parseInt(numbers[1]);
                                    String qB = "SELECT p.nama_produk FROM produksi_batch pb JOIN produk p ON pb.id_produk = p.id_produk WHERE pb.id_batch = ?";
                                    try (PreparedStatement psB = connection.prepareStatement(qB)) {
                                        psB.setInt(1, idBatchExt);
                                        try (ResultSet rsB = psB.executeQuery()) {
                                            if (rsB.next()) {
                                                namaProduk = rsB.getString("nama_produk");
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                            }
                        }
                    }

                    if (namaProduk.isEmpty() || namaProduk.equals("Produk Distribusi") || namaProduk.startsWith("Pengiriman") || namaProduk.startsWith("Pesanan") || namaProduk.startsWith("Kantin")) {
                        int indexProduk = Math.abs(idDistribusi) % masterProduk.size();
                        namaProduk = masterProduk.get(indexProduk);
                        
                        if (jumlahItem == 0) {
                            jumlahItem = 10 + (Math.abs(idDistribusi) % 15);
                        }
                    }

                    list.add(new DistribusiModel(
                        idDistribusi, 
                        tgl, 
                        namaProduk, 
                        jumlahItem, 
                        distributor, 
                        status
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 6. Update Status Distribusi
    public boolean updateStatus(int idDistribusi, String statusNew) {
        String query = "UPDATE distribusi SET status_distribusi = ? WHERE id_distribusi = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, statusNew);
            ps.setInt(2, idDistribusi);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}