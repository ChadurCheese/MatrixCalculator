import ui.MatrixCalculatorUI;
import javax.swing.*;
import java.awt.*;

// import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
// import com.formdev.flatlaf.FlatIntelliJLaf;

public class Main {
    public static void main(String[] args) {
        
        // Different FlatLaf themes
        FlatLightLaf.setup();
        // FlatIntelliJLaf.setup();
        // FlatDarculaLaf.setup();

        customizeUI();

        // system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Run the application
        SwingUtilities.invokeLater(() -> {
            MatrixCalculatorUI calculator = new MatrixCalculatorUI();
            calculator.setVisible(true);
        });
    }

    private static void customizeUI() {
        // buttons
        UIManager.put("Button.arc", 10);
        UIManager.put("Component.arc", 8);
        UIManager.put("Button.background", new Color(66, 133, 244)); // Google Blue
        UIManager.put("Button.foreground", Color.BLACK);
        
        // table colors
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.gridColor", new Color(240, 240, 240));
        
        // text fields 
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.borderColor", new Color(218, 220, 224));
    }
}