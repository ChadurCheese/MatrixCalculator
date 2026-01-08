
import ui.MatrixCalculatorUI;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Set FlatLaf modern look and feel
        FlatLightLaf.setup();
        
        // Optional: Add some custom UI improvements
        customizeUI();
        
        // Run the application
        SwingUtilities.invokeLater(() -> {
            MatrixCalculatorUI calculator = new MatrixCalculatorUI();
            calculator.setVisible(true);
        });
    }
    
    private static void customizeUI() {
        // Make UI components more modern
        UIManager.put("Button.arc", 8);
        UIManager.put("Component.arc", 8);
        UIManager.put("TextComponent.arc", 6);
        
        // Better scroll bars
        UIManager.put("ScrollBar.width", 12);
        
        // Improve table appearance globally
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("Table.intercellSpacing", new Dimension(0, 1));
        
        // Better selection colors
        UIManager.put("Table.selectionBackground", new Color(66, 133, 244, 100));
        UIManager.put("Table.selectionForeground", Color.BLACK);
    }
}