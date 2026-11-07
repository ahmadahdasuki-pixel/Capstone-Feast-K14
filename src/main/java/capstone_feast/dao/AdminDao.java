package capstone_feast.dao;

import capstone_feast.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDao {
    private Connection connection;

    public AdminDao() {
        this.connection = DatabaseConnection.getConnection();
    }

    public int getTotalBahanBaku() {
        int total = 0;
        String query = "SELECT COUNT(*) AS total FROM bahan_baku";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                total = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

}