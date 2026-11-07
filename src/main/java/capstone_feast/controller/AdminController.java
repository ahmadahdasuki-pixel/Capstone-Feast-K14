package capstone_feast.controller;

import capstone_feast.dao.DashboardAdminDao;
import capstone_feast.view.DashboardAdmin;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AdminController {
    private DashboardAdmin frame;
    private DashboardAdminDao dao;

    public AdminController(DashboardAdmin frame) {
        this.frame = frame;
        this.dao = new DashboardAdminDao();
        
        loadTabelBatch("Semua");
        
        if (frame.getCmbStatus() != null) {
            for (java.awt.event.ActionListener al : frame.getCmbStatus().getActionListeners()) {
                frame.getCmbStatus().removeActionListener(al);
            }
            
            frame.getCmbStatus().addActionListener(e -> {
                String statusPilihan = frame.getCmbStatus().getSelectedItem().toString();
                loadTabelBatch(statusPilihan);
            });
        }
    }

    public void loadTabelBatch(String statusFilter) {
        DefaultTableModel model = (DefaultTableModel) frame.getTabelBatchTerbaru().getModel();
        model.setRowCount(0);

        List<Object[]> dataBatch = dao.getBatchTerbaru();

        for (Object[] row : dataBatch) {
            String statusBatch = (String) row[4]; 

            if (statusFilter.equalsIgnoreCase("Semua") || 
                (statusBatch != null && statusBatch.equalsIgnoreCase(statusFilter))) {
                model.addRow(row);
            }
        }
    }
}