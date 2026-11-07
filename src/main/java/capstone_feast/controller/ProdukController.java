package capstone_feast.controller;

import capstone_feast.dao.ProdukDao;
import capstone_feast.model.Produk; 
import capstone_feast.view.Login;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProdukController {
    private capstone_feast.view.Produk frame;
    private ProdukDao dao;
    private int selectedIdProduk = -1; 

    public ProdukController(capstone_feast.view.Produk frame) {
        this.frame = frame;
        this.dao = new ProdukDao();
        initController();
        loadTable();
    }

    private void initController() {
        if (frame.getBtnTambah() != null) {
            frame.getBtnTambah().addActionListener(e -> tambahProduk());
        }

        if (frame.getBtnUbah() != null) {
            frame.getBtnUbah().addActionListener(e -> prosesUbahProduk());
        }

        if (frame.getBtnHapus() != null) {
            frame.getBtnHapus().addActionListener(e -> prosesNonaktifkanProduk());
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
        if (frame.getTabelProduk() != null) {
            frame.getTabelProduk().addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = frame.getTabelProduk().getSelectedRow();
                    if (row != -1) {
                        try {
                            selectedIdProduk = Integer.parseInt(frame.getTabelProduk().getValueAt(row, 0).toString());
                            frame.getNamaProdukField().setText(frame.getTabelProduk().getValueAt(row, 1).toString());
                            frame.getSatuanField().setText(frame.getTabelProduk().getValueAt(row, 2).toString());
                            frame.getHargaJualField().setText(frame.getTabelProduk().getValueAt(row, 3).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public void loadTable() {
        List<Produk> list = dao.getAllProduk();
        DefaultTableModel model = (DefaultTableModel) frame.getTabelProduk().getModel();
        model.setRowCount(0);

        for (Produk p : list) {
            model.addRow(new Object[]{
                p.getIdProduk(),
                p.getNamaProduk(),
                p.getSatuanProduk(),
                p.getHargaJual(),
                p.getStatusProduk()
            });
        }
    }

    // Validasi harga jual: wajib angka dan minimal Rp 5.000
    private boolean validasiHarga(String hargaStr) {
        try {
            double harga = Double.parseDouble(hargaStr);
            if (harga < 0) {
                JOptionPane.showMessageDialog(frame, "Harga jual tidak boleh bernilai negatif!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (harga < 5000) {
                JOptionPane.showMessageDialog(frame, "Harga jual tidak boleh di bawah Rp 5.000, batasan minimal adalah Rp 5.000!", "Peringatan Validasi Harga", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Kolom 'Harga Jual' wajib diisi dengan angka!", "Error Validasi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void tambahProduk() {
        String nama = frame.getNamaProdukField().getText().trim();
        String satuan = frame.getSatuanField().getText().trim();
        String hargaStr = frame.getHargaJualField().getText().trim();

        if (nama.isEmpty() || satuan.isEmpty() || hargaStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Semua kolom pada form wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validasiHarga(hargaStr)) {
            return;
        }

        double harga = Double.parseDouble(hargaStr);

        // Cek apakah produk dengan nama yang sama sudah terdaftar di database
        Produk existing = dao.getProdukByNama(nama);
        if (existing != null) {
            JOptionPane.showMessageDialog(
                frame, 
                "Produk dengan nama '" + nama + "' sudah terdaftar di database!\nSilakan pilih baris pada tabel dan gunakan tombol Ubah untuk memperbarui data.", 
                "Peringatan Duplikasi Data", 
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Produk baru = new Produk(0, 1, nama, satuan, harga, "Aktif");
        if (dao.insert(baru)) {
            JOptionPane.showMessageDialog(frame, "Produk baru berhasil ditambahkan!");
            loadTable();
            resetForm();
        } else {
            JOptionPane.showMessageDialog(frame, "Gagal menambahkan produk.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Proses Ubah Produk / Mengaktifkan Kembali Produk jika statusnya Nonaktif
    private void prosesUbahProduk() {
        if (selectedIdProduk == -1) {
            JOptionPane.showMessageDialog(frame, "Silakan pilih salah satu baris produk pada tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = frame.getTabelProduk().getSelectedRow();
        if (selectedRow != -1) {
            Object statusObj = frame.getTabelProduk().getValueAt(selectedRow, 4); 
            String statusProduk = statusObj != null ? statusObj.toString() : "";
            
            if (statusProduk.equalsIgnoreCase("Nonaktif") || statusProduk.equalsIgnoreCase("tidak aktif")) {
                int confirmAktif = JOptionPane.showConfirmDialog(
                    frame, 
                    "Produk ini berstatus NONAKTIF.\nApakah Anda ingin mengaktifkan kembali produk ini?", 
                    "Konfirmasi Aktivasi", 
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirmAktif == JOptionPane.YES_OPTION) {
                    if (dao.aktifkanProduk(selectedIdProduk)) {
                        JOptionPane.showMessageDialog(frame, "Produk berhasil diaktifkan kembali!");
                        loadTable();
                        resetForm();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Gagal mengaktifkan produk.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                return;
            }
        }

        String nama = frame.getNamaProdukField().getText().trim();
        String satuan = frame.getSatuanField().getText().trim();
        String hargaStr = frame.getHargaJualField().getText().trim();

        if (nama.isEmpty() || satuan.isEmpty() || hargaStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Semua kolom wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validasiHarga(hargaStr)) {
            return;
        }

        double harga = Double.parseDouble(hargaStr);

        Produk ubah = new Produk(selectedIdProduk, 1, nama, satuan, harga, "Aktif");
        if (dao.update(ubah)) {
            JOptionPane.showMessageDialog(frame, "Data produk berhasil diubah!");
            loadTable();
            resetForm();
        } else {
            JOptionPane.showMessageDialog(frame, "Gagal mengubah data produk.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Proses Nonaktifkan Produk (Hapus Produk)
    private void prosesNonaktifkanProduk() {
        if (selectedIdProduk == -1) {
            JOptionPane.showMessageDialog(frame, "Silakan pilih salah satu baris produk pada tabel yang ingin dinonaktifkan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Apakah Anda yakin ingin menonaktifkan produk ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(selectedIdProduk)) {
                JOptionPane.showMessageDialog(frame, "Status produk berhasil diubah menjadi Nonaktif!");
                loadTable();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(frame, "Gagal menonaktifkan produk.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resetForm() {
        frame.getNamaProdukField().setText("");
        frame.getSatuanField().setText("");
        frame.getHargaJualField().setText("");
        selectedIdProduk = -1;
    }
}