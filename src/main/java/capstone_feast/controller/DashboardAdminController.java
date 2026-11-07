package capstone_feast.controller;

import capstone_feast.config.UserSession; 
import capstone_feast.dao.DashboardAdminDao;
import capstone_feast.view.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class DashboardAdminController {
    private DashboardAdmin frame;
    private DashboardAdminDao dao;

    public DashboardAdminController(DashboardAdmin frame) {
        this.frame = frame;
        this.dao = new DashboardAdminDao();
        initController();
        loadDashboardData();
    }

    private void initController() {
        // 1. Tombol Menu Dashboard
        if (frame.getBtnDashboard() != null) {
            frame.getBtnDashboard().addActionListener(e -> {
            });
        }

        // 2. Tombol Menu Bahan Baku
        if (frame.getBtnBahan() != null) {
            frame.getBtnBahan().addActionListener(e -> {
                new BahanBaku().setVisible(true);
                frame.dispose();
            });
        }

        // 3. Tombol Menu Produk
        if (frame.getBtnProduk() != null) {
            frame.getBtnProduk().addActionListener(e -> {
                new Produk().setVisible(true);
                frame.dispose();
            });
        }

        // 4. Tombol Menu Resep
        if (frame.getBtnResep() != null) {
            frame.getBtnResep().addActionListener(e -> {
                new Resep().setVisible(true);
                frame.dispose();
            });
        }

        // 5. Tombol Menu Distributor
        if (frame.getBtnDistributor() != null) {
            frame.getBtnDistributor().addActionListener(e -> {
                new Distributor().setVisible(true);
                frame.dispose();
            });
        }

        // 6. Tombol Menu Kelola Akun Staff
        if (frame.getBtnKelolaAkun() != null) {
            frame.getBtnKelolaAkun().addActionListener(e -> {
                new KelolaAkunStaff().setVisible(true);
                frame.dispose();
            });
        }

        // 7. Tombol Logout
        if (frame.getBtnLogout() != null) {
            frame.getBtnLogout().addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                    frame, 
                    "Apakah Anda yakin ingin keluar (Logout)?", 
                    "Konfirmasi Logout", 
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    new Login().setVisible(true);
                    frame.dispose();
                }
            });
        }
    }

    private void loadDashboardData() {
        try {
            if (frame != null) {
                String namaAdmin = UserSession.getNamaUser();
                if (namaAdmin != null && !namaAdmin.isEmpty()) {
                    if (frame.getLblSelamatDatang() != null) {
                        frame.getLblSelamatDatang().setText("Selamat Datang, " + namaAdmin);
                    }
                }

                loadCardStatistikRealTime();

                frame.isiPilihanStatus();
                
                if (frame.getCmbStatus() != null) {
                    for (java.awt.event.ActionListener al : frame.getCmbStatus().getActionListeners()) {
                        frame.getCmbStatus().removeActionListener(al);
                    }
                    
                    frame.getCmbStatus().addActionListener(e -> {
                        String selectedStatus = (String) frame.getCmbStatus().getSelectedItem();
                        if (selectedStatus != null) {
                            loadTabelBatch(selectedStatus);
                        }
                    });
                }

                loadTabelBatch("Semua");
                aturWarnaTabelStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCardStatistikRealTime() {
        Map<String, Integer> bahanBaku = dao.getStatistikBahanBaku();
        Map<String, Integer> produk = dao.getStatistikProduk();
        Map<String, Integer> batch = dao.getStatistikBatch();
        Map<String, Integer> distribusi = dao.getStatistikDistribusi();

        // 1. Total Bahan Baku & Keterangannya
        if (frame.getLblTotalBahanBaku() != null) {
            frame.getLblTotalBahanBaku().setText(String.valueOf(bahanBaku.get("total")));
        }
        if (frame.getLblInfoBahanGabung() != null) {
            frame.getLblInfoBahanGabung().setText(bahanBaku.get("tersedia") + " Tersedia | " + bahanBaku.get("minimum") + " Minimum | " + bahanBaku.get("nonaktif") + " Nonaktif");
        }

        // 2. Total Produk & Keterangannya
        if (frame.getLblTotalProduk() != null) {
            frame.getLblTotalProduk().setText(String.valueOf(produk.get("total")));
        }
        if (frame.getLblInfoProdukGabung() != null) {
            frame.getLblInfoProdukGabung().setText(produk.get("aktif") + " Aktif | " + produk.get("nonaktif") + " Nonaktif");
        }

        // 3. Total Produksi Batch & Keterangannya
        if (frame.getLblTotalBatch() != null) {
            frame.getLblTotalBatch().setText(String.valueOf(batch.get("total")));
        }
        if (frame.getLblInfoBatchGabung() != null) {
            frame.getLblInfoBatchGabung().setText(batch.get("selesai") + " Selesai | " + batch.get("gagal") + " Gagal");
        }

        // 4. Total Distribusi & Keterangannya
        if (frame.getLblTotalDistribusi() != null) {
            frame.getLblTotalDistribusi().setText(String.valueOf(distribusi.get("total")));
        }
        if (frame.getLblInfoDistribusiGabung() != null) {
            frame.getLblInfoDistribusiGabung().setText(distribusi.get("selesai") + " Selesai | " + distribusi.get("proses") + " Diproses");
        }
    }

    public void loadTabelBatch(String statusFilter) {
        DefaultTableModel model = (DefaultTableModel) frame.getTabelBatchTerbaru().getModel();
        model.setRowCount(0); 

        List<Object[]> dataBatch = dao.getBatchTerbaru();

        for (Object[] row : dataBatch) {
            String statusBatch = (row[4] != null) ? row[4].toString().trim() : ""; 
            String filter = (statusFilter != null) ? statusFilter.trim() : "Semua";

            boolean isMatch = false;
            
            if (filter.equalsIgnoreCase("Semua")) {
                isMatch = true;
            } 
            else if (filter.equalsIgnoreCase("Selesai") && statusBatch.equalsIgnoreCase("Selesai")) {
                isMatch = true;
            } 
            else if (filter.equalsIgnoreCase("Di Proses") && (statusBatch.equalsIgnoreCase("Di Proses") || statusBatch.equalsIgnoreCase("Diproses"))) {
                isMatch = true;
            } 
            else if (filter.equalsIgnoreCase("Gagal") && statusBatch.equalsIgnoreCase("Gagal")) {
                isMatch = true;
            }
            else if (filter.equalsIgnoreCase("Dibuang") && statusBatch.equalsIgnoreCase("Dibuang")) {
                isMatch = true;
            }

            if (isMatch) {
                model.addRow(row);
            }
        }
    }

    private void aturWarnaTabelStatus() {
        JTable table = frame.getTabelBatchTerbaru();
        
        if (table.getColumnCount() > 4) {
            table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, 
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    
                    if (value != null) {
                        String status = value.toString().trim().toLowerCase();
                        
                        if (isSelected) {
                            c.setForeground(table.getSelectionForeground());
                            c.setBackground(table.getSelectionBackground());
                        } else {
                            c.setBackground(Color.WHITE);
                            
                            if (status.equals("selesai")) {
                                c.setForeground(new Color(0, 153, 0)); 
                            } else if (status.equals("di proses") || status.equals("diproses")) {
                                c.setForeground(new Color(204, 153, 0)); 
                            } else if (status.equals("gagal") || status.equals("dibuang")) {
                                c.setForeground(new Color(204, 0, 0)); 
                            } else {
                                c.setForeground(Color.BLACK);
                            }
                        }
                    }
                    return c;
                }
            });
        }
    }
}