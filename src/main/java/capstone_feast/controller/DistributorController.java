package capstone_feast.controller;

import capstone_feast.dao.DistributorDao;
import capstone_feast.model.Distributor;
import capstone_feast.view.Login;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class DistributorController {
    private capstone_feast.view.Distributor frame;
    private DistributorDao dao;
    private int selectedIdDistributor = -1;

    public DistributorController(capstone_feast.view.Distributor frame) {
        this.frame = frame;
        this.dao = new DistributorDao();
        initController();
        loadTable();
    }

    private void initController() {
        if (frame.getBtnTambah() != null) {
            frame.getBtnTambah().addActionListener(e -> tambahDistributor());
        }

        if (frame.getBtnUbah() != null) {
            frame.getBtnUbah().addActionListener(e -> prosesUbahDistributor());
        }

        if (frame.getBtnHapus() != null) {
            frame.getBtnHapus().addActionListener(e -> prosesNonaktifkanDistributor());
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

        if (frame.getTabelDistributor() != null) {
            frame.getTabelDistributor().addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = frame.getTabelDistributor().getSelectedRow();
                    if (row != -1) {
                        try {
                            selectedIdDistributor = Integer.parseInt(frame.getTabelDistributor().getValueAt(row, 0).toString());
                            frame.getNamaDistributorField().setText(frame.getTabelDistributor().getValueAt(row, 1).toString());
                            frame.getAlamatField().setText(frame.getTabelDistributor().getValueAt(row, 2).toString());
                            frame.getNoTeleponField().setText(frame.getTabelDistributor().getValueAt(row, 3).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public void loadTable() {
        List<Distributor> list = dao.getAllDistributor();
        DefaultTableModel model = (DefaultTableModel) frame.getTabelDistributor().getModel();
        model.setRowCount(0);

        for (Distributor d : list) {
            model.addRow(new Object[]{
                d.getIdDistributor(),
                d.getNamaDistributor(),
                d.getAlamat(),
                d.getNoTelepon(),
                d.getStatusDistributor()
            });
        }
    }

    // Validasi No Telepon (Harus angka dan minimal 8 digit)
    private boolean validasiNoTelepon(String noTelp) {
        if (!noTelp.matches("\\d+")) {
            JOptionPane.showMessageDialog(frame, "Nomor telepon harus berupa angka (integer)!", "Error Validasi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (noTelp.length() < 8) {
            JOptionPane.showMessageDialog(frame, "Nomor telepon minimal harus terdiri dari 8 digit!", "Peringatan Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void tambahDistributor() {
        String nama = frame.getNamaDistributorField().getText().trim();
        String alamat = frame.getAlamatField().getText().trim();
        String noTelp = frame.getNoTeleponField().getText().trim();

        if (nama.isEmpty() || alamat.isEmpty() || noTelp.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Semua kolom pada form wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validasiNoTelepon(noTelp)) {
            return;
        }

        // Cek duplikasi nama distributor
        Distributor existing = dao.getDistributorByNama(nama);
        if (existing != null) {
            JOptionPane.showMessageDialog(
                frame, 
                "Distributor dengan nama '" + nama + "' sudah terdaftar!\nGunakan tombol Ubah untuk memperbarui data.", 
                "Peringatan Duplikasi", 
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Distributor baru = new Distributor(0, nama, alamat, noTelp, "Aktif");
        if (dao.insert(baru)) {
            JOptionPane.showMessageDialog(frame, "Distributor berhasil ditambahkan!");
            loadTable();
            resetForm();
        } else {
            JOptionPane.showMessageDialog(frame, "Gagal menambahkan distributor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prosesUbahDistributor() {
        if (selectedIdDistributor == -1) {
            JOptionPane.showMessageDialog(frame, "Silakan pilih salah satu baris distributor pada tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = frame.getTabelDistributor().getSelectedRow();
        if (selectedRow != -1) {
            Object statusObj = frame.getTabelDistributor().getValueAt(selectedRow, 4); 
            String statusDistributor = statusObj != null ? statusObj.toString() : "";

            if (statusDistributor.equalsIgnoreCase("Nonaktif") || statusDistributor.equalsIgnoreCase("tidak aktif")) {
                int confirmAktif = JOptionPane.showConfirmDialog(
                    frame, 
                    "Distributor ini berstatus NONAKTIF.\nApakah Anda ingin mengaktifkan kembali?", 
                    "Konfirmasi Aktivasi", 
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirmAktif == JOptionPane.YES_OPTION) {
                    if (dao.aktifkanDistributor(selectedIdDistributor)) {
                        JOptionPane.showMessageDialog(frame, "Distributor berhasil diaktifkan kembali!");
                        loadTable();
                        resetForm();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Gagal mengaktifkan distributor.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                return;
            }
        }

        String nama = frame.getNamaDistributorField().getText().trim();
        String alamat = frame.getAlamatField().getText().trim();
        String noTelp = frame.getNoTeleponField().getText().trim();

        if (nama.isEmpty() || alamat.isEmpty() || noTelp.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Semua kolom wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validasiNoTelepon(noTelp)) {
            return;
        }

        Distributor ubah = new Distributor(selectedIdDistributor, nama, alamat, noTelp, "Aktif");
        if (dao.update(ubah)) {
            JOptionPane.showMessageDialog(frame, "Data distributor berhasil diubah!");
            loadTable();
            resetForm();
        } else {
            JOptionPane.showMessageDialog(frame, "Gagal mengubah data distributor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prosesNonaktifkanDistributor() {
        if (selectedIdDistributor == -1) {
            JOptionPane.showMessageDialog(frame, "Silakan pilih salah satu baris distributor pada tabel yang ingin dinonaktifkan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Apakah Anda yakin ingin menonaktifkan distributor ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(selectedIdDistributor)) {
                JOptionPane.showMessageDialog(frame, "Status distributor berhasil diubah menjadi Nonaktif!");
                loadTable();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(frame, "Gagal menonaktifkan distributor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resetForm() {
        frame.getNamaDistributorField().setText("");
        frame.getAlamatField().setText("");
        frame.getNoTeleponField().setText("");
        selectedIdDistributor = -1;
    }
}