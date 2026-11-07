package capstone_feast.dao;

import capstone_feast.config.DatabaseConnection;
import capstone_feast.model.Admin;
import capstone_feast.model.Pengguna;
import capstone_feast.model.Staff;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDao {
    private Connection connection;

    public LoginDao() {
        this.connection = DatabaseConnection.getConnection();
    }

    public Pengguna login(String username, String password) {
        Pengguna pengguna = null;
        String query = "SELECT * FROM pengguna WHERE username = ? AND password = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt(1); 
                    String namaPengguna = resultSet.getString("nama_pengguna");
                    String dbUsername = resultSet.getString("username");
                    
                    String statusAkun = resultSet.getString("status_akun"); 

                    if (dbUsername.toLowerCase().contains("admin")) {
                        pengguna = new Admin(id, namaPengguna, dbUsername, statusAkun, null, null);
                    } else {
                        pengguna = new Staff(id, namaPengguna, dbUsername, statusAkun, null, "Reguler");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pengguna;
    }
}