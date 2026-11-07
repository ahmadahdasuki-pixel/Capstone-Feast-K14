package capstone_feast.controller;

import capstone_feast.config.UserSession;
import capstone_feast.dao.DashboardStaffDao;
import capstone_feast.dao.ProduksiDao;
import capstone_feast.model.ProduksiModel;
import capstone_feast.view.DashboardStaff;
import capstone_feast.view.Login;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DashboardStaffController {
    private DashboardStaff frame;
    private DashboardStaffDao dao;
    private ProduksiDao produksiDao; 

    public DashboardStaffController(DashboardStaff frame) {
        this.frame = frame;
        this.dao = new DashboardStaffDao();
        this.produksiDao = new ProduksiDao();
        
        initController();
        loadDashboardData();
        loadCmbFilterStatus();
        loadTableBatchByStatus("Semua"); 
    }

    private void initController() {
        if (frame.getBtnLogout() != null) {
            frame.getBtnLogout().addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(frame, "Yakin Ingin Keluar?", "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    new Login().setVisible(true);
                    frame.dispose();
                }
            });
        }

        JComboBox cmbFilter = frame.getCmbStatus(); 
        if (cmbFilter != null) {
            cmbFilter.addActionListener(e -> {
                String selectedStatus = (String) cmbFilter.getSelectedItem();
                if (selectedStatus != null) {
                    loadTableBatchByStatus(selectedStatus);
                }
            });
        }
    }

    private void loadCmbFilterStatus() {
        JComboBox cmbFilter = frame.getCmbStatus();
        if (cmbFilter != null) {
            cmbFilter.removeAllItems();
            cmbFilter.addItem("Semua");
            cmbFilter.addItem("Selesai");
            cmbFilter.addItem("Di Proses");
            cmbFilter.addItem("Gagal");
            cmbFilter.addItem("Dibuang"); 
        }
    }

    private void loadTableBatchByStatus(String status) {
        JTable table = frame.getTabelBatchTerbaru();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        List<ProduksiModel> list = produksiDao.getDataByStatusSortedByFEFO(status);

        for (ProduksiModel p : list) {
            model.addRow(new Object[]{
                p.getNamaProduk(),
                p.getNomorBatch(),
                p.getJumlahProduksi(),
                p.getTanggalKadaluarsa(),
                p.getStatusBatch()
            });
        }
    }

    public void loadDashboardData() {
        String namaLogin = UserSession.getNamaUser();
        if (namaLogin == null || namaLogin.isEmpty()) {
            namaLogin = "Staff";
        }
        frame.getLblSelamatDatang().setText("Selamat Datang Staff, " + namaLogin + "....");

        // 1. Muat Statistik Bahan Baku
        Map<String, Integer> bb = dao.getStatistikBahanBaku();
        frame.getLblTotalBahanBaku().setText(String.valueOf(bb.get("total")));
        frame.getLblInfoBahanGabung().setText(bb.get("tersedia") + " Tersedia | " + bb.get("minimum") + " Minimum | " + bb.get("nonaktif") + " Nonaktif");

        // 2. Muat Statistik Produk
        Map<String, Integer> pr = dao.getStatistikProduk();
        frame.getLblTotalProduk().setText(String.valueOf(pr.get("total")));
        frame.getLblInfoProdukGabung().setText(pr.get("aktif") + " Aktif | " + pr.get("nonaktif") + " Nonaktif");

        // 3. Muat Statistik Batch
        Map<String, Integer> bt = dao.getStatistikBatch();
        frame.getLblTotalBatch().setText(String.valueOf(bt.get("total")));
        frame.getLblInfoBatchGabung().setText(bt.get("selesai") + " Selesai | " + bt.get("gagal") + " Gagal");

        // 4. Muat Statistik Distribusi
        Map<String, Integer> ds = dao.getStatistikDistribusi();
        frame.getLblTotalDistribusi().setText(String.valueOf(ds.get("total")));
        frame.getLblInfoDistribusiGabung().setText(ds.get("selesai") + " Selesai | " + ds.get("proses") + " Diproses");

        // 5. Renderer Warna Indikator Status pada Tabel Batch
        JTable table = frame.getTabelBatchTerbaru();
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 4 && value != null) {
                    String status = value.toString().toLowerCase();
                    if (status.contains("selesai") || status.contains("aktif")) {
                        c.setForeground(new Color(0, 150, 0)); 
                    } else if (status.contains("habis") || status.contains("gagal") || status.contains("dibuang")) {
                        c.setForeground(new Color(200, 0, 0)); 
                    } else {
                        c.setForeground(new Color(200, 150, 0)); 
                    }
                } else if (!isSelected) {
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });
    }
}