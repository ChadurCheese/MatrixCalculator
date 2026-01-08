package ui;

import javax.swing.*;
import java.awt.*;

public class OperationPanel extends JPanel {
    private JComboBox<String> operationCombo;
    private JButton calculateButton;
    private JButton clearButton;
    private JSpinner scalarSpinner;
    private JSpinner exponentSpinner;
    private JLabel scalarLabel;
    private JLabel exponentLabel;
    
    private OperationListener listener;
    
    public interface OperationListener {
        void onOperationSelected(String operation, double scalar, int exponent);
        void onClear();
    }
    
    public OperationPanel() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Operation selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        add(new JLabel("Select Operation:"), gbc);
        
        String[] operations = {
            "Addition (A + B)",
            "Subtraction (A - B)",
            "Multiplication (A × B)",
            "Scalar Multiplication",
            "Transpose (Aᵀ)",
            "Inverse (A⁻¹)",
            "Dot Product",
            "Determinant (det(A))",
            "Power (Aⁿ)",
            "Trace"
        };
        
        operationCombo = new JComboBox<>(operations);
        operationCombo.addActionListener(e -> updateControls());
        gbc.gridy = 1;
        add(operationCombo, gbc);
        
        // Scalar input
        scalarLabel = new JLabel("Scalar:");
        scalarLabel.setVisible(false);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(scalarLabel, gbc);
        
        scalarSpinner = new JSpinner(new SpinnerNumberModel(1.0, -100.0, 100.0, 0.5));
        scalarSpinner.setVisible(false);
        gbc.gridx = 1;
        add(scalarSpinner, gbc);
        
        // Exponent input
        exponentLabel = new JLabel("Exponent:");
        exponentLabel.setVisible(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(exponentLabel, gbc);
        
        exponentSpinner = new JSpinner(new SpinnerNumberModel(2, -10, 10, 1));
        exponentSpinner.setVisible(false);
        gbc.gridx = 1;
        add(exponentSpinner, gbc);
        
        // Buttons
        calculateButton = new JButton("Calculate");
        calculateButton.setBackground(new Color(70, 130, 180));
        calculateButton.setForeground(Color.BLACK);
        calculateButton.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(calculateButton, gbc);
        
        clearButton = new JButton("Reset");
        clearButton.setBackground(new Color(220, 20, 60));
        clearButton.setForeground(Color.BLACK);
        gbc.gridy = 5;
        add(clearButton, gbc);
        
        // Button actions
        calculateButton.addActionListener(e -> performCalculation());
        clearButton.addActionListener(e -> {
            if (listener != null) {
                listener.onClear();
            }
        });
    }
    
    private void updateControls() {
        String selected = (String) operationCombo.getSelectedItem();
        if (selected == null) return;
        
        boolean showScalar = selected.contains("Scalar");
        boolean showExponent = selected.contains("Power");
        
        scalarLabel.setVisible(showScalar);
        scalarSpinner.setVisible(showScalar);
        exponentLabel.setVisible(showExponent);
        exponentSpinner.setVisible(showExponent);
        
        revalidate();
        repaint();
    }
    
    private void performCalculation() {
        if (listener != null) {
            String operation = (String) operationCombo.getSelectedItem();
            double scalar = (double) scalarSpinner.getValue();
            int exponent = (int) exponentSpinner.getValue();
            listener.onOperationSelected(operation, scalar, exponent);
        }
    }
    
    public void setOperationListener(OperationListener listener) {
        this.listener = listener;
    }
    
    public String getSelectedOperation() {
        return (String) operationCombo.getSelectedItem();
    }
}