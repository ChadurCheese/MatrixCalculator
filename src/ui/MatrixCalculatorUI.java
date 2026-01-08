package ui;

import core.Matrix;
import core.MatrixOperations;
import core.MatrixException;

import javax.swing.*;
import java.awt.*;

public class MatrixCalculatorUI extends JFrame {
    private MatrixInputPanel matrixAPanel;
    private MatrixInputPanel matrixBPanel;
    private OperationPanel operationPanel;
    private MatrixDisplayPanel resultPanel;
    
    public MatrixCalculatorUI() {
        initUI();
    }
    
    private void initUI() {
        setTitle("Matrix Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Set application icon (optional)
        try {
            ImageIcon icon = new ImageIcon("src\\assets\\matrixCalc.jpg");
            setIconImage(icon.getImage());
        } catch (Exception e) {
            // Use default icon if custom icon not found
        }
        
        // Create menu bar
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        
        // Create panels
        matrixAPanel = new MatrixInputPanel("Matrix A");
        matrixBPanel = new MatrixInputPanel("Matrix B");
        operationPanel = new OperationPanel();
        resultPanel = new MatrixDisplayPanel();
        
        // Set operation panel listener
        operationPanel.setOperationListener(new OperationPanel.OperationListener() {
            @Override
            public void onOperationSelected(String operation, double scalar, int exponent) {
                performOperation(operation, scalar, exponent);
            }
            
            @Override
            public void onClear() {
                clearAll();
            }
        });
        
        // Create main content panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        
        // Matrix A panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.4;
        gbc.weighty = 1.0;
        mainPanel.add(matrixAPanel, gbc);
        
        // Operation panel
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 0.2;
        gbc.weighty = 0.3;
        mainPanel.add(operationPanel, gbc);
        
        // Result panel
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weighty = 0.7;
        mainPanel.add(resultPanel, gbc);
        
        // Matrix B panel
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.4;
        gbc.weighty = 1.0;
        mainPanel.add(matrixBPanel, gbc);
        
        // Add status bar
        JLabel statusBar = new JLabel(" Ready");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        
        // Add components to frame
        add(mainPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        
        // Set window properties
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(e -> clearAll());
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(newItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Examples menu
        JMenu examplesMenu = new JMenu("Examples");
        
        JMenuItem identity2x2 = new JMenuItem("2x2 Identity Matrix");
        identity2x2.addActionListener(e -> loadExample("identity2x2"));
        
        JMenuItem random3x3 = new JMenuItem("Random 3x3 Matrix");
        random3x3.addActionListener(e -> loadExample("random3x3"));
        
        JMenuItem systemEq = new JMenuItem("System of Equations");
        systemEq.addActionListener(e -> loadExample("system"));
        
        examplesMenu.add(identity2x2);
        examplesMenu.add(random3x3);
        examplesMenu.add(systemEq);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        
        JMenuItem helpItem = new JMenuItem("Help Contents");
        helpItem.addActionListener(e -> showHelpDialog());
        
        helpMenu.add(helpItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);
        
        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(examplesMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    private void performOperation(String operation, double scalar, int exponent) {
        try {
            Matrix A = new Matrix(matrixAPanel.getMatrixData());
            Matrix B = null;
            
            if (!operation.contains("Transpose") && 
                !operation.contains("Inverse") && 
                !operation.contains("Determinant") &&
                !operation.contains("Trace")) {
                B = new Matrix(matrixBPanel.getMatrixData());
            }
            
            Matrix result = null;
            double doubleResult = 0;
            String resultStr = "";
            
            switch (operation) {
                case "Addition (A + B)":
                    result = MatrixOperations.add(A, B);
                    resultStr = result.toString();
                    break;
                    
                case "Subtraction (A - B)":
                    result = MatrixOperations.subtract(A, B);
                    resultStr = result.toString();
                    break;
                    
                case "Multiplication (A × B)":
                    result = MatrixOperations.multiply(A, B);
                    resultStr = result.toString();
                    break;
                    
                case "Scalar Multiplication":
                    result = MatrixOperations.scalarMultiply(A, scalar);
                    resultStr = "Scalar: " + scalar + "\n\nResult:\n" + result.toString();
                    break;
                    
                case "Transpose (Aᵀ)":
                    result = MatrixOperations.transpose(A);
                    resultStr = "Original:\n" + A.toString() + "\nTranspose:\n" + result.toString();
                    break;
                    
                case "Inverse (A⁻¹)":
                    result = MatrixOperations.inverse(A);
                    resultStr = "Original:\n" + A.toString() + 
                               "\nInverse:\n" + result.toString() +
                               "\n\nVerification (A × A⁻¹):\n" + 
                               MatrixOperations.multiply(A, result).toString();
                    break;
                    
                case "Dot Product":
                    doubleResult = MatrixOperations.dotProduct(A, B);
                    resultStr = String.format("Dot Product: %.4f", doubleResult);
                    break;
                    
                case "Determinant (det(A))":
                    doubleResult = MatrixOperations.determinant(A);
                    resultStr = String.format("Determinant: %.4f", doubleResult);
                    break;
                    
                case "Power (Aⁿ)":
                    result = MatrixOperations.power(A, exponent);
                    resultStr = "Exponent: " + exponent + "\n\nResult:\n" + result.toString();
                    break;
                    
                case "Trace":
                    doubleResult = MatrixOperations.trace(A);
                    resultStr = String.format("Trace: %.4f", doubleResult);
                    break;
            }
            
            resultPanel.displayResult(operation, resultStr);
            
        } catch (MatrixException e) {
            resultPanel.displayError(operation, e.getMessage());
        } catch (Exception e) {
            resultPanel.displayError(operation, "Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void clearAll() {
        matrixAPanel.setMatrixData(new double[][]{{0, 0}, {0, 0}});
        matrixBPanel.setMatrixData(new double[][]{{0, 0}, {0, 0}});
        resultPanel.clear();
    }
    
    private void loadExample(String example) {
        switch (example) {
            case "identity2x2":
                matrixAPanel.setMatrixData(new double[][]{{1, 0}, {0, 1}});
                matrixBPanel.setMatrixData(new double[][]{{2, 1}, {1, 2}});
                break;
                
            case "random3x3":
                matrixAPanel.setMatrixData(Matrix.random(3, 3, -5, 5).getData());
                matrixBPanel.setMatrixData(Matrix.random(3, 3, -5, 5).getData());
                break;
                
            case "system":
                // Example: 2x + y = 5, x + 2y = 4
                matrixAPanel.setMatrixData(new double[][]{{2, 1}, {1, 2}});
                matrixBPanel.setMatrixData(new double[][]{{5}, {4}});
                break;
        }
    }
    
    private void showAboutDialog() {
        String aboutText = "Matrix Calculator\n" +
                          "Version 1.0\n\n" +
                          "A comprehensive matrix operations calculator\n" +
                          "Supporting addition, multiplication, transpose,\n" +
                          "inverse, determinant, and more!\n\n" +
                          "Created with Java Swing";
        
        JOptionPane.showMessageDialog(this, aboutText, "About Matrix Calculator", 
                                     JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showHelpDialog() {
        String helpText = "Matrix Calculator Help\n\n" +
                         "1. Enter matrix values in the input tables\n" +
                         "2. Use the resize controls to change matrix dimensions\n" +
                         "3. Select an operation from the dropdown\n" +
                         "4. Click Calculate to perform the operation\n" +
                         "5. Results appear in the Result panel\n\n" +
                         "Supported Operations:\n" +
                         "- Addition & Subtraction\n" +
                         "- Matrix Multiplication\n" +
                         "- Scalar Multiplication\n" +
                         "- Transpose\n" +
                         "- Inverse\n" +
                         "- Dot Product\n" +
                         "- Determinant\n" +
                         "- Matrix Power\n" +
                         "- Trace";
        
        JTextArea textArea = new JTextArea(helpText);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Help", 
                                     JOptionPane.PLAIN_MESSAGE);
    }
}