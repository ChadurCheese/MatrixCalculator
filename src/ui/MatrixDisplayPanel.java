package ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import core.Matrix;

import java.awt.*;

public class MatrixDisplayPanel extends JPanel {
    private JTextArea resultArea;
    private JLabel operationLabel;
    
    public MatrixDisplayPanel() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(0, 10));  // Add vertical spacing
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Result",
            TitledBorder.LEFT,
            TitledBorder.TOP
        ));
        
        operationLabel = new JLabel("Operation: None");
        operationLabel.setFont(new Font("Arial", Font.BOLD, 14));
        operationLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        add(operationLabel, BorderLayout.NORTH);
        
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setBackground(new Color(250, 250, 250));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        
        // Make scroll pane fill available space
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Add to a container with proper constraints
        JPanel resultContainer = new JPanel(new BorderLayout());
        resultContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        resultContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(resultContainer, BorderLayout.CENTER);
    }
    
    public void displayResult(String operation, String result) {
        operationLabel.setText("Operation: " + operation);
        
        // Try to format matrix-like output better
        if (result.contains("[") && result.contains("]")) {
            // It looks like a matrix - add some formatting
            String formatted = formatMatrixOutput(result);
            resultArea.setText(formatted);
        } else {
            resultArea.setText(result);
        }
        
        resultArea.setForeground(Color.BLACK);
    }

    private String formatMatrixOutput(String matrixText) {
        // Simple formatting to align matrix brackets
        String[] lines = matrixText.split("\n");
        StringBuilder formatted = new StringBuilder();
        
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("[") && line.endsWith("]")) {
                // Format matrix row
                String numbers = line.substring(1, line.length() - 1);
                String[] nums = numbers.split(",");
                
                formatted.append("[");
                for (int i = 0; i < nums.length; i++) {
                    String num = nums[i].trim();
                    try {
                        double d = Double.parseDouble(num);
                        // Format with consistent width
                        if (Math.abs(d - Math.round(d)) < 0.000001) {
                            formatted.append(String.format("%8d", Math.round(d)));
                        } else {
                            formatted.append(String.format("%10.4f", d));
                        }
                    } catch (NumberFormatException e) {
                        formatted.append(String.format("%10s", num));
                    }
                    
                    if (i < nums.length - 1) {
                        formatted.append("  ");
                    }
                }
                formatted.append("]\n");
            } else {
                formatted.append(line).append("\n");
            }
        }
        
        return formatted.toString();
    }
    
    public void clear() {
        operationLabel.setText("Operation: None");
        resultArea.setText("");
    }
    
    public void displayError(String operation, String error) {
        operationLabel.setText("Operation: " + operation + " - ERROR");
        resultArea.setText("❌ Error: " + error + 
                        "\n\nPlease check:\n" +
                        "1. Matrix dimensions are compatible\n" +
                        "2. Matrix is not singular (for inverse)\n" +
                        "3. Input values are valid numbers");
        resultArea.setForeground(new Color(220, 20, 60));  // Red color for errors
    }
}