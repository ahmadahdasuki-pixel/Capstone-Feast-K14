package capstone_feast.controller;

import capstone_feast.dao.ResepDao;
import capstone_feast.model.DetailResep;
import capstone_feast.model.Produk;
import capstone_feast.model.BahanBaku; 
import capstone_feast.view.Login;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ResepController {
    private capstone_feast.view.Resep frame;
    private ResepDao dao;
    private int selectedIdDetailResep = -1;

    public ResepController(capstone_feast.view.Resep frame) {
        this.frame = frame;
        this.dao = new ResepDao();
        loadComboProduk();
        initController();
        loadTableResepBySelectedProduk();
    }

    private void loadComboProduk() {
        if (frame.getCbProduk() != null) {
            frame.getCbProduk().removeAllItems();
            List<Produk> listProduk = dao.getAllProdukForCombo();
            for (Produk p : listProduk) {
                frame.getCbProduk().addItem(p);
            }
        }
    }

    private void initController() {
        if (frame.getBtnTambah() != null) {
            frame.getBtnTambah().addActionListener(e -> tambahResep());
        }

        if (frame.getBtnUbah() != null) {
            frame.getBtnUbah().addActionListener(e -> ubahTakaranResep());
        }

        if (frame.getBtnHapus() != null) {
            frame.getBtnHapus().addActionListener(e -> hapusResep());
        }

        if (frame.getCbProduk() != null) {
            frame.getCbProduk().addActionListener(e -> loadTableResepBySelectedProduk());
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

        if (frame.getTabelResep() != null) {
            frame.getTabelResep().addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = frame.getTabelResep().getSelectedRow();
                    if (row != -1) {
                        try {
                            selectedIdDetailResep = Integer.parseInt(frame.getTabelResep().getValueAt(row, 0).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public void loadTableResepBySelectedProduk() {
        DefaultTableModel model = (DefaultTableModel) frame.getTabelResep().getModel();
        model.setRowCount(0);

        Object selectedObj = frame.getCbProduk().getSelectedItem();
        if (selectedObj instanceof Produk) {
            Produk selectedProduk = (Produk) selectedObj;
            List<DetailResep> list = dao.getResepByProduk(selectedProduk.getIdProduk());

            for (DetailResep dr : list) {
                model.addRow(new Object[]{
                    dr.getIdDetailResep(),
                    dr.getIdBahan(),
                    dr.getNamaBahan(),
                    dr.getJumlahBahan(),
                    dr.getSatuan()   
                });
            }
        }
    }

    private void tambahResep() {
        Object selectedObj = frame.getCbProduk().getSelectedItem();
        if (!(selectedObj instanceof Produk)) {
            JOptionPane.showMessageDialog(frame, "Pilih produk terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Produk selectedProduk = (Produk) selectedObj;

        List<ResepDao.BahanItem> listBahan = dao.getListBahanItem(); 
        if (listBahan == null || listBahan.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Data bahan baku kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JComboBox<ResepDao.BahanItem> cmbBahanDialog = new JComboBox<>();
        for (ResepDao.BahanItem item : listBahan) {
            cmbBahanDialog.addItem(item);
        }

        JTextField txtJumlah = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.add(new JLabel("Pilih Bahan Baku:"));
        panel.add(cmbBahanDialog);
        panel.add(new JLabel("Jumlah Bahan:"));
        panel.add(txtJumlah);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Tambah Komposisi Resep: " + selectedProduk.getNamaProduk(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                double jumlah = Double.parseDouble(txtJumlah.getText().trim());
                if (jumlah <= 0) {
                    JOptionPane.showMessageDialog(frame, "Jumlah bahan harus lebih dari 0!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                ResepDao.BahanItem selectedBahan = (ResepDao.BahanItem) cmbBahanDialog.getSelectedItem();
                
                boolean sukses = dao.insertDetailResep(selectedProduk.getIdProduk(), selectedBahan.getIdBahan(), jumlah);
                
                if (sukses) {
                    JOptionPane.showMessageDialog(frame, "Berhasil menambahkan bahan ke resep!");
                    loadTableResepBySelectedProduk();
                } else {
                    JOptionPane.showMessageDialog(frame, "Gagal menyimpan ke database.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Jumlah harus berupa angka yang valid!", "Format Salah", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ubahTakaranResep() {
        if (selectedIdDetailResep == -1) {
            JOptionPane.showMessageDialog(frame, "Pilih baris resep pada tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField txtJumlahBaru = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Jumlah Takaran Baru:"));
        panel.add(txtJumlahBaru);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Ubah Takaran Resep", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                double jumlahBaru = Double.parseDouble(txtJumlahBaru.getText().trim());
                if (jumlahBaru <= 0) {
                    JOptionPane.showMessageDialog(frame, "Jumlah takaran harus lebih dari 0!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean sukses = dao.updateTakaranResep(selectedIdDetailResep, jumlahBaru);
                
                if (sukses) {
                    JOptionPane.showMessageDialog(frame, "Takaran resep berhasil diubah!");
                    loadTableResepBySelectedProduk();
                    selectedIdDetailResep = -1;
                } else {
                    JOptionPane.showMessageDialog(frame, "Gagal mengubah takaran resep.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Takaran harus berupa angka yang valid!", "Format Salah", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void hapusResep() {
        if (selectedIdDetailResep == -1) {
            JOptionPane.showMessageDialog(frame, "Pilih baris resep pada tabel yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(frame, "Yakin ingin menghapus bahan dari resep ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.deleteResep(selectedIdDetailResep)) {
                JOptionPane.showMessageDialog(frame, "Resep berhasil dihapus!");
                loadTableResepBySelectedProduk();
                selectedIdDetailResep = -1;
            } else {
                JOptionPane.showMessageDialog(frame, "Gagal menghapus resep.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}