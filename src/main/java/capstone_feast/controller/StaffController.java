package capstone_feast.controller;

import capstone_feast.dao.StaffDao;
import capstone_feast.model.Staff;
import capstone_feast.view.Login;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class StaffController {
    private capstone_feast.view.KelolaAkunStaff frame;
    private StaffDao dao;
    private int selectedIdStaff = -1;

    public StaffController(capstone_feast.view.KelolaAkunStaff frame) {
        this.frame = frame;
        this.dao = new StaffDao();
        initController();
        loadTable("");
    }

    private void initController() {
        if (frame.getBtnRefresh() != null) {
            frame.getBtnRefresh().addActionListener(e -> {
                frame.getTxtPencarian().setText("");
                loadTable("");
            });
        }

        if (frame.getTxtPencarian() != null) {
            frame.getTxtPencarian().addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    loadTable(frame.getTxtPencarian().getText().trim());
                }
            });
        }

        // Tombol Tambah Akun memanggil method tambahStaff() yang membuka form Register
        if (frame.getBtnTambah() != null) {
            frame.getBtnTambah().addActionListener(e -> tambahStaff());
        }

        if (frame.getBtnUbah() != null) {
            frame.getBtnUbah().addActionListener(e -> ubahStaff());
        }

        if (frame.getBtnHapus() != null) {
            frame.getBtnHapus().addActionListener(e -> nonaktifkanStaff());
        }
        
        if (frame.getBtnLogout() != null) {
            frame.getBtnLogout().addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                    frame, 
                    "Yakin Ingin Keluar?", 
                    "Konfirmasi Logout", 
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    new Login().setVisible(true);
                    frame.dispose();
                }
            });
        }

        if (frame.getTabelStaff() != null) {
            frame.getTabelStaff().addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = frame.getTabelStaff().getSelectedRow();
                    if (row != -1) {
                        try {
                            // Karena kolom ID mungkin disembunyikan atau berada di baris data objek, 
                            // kita ambil ID dari database berdasarkan baris yang diklik atau pastikan indeksnya benar.
                            // Jika list dao di-load urut, kita bisa ambil berdasarkan list atau jika ID disimpan di kolom tersembunyi.
                            // Mari kita amankan pengambilan ID berdasarkan data staff di list DAO atau pencarian list:
                            String usernameSelected = frame.getTabelStaff().getValueAt(row, 1).toString(); // Kolom 1 adalah Username
                            Staff st = dao.getStaffList("").stream()
                                          .filter(s -> s.getUsername().equals(usernameSelected))
                                          .findFirst().orElse(null);
                            if (st != null) {
                                selectedIdStaff = st.getId();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public void loadTable(String keyword) {
        List<Staff> list = dao.getStaffList(keyword);
        DefaultTableModel model = (DefaultTableModel) frame.getTabelStaff().getModel();
        model.setRowCount(0);

        for (Staff s : list) {
            model.addRow(new Object[]{
                s.getNamaPengguna(), // Kolom 0: Nama Staff
                s.getUsername(),     // Kolom 1: Username
                s.getPassword(),     // Kolom 2: Password (Ditampilkan agar admin bisa melihat)
                s.getStatusAkun(),   // Kolom 3: Status Akun
                s.getNomorTelepon(), // Kolom 4: No Telepon
                s.getShift()         // Kolom 5: Shift Kerja
            });
        }
    }

    // Validasi No Telepon (Harus angka dan minimal 8 digit)
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

    private void tambahStaff() {
        capstone_feast.view.Register formRegister = new capstone_feast.view.Register();
        formRegister.setLocationRelativeTo(frame); 
        formRegister.setVisible(true);
    }

    private void ubahStaff() {
        if (selectedIdStaff == -1) {
            JOptionPane.showMessageDialog(frame, "Pilih baris staff pada tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = frame.getTabelStaff().getSelectedRow();
        String statusAkun = frame.getTabelStaff().getValueAt(selectedRow, 3).toString(); // Kolom status akun di index 3

        if (statusAkun.equalsIgnoreCase("Nonaktif")) {
            int confirm = JOptionPane.showConfirmDialog(
                frame, 
                "Akun ini berstatus NONAKTIF.\nApakah Anda ingin mengaktifkan kembali?", 
                "Konfirmasi Aktivasi", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.aktifkanStaff(selectedIdStaff)) {
                    JOptionPane.showMessageDialog(frame, "Akun staff berhasil diaktifkan kembali!");
                    loadTable("");
                    selectedIdStaff = -1;
                } else {
                    JOptionPane.showMessageDialog(frame, "Gagal mengaktifkan akun.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            return;
        }

        Staff currentStaff = dao.getStaffList("").stream().filter(s -> s.getId() == selectedIdStaff).findFirst().orElse(null);
        if (currentStaff == null) return;

        JTextField tfNama = new JTextField(currentStaff.getNamaPengguna());
        JTextField tfUser = new JTextField(currentStaff.getUsername());
        JPasswordField pfPass = new JPasswordField(currentStaff.getPassword());
        JTextField tfShift = new JTextField(currentStaff.getShift());
        JTextField tfTelp = new JTextField(currentStaff.getNomorTelepon());

        Object[] message = {
            "Nama Staff:", tfNama,
            "Username:", tfUser,
            "Password:", pfPass,
            "Shift Kerja:", tfShift,
            "No Telepon:", tfTelp
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Ubah Data Akun Staff", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nama = tfNama.getText().trim();
            String user = tfUser.getText().trim();
            String pass = new String(pfPass.getPassword()).trim();
            String shift = tfShift.getText().trim();
            String telp = tfTelp.getText().trim();

            if (nama.isEmpty() || user.isEmpty() || pass.isEmpty() || shift.isEmpty() || telp.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Semua kolom wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!validasiNoTelepon(telp)) {
                return;
            }

            Staff updateStaff = new Staff(selectedIdStaff, nama, user, pass, "Aktif", telp, shift);
            if (dao.update(updateStaff)) {
                JOptionPane.showMessageDialog(frame, "Data akun staff berhasil diperbarui!");
                loadTable("");
                selectedIdStaff = -1;
            } else {
                JOptionPane.showMessageDialog(frame, "Gagal memperbarui data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void nonaktifkanStaff() {
        if (selectedIdStaff == -1) {
            JOptionPane.showMessageDialog(frame, "Pilih baris staff pada tabel yang ingin dinonaktifkan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Apakah Anda yakin ingin menonaktifkan akun staff ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(selectedIdStaff)) {
                JOptionPane.showMessageDialog(frame, "Akun staff berhasil dinonaktifkan!");
                loadTable("");
                selectedIdStaff = -1;
            } else {
                JOptionPane.showMessageDialog(frame, "Gagal menonaktifkan akun.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}