package ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class MatrixDisplayPanel extends JPanel {
    private JTextArea resultArea;
    private JLabel operationLabel;
    
    public MatrixDisplayPanel() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Result",
            TitledBorder.LEFT,
            TitledBorder.TOP
        ));
        
        operationLabel = new JLabel("Operation: None");
        operationLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(operationLabel, BorderLayout.NORTH);
        
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void displayResult(String operation, String result) {
        operationLabel.setText("Operation: " + operation);
        resultArea.setText(result);
    }
    
    public void clear() {
        operationLabel.setText("Operation: None");
        resultArea.setText("");
    }
    
    public void displayError(String operation, String error) {
        operationLabel.setText("Operation: " + operation + " - ERROR");
        resultArea.setText("Error: " + error + "\n\nPlease check your input and try again.");
        resultArea.setForeground(Color.RED);
    }
}