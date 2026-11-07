package capstone_feast.config;

import java.sql.Connection;

public class TesKoneksi {
    public static void main(String[] args) {
        System.out.println("Mencoba menghubungkan ke database...");
        
        Connection conn = DatabaseConnection.getConnection();
        
        if (conn != null) {
            System.out.println("SUKSES: Koneksi ke database MySQL berhasil terhubung dengan baik!");
        } else {
            System.out.println("GAGAL: Periksa kembali XAMPP, nama database, username, atau password Anda.");
        }
    }
}