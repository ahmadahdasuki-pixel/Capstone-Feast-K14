package capstone_feast.controller;

import capstone_feast.dao.DistribusiDao;
import capstone_feast.model.ComboItem;
import capstone_feast.model.DistribusiModel;
import capstone_feast.view.Distribusi; 
import capstone_feast.view.Login;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Component;
import java.util.List;

public class DistribusiController {
    private Distribusi frame;
    private DistribusiDao dao;
    private int selectedIdDistribusi = -1; 
    public DistribusiController(Distribusi frame) {
        this.frame = frame;
        this.dao = new DistribusiDao();
        initController();
        loadAllData(); 
    }

    private void initController() {
        frame.getBtnSimpan().addActionListener(e -> prosesSimpanDistribusi());

        frame.getBtnRefresh().addActionListener(e -> {
            if (frame.getCmbFilterStatus() != null) {
                frame.getCmbFilterStatus().setSelectedIndex(0); 
            }
            loadAllData();
        });

        frame.getBtnUbahStatus().addActionListener(e -> prosesUbahStatus());

        if (frame.getCmbFilterStatus() != null) {
            frame.getCmbFilterStatus().removeAllItems();
            frame.getCmbFilterStatus().addItem("Semua");
            frame.getCmbFilterStatus().addItem("Diproses");
            frame.getCmbFilterStatus().addItem("Dikirim");
            frame.getCmbFilterStatus().addItem("Selesai");
            frame.getCmbFilterStatus().addItem("Batal");

            frame.getCmbFilterStatus().addActionListener(e -> loadTabelRiwayat());
        }

        setupTableColorRenderer();

        frame.getTabelRiwayat().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = frame.getTabelRiwayat().getSelectedRow();
                if (row != -1) {
                    
                }
            }
        });

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
    
    private void loadAllData() {
        loadComboBatch();
        loadComboDistributor();
        loadTabelRiwayat();
        resetForm();
    }

    private void loadComboBatch() {
        JComboBox cmb = frame.getCmbProduk();
        if (cmb != null) {
            cmb.removeAllItems();
            List<ComboItem> list = dao.getListBatchAktif();
            for (ComboItem item : list) {
                cmb.addItem(item);
            }
        }
    }

    private void loadComboDistributor() {
        JComboBox cmb = frame.getCmbDistributor();
        if (cmb != null) {
            cmb.removeAllItems();
            List<ComboItem> list = dao.getListDistributor();
            for (ComboItem item : list) {
                cmb.addItem(item);
            }
        }
    }

    private void loadTabelRiwayat() {
        DefaultTableModel model = (DefaultTableModel) frame.getTabelRiwayat().getModel();
        model.setRowCount(0); 

        String statusFilter = "Semua";
        if (frame.getCmbFilterStatus() != null && frame.getCmbFilterStatus().getSelectedItem() != null) {
            statusFilter = frame.getCmbFilterStatus().getSelectedItem().toString();
        }

        List<DistribusiModel> list = dao.getRiwayatByStatusAndSort(statusFilter, "DESC");
        
        for (DistribusiModel d : list) {
            model.addRow(new Object[]{
                d.getTglPengiriman(),
                d.getNamaProdukBatch(),
                d.getJumlah(),
                d.getNamaDistributor(),
                d.getStatus()
            });
        }
    }

    private void setupTableColorRenderer() {
        int statusColumnIndex = 4; 

        frame.getTabelRiwayat().setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    Object statusObj = table.getValueAt(row, statusColumnIndex);
                    if (statusObj != null) {
                        String status = statusObj.toString().trim().toLowerCase();
                        
                        if (status.contains("selesai")) {
                            c.setBackground(new Color(220, 255, 220)); 
                            c.setForeground(new Color(0, 100, 0));
                        } else if (status.contains("batal")) {
                            c.setBackground(new Color(255, 220, 220)); 
                            c.setForeground(new Color(139, 0, 0));
                        } else if (status.contains("proses")) {
                            c.setBackground(new Color(255, 255, 200)); 
                            c.setForeground(new Color(150, 100, 0));
                        } else if (status.contains("kirim")) {
                            c.setBackground(new Color(220, 240, 255)); 
                            c.setForeground(new Color(0, 0, 139));
                        } else {
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                        }
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                } else {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }
                return c;
            }
        });
    }

    private void resetForm() {
        if (frame.getTxtJumlah() != null) frame.getTxtJumlah().setText("");
        if (frame.getTxtTanggal() != null) frame.getTxtTanggal().setText(""); 
        if (frame.getCmbProduk() != null) frame.getCmbProduk().setSelectedIndex(-1);
        if (frame.getCmbDistributor() != null) frame.getCmbDistributor().setSelectedIndex(-1);
        selectedIdDistribusi = -1;
    }

    private void prosesSimpanDistribusi() {
        ComboItem selectedBatch = (ComboItem) frame.getCmbProduk().getSelectedItem();
        ComboItem selectedDist = (ComboItem) frame.getCmbDistributor().getSelectedItem();
        String jumlahStr = frame.getTxtJumlah().getText().trim();
        String tglKirimInput = frame.getTxtTanggal().getText().trim();

        if (selectedBatch == null || selectedDist == null || jumlahStr.isEmpty() || tglKirimInput.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Semua data wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validasi & Format Tanggal
        String tglKirim;
        try {
            String cleanTgl = tglKirimInput.replace("/", "-");
            java.time.format.DateTimeFormatter inputFormatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
            java.time.format.DateTimeFormatter dbFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");

            java.time.LocalDate parsedDate = java.time.LocalDate.parse(cleanTgl, inputFormatter);
            tglKirim = parsedDate.format(dbFormatter);
        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(
                frame, 
                "Format tanggal distribusi salah! Gunakan format: DD-MM-YYYY (Contoh: 05-11-2026)", 
                "Format Tanggal Salah", 
                JOptionPane.ERROR_MESSAGE
            );
            return; 
        }

        int jumlahMinta;
        try {
            jumlahMinta = Integer.parseInt(jumlahStr);
            if (jumlahMinta <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Jumlah harus berupa angka positif!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Cek Ketersediaan Stok
        int idBatch = selectedBatch.getId();
        int stokTersedia = dao.getSisaStokBatch(idBatch);

        if (stokTersedia < jumlahMinta) {
            JOptionPane.showMessageDialog(frame, 
                "Stok tidak mencukupi!\nPermintaan: " + jumlahMinta + "\nTersedia: " + stokTersedia, 
                "Stok Kurang", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Proses Simpan & Update Stok
        int idDistributor = selectedDist.getId();
        boolean sukses = dao.simpanDistribusi(idBatch, idDistributor, jumlahMinta, tglKirim);

        if (sukses) {
            JOptionPane.showMessageDialog(frame, "Data distribusi berhasil disimpan dan stok batch dikurangi.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadAllData(); 
        } else {
            JOptionPane.showMessageDialog(frame, "Gagal menyimpan data ke database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prosesUbahStatus() {
        int row = frame.getTabelRiwayat().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(frame, "Pilih data pada tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String statusFilter = "Semua";
        if (frame.getCmbFilterStatus() != null && frame.getCmbFilterStatus().getSelectedItem() != null) {
            statusFilter = frame.getCmbFilterStatus().getSelectedItem().toString();
        }

        List<DistribusiModel> currentList = dao.getRiwayatByStatusAndSort(statusFilter, "DESC");
        if (row >= currentList.size()) return;

        selectedIdDistribusi = currentList.get(row).getIdDistribusi();
        String statusSaatIni = currentList.get(row).getStatus();

        if (statusSaatIni.equalsIgnoreCase("Selesai") || statusSaatIni.equalsIgnoreCase("Batal")) {
            JOptionPane.showMessageDialog(frame, "Status sudah final, tidak bisa diubah.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Pilihan status yang tersedia
        Object[] options = {"Diproses", "Dikirim", "Selesai", "Batal"};
        int n = JOptionPane.showOptionDialog(
            frame,
            "Ubah status distribusi menjadi?",
            "Ubah Status",
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (n == JOptionPane.CLOSED_OPTION || n == -1) {
            selectedIdDistribusi = -1;
            return; 
        }

        // Menentukan status baru berdasarkan tombol pilihan yang diklik user
        String statusNew = "";
        switch (n) {
            case 0:
                statusNew = "diproses";
                break;
            case 1:
                statusNew = "dikirim";
                break;
            case 2:
                statusNew = "selesai";
                break;
            case 3:
                statusNew = "batal";
                break;
            default:
                return; 
        }

        boolean sukses = dao.updateStatus(selectedIdDistribusi, statusNew);
        if (sukses) {
            JOptionPane.showMessageDialog(frame, "Status berhasil diperbarui.");
            loadTabelRiwayat();
            selectedIdDistribusi = -1;
        } else {
            JOptionPane.showMessageDialog(frame, "Gagal memperbarui status.");
        }
    }
}