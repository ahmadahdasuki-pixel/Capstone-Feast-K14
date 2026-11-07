package capstone_feast.controller;

import capstone_feast.dao.BahanBakuDao;
import capstone_feast.model.BahanBaku; 
import capstone_feast.view.Login;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BahanBakuController {
    private capstone_feast.view.BahanBaku frame; 
    private BahanBakuDao dao;
    private int selectedIdBahan = -1;

    public BahanBakuController(capstone_feast.view.BahanBaku frame) {
        this.frame = frame;
        this.dao = new BahanBakuDao();
        initController();
        loadTableBahanBaku();
    }

    private void initController() {
        if (frame.getBtnTambah() != null) {
            frame.getBtnTambah().addActionListener(e -> tambahAtauUpdateStokBahan());
        }

        if (frame.getBtnUbah() != null) {
            frame.getBtnUbah().addActionListener(e -> prosesUbahBahan());
        }

        if (frame.getBtnHapus() != null) {
            frame.getBtnHapus().addActionListener(e -> prosesNonaktifkanBahan());
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

        if (frame.getTabelBahan() != null) {
            frame.getTabelBahan().addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = frame.getTabelBahan().getSelectedRow();
                    if (row != -1) {
                        try {
                            selectedIdBahan = Integer.parseInt(frame.getTabelBahan().getValueAt(row, 0).toString());
                            frame.getNamaBahanField().setText(frame.getTabelBahan().getValueAt(row, 1).toString());
                            frame.getSatuanField().setText(frame.getTabelBahan().getValueAt(row, 2).toString());
                            frame.getStokSaatIniField().setText(frame.getTabelBahan().getValueAt(row, 3).toString());
                            frame.getStokMinimField().setText(frame.getTabelBahan().getValueAt(row, 4).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void loadTableBahanBaku() {
        List<BahanBaku> list = dao.getAllBahanBaku();
        DefaultTableModel model = (DefaultTableModel) frame.getTabelBahan().getModel();
        model.setRowCount(0);

        for (BahanBaku b : list) {
            model.addRow(new Object[]{
                b.getIdBahan(),
                b.getNamaBahan(),
                b.getSatuan(),
                b.getStokSaatIni(),
                b.getStokMinimum(),
                b.getStatusBahanBaku()
            });
        }
        
        aturWarnaTabelBahanBaku();
    }

    private void aturWarnaTabelBahanBaku() {
        JTable table = frame.getTabelBahan();
        if (table == null) return;

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                try {
                    Object valStokAktif = table.getValueAt(row, 3);
                    Object valStokMin = table.getValueAt(row, 4);
                    
                    if (valStokAktif != null && valStokMin != null) {
                        double stokAktif = Double.parseDouble(valStokAktif.toString());
                        double stokMin = Double.parseDouble(valStokMin.toString());
                        
                        if (isSelected) {
                            c.setBackground(table.getSelectionBackground());
                            c.setForeground(table.getSelectionForeground());
                        } else {
                            if (stokAktif <= stokMin) {
                                c.setBackground(new Color(255, 204, 204)); // Merah muda
                                c.setForeground(new Color(153, 0, 0));       //Teks merah gelap
                            } else {
                                c.setBackground(Color.WHITE);
                                c.setForeground(Color.BLACK);
                            }
                        }
                    }
                } catch (Exception e) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                
                return c;
            }
        });
    }

    private boolean validasiInputAngka(String stokStr, String minStr) {
        try {
            double stok = Double.parseDouble(stokStr);
            double min = Double.parseDouble(minStr);
            if (stok < 0 || min < 0) {
                JOptionPane.showMessageDialog(frame, "Nilai stok tidak boleh bernilai negatif!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Kolom 'Stok Saat Ini' dan 'Stok Minimum' wajib diisi dengan angka!", "Error Validasi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void tambahAtauUpdateStokBahan() {
        String nama = frame.getNamaBahanField().getText().trim();
        String satuan = frame.getSatuanField().getText().trim();
        String stokStr = frame.getStokSaatIniField().getText().trim();
        String minStr = frame.getStokMinimField().getText().trim();

        if (nama.isEmpty() || satuan.isEmpty() || stokStr.isEmpty() || minStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Semua kolom pada form wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validasiInputAngka(stokStr, minStr)) {
            return;
        }

        double stokBaru = Double.parseDouble(stokStr);
        double stokMin = Double.parseDouble(minStr);

        BahanBaku existing = dao.getBahanBakuByNama(nama);
        if (existing != null) {
            String statusBahan = existing.getStatusBahanBaku() != null ? existing.getStatusBahanBaku() : "";
            if (statusBahan.equalsIgnoreCase("Nonaktif")) {
                JOptionPane.showMessageDialog(
                    frame, 
                    "Bahan baku '" + nama + "' berstatus NONAKTIF.\nTidak dapat menambahkan stok pada bahan yang sudah nonaktif.", 
                    "Peringatan", 
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                frame, 
                "Bahan baku dengan nama '" + nama + "' sudah terdaftar.\nApakah Anda ingin menambahkan stoknya sebesar " + stokBaru + "?", 
                "Konfirmasi Penambahan Stok", 
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean sukses = dao.updateStokBahanBaku(existing.getIdBahan(), stokBaru);
                if (sukses) {
                    JOptionPane.showMessageDialog(frame, "Stok bahan baku berhasil ditambahkan!");
                    loadTableBahanBaku();
                    resetForm();
                } else {
                    JOptionPane.showMessageDialog(frame, "Gagal memperbarui stok.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            boolean sukses = dao.insertBahanBaku(nama, satuan, stokBaru, stokMin);
            if (sukses) {
                JOptionPane.showMessageDialog(frame, "Bahan baku baru berhasil ditambahkan!");
                loadTableBahanBaku();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(frame, "Gagal menambahkan bahan baku.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void prosesUbahBahan() {
        if (selectedIdBahan == -1) {
            JOptionPane.showMessageDialog(frame, "Silakan pilih salah satu baris bahan pada tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = frame.getTabelBahan().getSelectedRow();
        if (selectedRow != -1) {
            Object statusObj = frame.getTabelBahan().getValueAt(selectedRow, 5);
            String statusBahan = statusObj != null ? statusObj.toString() : "";
            
            if (statusBahan.equalsIgnoreCase("Nonaktif")) {
                int confirmAktif = JOptionPane.showConfirmDialog(
                    frame, 
                    "Bahan baku ini berstatus NONAKTIF.\nApakah Anda ingin mengaktifkan kembali bahan baku ini?", 
                    "Konfirmasi Aktivasi", 
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirmAktif == JOptionPane.YES_OPTION) {
                    boolean suksesAktif = dao.aktifkanBahanBaku(selectedIdBahan);
                    if (suksesAktif) {
                        JOptionPane.showMessageDialog(frame, "Bahan baku berhasil diaktifkan kembali!");
                        loadTableBahanBaku();
                        resetForm();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Gagal mengaktifkan bahan baku.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                return;
            }
        }

        String nama = frame.getNamaBahanField().getText().trim();
        String satuan = frame.getSatuanField().getText().trim();
        String stokStr = frame.getStokSaatIniField().getText().trim();
        String minStr = frame.getStokMinimField().getText().trim();

        if (nama.isEmpty() || satuan.isEmpty() || stokStr.isEmpty() || minStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Semua kolom wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validasiInputAngka(stokStr, minStr)) {
            return;
        }

        double stok = Double.parseDouble(stokStr);
        double min = Double.parseDouble(minStr);

        boolean sukses = dao.updateBahanBaku(selectedIdBahan, nama, satuan, stok, min);
        if (sukses) {
            JOptionPane.showMessageDialog(frame, "Data bahan baku berhasil diubah!");
            loadTableBahanBaku();
            resetForm();
        } else {
            JOptionPane.showMessageDialog(frame, "Gagal mengubah data bahan baku.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prosesNonaktifkanBahan() {
        if (selectedIdBahan == -1) {
            JOptionPane.showMessageDialog(frame, "Silakan pilih salah satu baris bahan pada tabel yang ingin dinonaktifkan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Apakah Anda yakin ingin menonaktifkan bahan baku ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean sukses = dao.nonaktifkanBahanBaku(selectedIdBahan);
            if (sukses) {
                JOptionPane.showMessageDialog(frame, "Bahan baku berhasil dinonaktifkan!");
                loadTableBahanBaku();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(frame, "Gagal menonaktifkan bahan baku.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void resetForm() {
        frame.getNamaBahanField().setText("");
        frame.getSatuanField().setText("");
        frame.getStokSaatIniField().setText("");
        frame.getStokMinimField().setText("");
        selectedIdBahan = -1;
    }
}