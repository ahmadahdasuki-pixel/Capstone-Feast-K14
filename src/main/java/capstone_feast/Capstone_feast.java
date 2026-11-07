package capstone_feast;

import capstone_feast.view.Login;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Capstone_feast {

    public static void main(String[] args) {
        com.formdev.flatlaf.FlatLightLaf.setup();
        UIManager.put("Button.arc", 12);
        UIManager.put("Component.arc", 12);

        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}