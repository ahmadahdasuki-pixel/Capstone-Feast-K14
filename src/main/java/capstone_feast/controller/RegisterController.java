package capstone_feast.controller;

import capstone_feast.dao.StaffDao;
import capstone_feast.model.Staff;
import capstone_feast.view.Login;
import capstone_feast.view.Register;
import javax.swing.JOptionPane;

public class RegisterController {
    private Register frame;
    private StaffDao dao;

    public RegisterController(Register frame) {
        this.frame = frame;
        this.dao = new StaffDao();
        initController();
    }

    private void initController() {
        if (frame.getCheckLihatPassword() != null) {
            frame.getCheckLihatPassword().addActionListener(e -> {
                if (frame.getCheckLihatPassword().isSelected()) {
                    frame.getTxtPassword().setEchoChar((char) 0);
                } else {
                    frame.getTxtPassword().setEchoChar('•');
                }
            });
        }

        if (frame.getBtnTambahkanAkun() != null) {
            frame.getBtnTambahkanAkun().addActionListener(e -> prosesRegister());
        }
    }

    private boolean validasiNoTelepon(String noTelp) {
        if (!noTelp.matches("\\d+")) {
            JOptionPane.showMessageDialog(frame, "Nomor telepon harus berupa angka!", "Error Validasi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (noTelp.length() < 8) {
            JOptionPane.showMessageDialog(frame, "Nomor telepon minimal harus terdiri dari 8 digit!", "Peringatan Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void prosesRegister() {
        String namaLengkap = frame.getTxtNama() != null ? frame.getTxtNama().getText().trim() : "";
        String noTelepon = frame.getTxtTelepon() != null ? frame.getTxtTelepon().getText().trim() : "";
        String username = frame.getTxtUsername().getText().trim();
        String password = new String(frame.getTxtPassword().getPassword());

        // 1. Validasi Field Kosong
        if (username.isEmpty() || password.isEmpty() || namaLengkap.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nama lengkap, username, dan password wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. VALIDASI PASSWORD: Harus tepat 6 karakter 
        if (password.length() != 6) {
            JOptionPane.showMessageDialog(
                frame, 
                "Password harus tepat 6 karakter!", 
                "Peringatan Validasi", 
                JOptionPane.WARNING_MESSAGE
            );
            return; 
        }

        // 3. Validasi Nomor Telepon jika diisi
        if (!noTelepon.isEmpty() && !validasiNoTelepon(noTelepon)) {
            return;
        }

        Staff staff = new Staff();
        staff.setNamaPengguna(namaLengkap);
        staff.setNamaPengguna(namaLengkap);
        staff.setNomorTelepon(noTelepon);
        staff.setUsername(username);
        staff.setPassword(password);
        staff.setStatusAkun("Aktif");
        staff.setShift("Pagi");

        boolean sukses = dao.insert(staff);

        if (sukses) {
            JOptionPane.showMessageDialog(frame, "Akun baru berhasil ditambahkan oleh Admin!");
          
            frame.dispose(); 
        } else {
            JOptionPane.showMessageDialog(
                frame, 
                "Registrasi Gagal: Username \"" + username + "\" sudah digunakan oleh akun lain. Silakan gunakan username yang berbeda!", 
                "Username Sudah Terdaftar", 
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}