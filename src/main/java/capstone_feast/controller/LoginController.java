package capstone_feast.controller;

import capstone_feast.config.UserSession;
import capstone_feast.dao.LoginDao;
import capstone_feast.model.Pengguna;
import capstone_feast.view.DashboardAdmin;
import capstone_feast.view.DashboardStaff;
import capstone_feast.view.Login;
import javax.swing.JOptionPane;

public class LoginController {
    private Login frame;
    private LoginDao loginDao;

    public LoginController(Login frame) {
        this.frame = frame;
        this.loginDao = new LoginDao();
        initController();
    }

    private void initController() {
        frame.getBtnLogin().addActionListener(e -> prosesLogin());

        frame.getChkLihatPassword().addActionListener(e -> {
            if (frame.getChkLihatPassword().isSelected()) {
                frame.getTxtPassword().setEchoChar((char) 0);
            } else {
                frame.getTxtPassword().setEchoChar('•'); 
            }
        });
    }

    public void prosesLogin() {
        String username = frame.getTxtUsername().getText().trim();
        String password = new String(frame.getTxtPassword().getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Username dan Password tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pengguna user = loginDao.login(username, password);

        if (user != null) {
            // Validasi Cek Apakah Status Akun Nonaktif
            String statusAkun = user.getStatusAkun(); 
            if (statusAkun != null && statusAkun.equalsIgnoreCase("Nonaktif")) {
                JOptionPane.showMessageDialog(
                    frame, 
                    "Login Gagal! Akun Anda berstatus NONAKTIF. Silakan hubungi Administrator.", 
                    "Akses Ditolak", 
                    JOptionPane.ERROR_MESSAGE
                );
                return; 
            }


            String role = user.getRole(); 
            
            String namaUser = user.getNamaPengguna(); 
            UserSession.setNamaUser(namaUser);
            UserSession.setRole(role);

            JOptionPane.showMessageDialog(frame, "Login Berhasil sebagai " + role + "!");
            frame.dispose();

            if ("Admin".equalsIgnoreCase(role)) {
                new DashboardAdmin().setVisible(true); 
            } else if ("Staff".equalsIgnoreCase(role)) {
                new DashboardStaff().setVisible(true); 
            } else {
                JOptionPane.showMessageDialog(null, "Role pengguna tidak dikenal!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(frame, "Login Gagal! Periksa kembali username atau password Anda.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}