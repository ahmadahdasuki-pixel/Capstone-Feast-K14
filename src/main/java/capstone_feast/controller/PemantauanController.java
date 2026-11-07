package capstone_feast.controller;

import capstone_feast.dao.PemantauanDao;
import capstone_feast.model.PemantauanModel;
import capstone_feast.view.Login;
import capstone_feast.view.PemantauanStok;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Component;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PemantauanController {
    private PemantauanStok frame;
    private PemantauanDao dao;
    private int selectedIdBatch = -1;
    private String selectedStatusBatch = "";
    private List<PemantauanModel> currentBatchList;

    public PemantauanController(PemantauanStok frame) {
        this.frame = frame;
        this.dao = new PemantauanDao();
        initController();
        loadTabelBahanBaku();
        loadTabelBatchProduksi();
    }

    private void initController() {
        if (frame.getTabelStokBatch() != null) {
            frame.getTabelStokBatch().addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = frame.getTabelStokBatch().getSelectedRow();
                    if (row != -1 && currentBatchList != null && row < currentBatchList.size()) {
                        PemantauanModel selectedModel = currentBatchList.get(row);
                        selectedIdBatch = selectedModel.getIdBatch();
                        selectedStatusBatch = frame.getTabelStokBatch().getValueAt(row, 4).toString();
                    }
                }
            });
        }

        if (frame.getBtnBuangStok() != null) {
            frame.getBtnBuangStok().addActionListener(e -> prosesBuangStok());
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
    }
    
    
    
    public void loadTabelBahanBaku() {
        List<PemantauanModel> list = dao.getAllBahanBaku();
        DefaultTableModel model = (DefaultTableModel) frame.getTabelStokBahan().getModel();
        model.setRowCount(0);

        for (PemantauanModel b : list) {
            model.addRow(new Object[]{
                b.getNamaBahan(),
                b.getSatuanBahan(),
                b.getStokSaatIni(),
                b.getStokMinimum(),
                b.getStatusBahan()
            });
        }

        frame.getTabelStokBahan().setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Object statusObj = table.getValueAt(row, 4);
                if (statusObj != null && statusObj.toString().equalsIgnoreCase("Minimum")) {
                    c.setBackground(new Color(255, 230, 230));
                    c.setForeground(Color.RED);
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                    c.setForeground(isSelected ? table.getSelectionForeground() : Color.BLACK);
                }
                return c;
            }
        });
    }

    public void loadTabelBatchProduksi() {
        currentBatchList = dao.getAllBatchProduksi();
        DefaultTableModel model = (DefaultTableModel) frame.getTabelStokBatch().getModel();
        model.setRowCount(0);

        LocalDate hariIni = LocalDate.now();

        for (PemantauanModel p : currentBatchList) {
            String statusWaktu = "Aman";
            
            if (p.getStatusBatch() != null && p.getStatusBatch().startsWith("Dibuang")) {
                statusWaktu = p.getStatusBatch();
            } else {
                try {
                    LocalDate tglKadaluarsa = LocalDate.parse(p.getTglKadaluarsa());
                    long selisihHari = ChronoUnit.DAYS.between(hariIni, tglKadaluarsa);

                    if (selisihHari < 0) {
                        statusWaktu = "Sudah Kadaluarsa";
                    } else if (selisihHari <= 3) {
                        statusWaktu = "Mendekati Kadaluarsa (" + selisihHari + " hari lagi)";
                    } else {
                        statusWaktu = "Aman";
                    }
                } catch (Exception e) {
                    statusWaktu = p.getStatusBatch();
                }
            }

            model.addRow(new Object[]{
                p.getNamaProduk(),
                p.getTglProduksi(),
                p.getTglKadaluarsa(),
                p.getSisaStokBatch(),
                statusWaktu
            });
        }

        frame.getTabelStokBatch().setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                Object statusObj = table.getValueAt(row, 4);
                if (statusObj != null) {
                    String statusStr = statusObj.toString();
                    if (statusStr.contains("Sudah Kadaluarsa")) {
                        c.setBackground(new Color(255, 200, 200));
                        c.setForeground(Color.RED);
                    } else if (statusStr.contains("Mendekati Kadaluarsa")) {
                        c.setBackground(new Color(255, 255, 180));
                        c.setForeground(new Color(140, 100, 0));
                    } else if (statusStr.contains("Dibuang")) {
                        c.setBackground(new Color(220, 220, 220));
                        c.setForeground(Color.GRAY);
                    } else {
                        c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                        c.setForeground(isSelected ? table.getSelectionForeground() : Color.BLACK);
                    }
                }
                return c;
            }
        });
    }

    private void prosesBuangStok() {
        if (selectedIdBatch == -1) {
            JOptionPane.showMessageDialog(frame, "Silakan pilih/klik baris batch produk pada tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!selectedStatusBatch.contains("Sudah Kadaluarsa")) {
            JOptionPane.showMessageDialog(frame, "Batch produk ini belum kadaluarsa! Anda hanya dapat membuang stok batch yang berstatus 'Sudah Kadaluarsa'.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String alasan = JOptionPane.showInputDialog(frame, "Masukkan alasan pembuangan stok batch ini:", "Alasan Pembuangan Stok", JOptionPane.QUESTION_MESSAGE);

        if (alasan == null) {
            return;
        }

        if (alasan.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Alasan pembuangan wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Yakin ingin membuang stok dengan alasan: \"" + alasan + "\"?", "Konfirmasi Pembuangan Stok", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean sukses = dao.buangStokBatch(selectedIdBatch, alasan.trim());
            if (sukses) {
                JOptionPane.showMessageDialog(frame, "Stok batch berhasil dibuang dan dicatat.");
                loadTabelBatchProduksi();
                selectedIdBatch = -1;
            } else {
                JOptionPane.showMessageDialog(frame, "Gagal membuang stok ke database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}