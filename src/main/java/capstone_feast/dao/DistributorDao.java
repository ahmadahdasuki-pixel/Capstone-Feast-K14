package capstone_feast.dao;

import capstone_feast.config.DatabaseConnection;
import capstone_feast.model.Distributor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DistributorDao {
    private Connection connection;

    public DistributorDao() {
        this.connection = DatabaseConnection.getConnection();
    }

    public List<Distributor> getAllDistributor() {
        List<Distributor> list = new ArrayList<>();
        String query = "SELECT * FROM distributor ORDER BY id_distributor ASC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Distributor d = new Distributor(
                    rs.getInt("id_distributor"),
                    rs.getInt("id_admin"),
                    rs.getString("nama_distributor"),
                    rs.getString("alamat"),
                    rs.getString("nomor_telepon_distributor"), 
                    rs.getString("status_distributor")       
                );
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cek duplikasi berdasarkan nama distributor
    public Distributor getDistributorByNama(String namaDistributor) {
        String query = "SELECT * FROM distributor WHERE LOWER(nama_distributor) = LOWER(?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, namaDistributor.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Distributor(
                    rs.getInt("id_distributor"),
                    rs.getInt("id_admin"),
                    rs.getString("nama_distributor"),
                    rs.getString("alamat"),
                    rs.getString("nomor_telepon_distributor"),
                    rs.getString("status_distributor")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(Distributor distributor) {
        String query = "INSERT INTO distributor (id_admin, nama_distributor, alamat, nomor_telepon_distributor, status_distributor) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, distributor.getIdAdmin());
            ps.setString(2, distributor.getNamaDistributor());
            ps.setString(3, distributor.getAlamat());
            ps.setString(4, distributor.getNoTelepon());
            ps.setString(5, distributor.getStatusDistributor());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Distributor distributor) {
        String query = "UPDATE distributor SET nama_distributor = ?, alamat = ?, nomor_telepon_distributor = ?, status_distributor = ? WHERE id_distributor = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, distributor.getNamaDistributor());
            ps.setString(2, distributor.getAlamat());
            ps.setString(3, distributor.getNoTelepon());
            ps.setString(4, distributor.getStatusDistributor());
            ps.setInt(5, distributor.getIdDistributor());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // delete (mengubah status menjadi Nonaktif)
    public boolean delete(int idDistributor) {
        String query = "UPDATE distributor SET status_distributor = 'Nonaktif' WHERE id_distributor = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idDistributor);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mengaktifkan kembali distributor yang berstatus Nonaktif
    public boolean aktifkanDistributor(int idDistributor) {
        String query = "UPDATE distributor SET status_distributor = 'Aktif' WHERE id_distributor = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idDistributor);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}