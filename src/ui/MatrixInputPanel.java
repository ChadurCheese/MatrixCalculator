package ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
// import javax.swing.table.TableCellEditor;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class MatrixInputPanel extends JPanel {
    private JTable matrixTable;
    private DefaultTableModel tableModel;
    private JLabel dimensionsLabel;
    private JSpinner rowsSpinner;
    private JSpinner colsSpinner;
    private String matrixName;
    
    public MatrixInputPanel(String name) {
        this.matrixName = name;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            matrixName,
            TitledBorder.LEFT,
            TitledBorder.TOP
        ));
        
        // Dimension controls
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        controlPanel.add(new JLabel("Rows:"));
        rowsSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        controlPanel.add(rowsSpinner);
        
        controlPanel.add(new JLabel("Cols:"));
        colsSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        controlPanel.add(colsSpinner);
        
        JButton resizeButton = new JButton("Resize");
        resizeButton.addActionListener(e -> resizeMatrix());
        controlPanel.add(resizeButton);
        
        JButton randomButton = new JButton("Random Fill");
        randomButton.addActionListener(e -> fillRandom());
        controlPanel.add(randomButton);
        
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearMatrix());
        controlPanel.add(clearButton);
        
        add(controlPanel, BorderLayout.NORTH);
        
        // Matrix table
        // Matrix table with custom model for better number handling
    tableModel = new DefaultTableModel(2, 2) {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return Double.class;
        }
        
        @Override
        public void setValueAt(Object value, int row, int column) {
            // Handle string input and convert to Double
            if (value instanceof String) {
                String str = ((String) value).trim();
                if (str.isEmpty()) {
                    super.setValueAt(0.0, row, column);
                } else {
                    try {
                        // Remove leading zeros (fixes 0.01 issue)
                        if (str.startsWith("0") && str.length() > 1 && !str.startsWith("0.")) {
                            str = str.replaceFirst("^0+", "");
                            if (str.isEmpty()) str = "0";
                        }
                        double num = Double.parseDouble(str);
                        super.setValueAt(num, row, column);
                    } catch (NumberFormatException e) {
                        // Keep old value if invalid
                        super.setValueAt(getValueAt(row, column), row, column);
                    }
                }
            } else {
                super.setValueAt(value, row, column);
            }
        }
    };

    matrixTable = new JTable(tableModel);

    // ========== FIX 1: Single cell selection (not entire row/col) ==========
    matrixTable.setRowSelectionAllowed(false);
    matrixTable.setColumnSelectionAllowed(false);
    matrixTable.setCellSelectionEnabled(true);
    matrixTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

    // ========== FIX 2: Better cell highlighting ==========
    matrixTable.setSelectionBackground(new Color(66, 133, 244, 100));  // Semi-transparent blue
    matrixTable.setSelectionForeground(Color.BLACK);
    matrixTable.setRowHeight(30);

    // ========== FIX 3: Better table appearance ==========
    matrixTable.setShowGrid(true);
    matrixTable.setGridColor(new Color(230, 230, 230));
    matrixTable.setIntercellSpacing(new Dimension(0, 0));

    // ========== FIX 4: Center align and format numbers ==========
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, 
                isSelected, hasFocus, row, column);
            
            // Always center align
            setHorizontalAlignment(JLabel.CENTER);
            
            // Format numbers nicely - remove trailing .0
            if (value instanceof Number) {
                double num = ((Number) value).doubleValue();
                if (Math.abs(num - Math.round(num)) < 0.000001) {
                    // It's essentially an integer
                    setText(String.format("%d", Math.round(num)));
                } else {
                    // Show 2 decimal places max
                    setText(String.format("%.2f", num));
                }
            }
            
            // Alternating row colors (subtle)
            if (!isSelected) {
                if (row % 2 == 0) {
                    c.setBackground(Color.WHITE);
                } else {
                    c.setBackground(new Color(248, 249, 250));  // Very light gray
                }
            }
            
            // Custom border for focused cell
            if (hasFocus) {
                setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 2));
            } else {
                setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            }
            
            return c;
        }
    };

    // Apply the renderer to all columns
    for (int i = 0; i < matrixTable.getColumnCount(); i++) {
        matrixTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        matrixTable.getColumnModel().getColumn(i).setPreferredWidth(70);
    }

    // ========== FIX 5: Better table header ==========
    matrixTable.getTableHeader().setReorderingAllowed(false);
    matrixTable.getTableHeader().setBackground(new Color(248, 249, 250));
    matrixTable.getTableHeader().setForeground(new Color(60, 64, 67));
    matrixTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

    // ========== FIX 6: Custom editor for better input (fixes 0.01 issue) ==========
    matrixTable.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
        {
            JTextField textField = (JTextField) getComponent();
            textField.setHorizontalAlignment(JTextField.CENTER);
            
            // Select all text when cell gets focus
            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        textField.selectAll();
                    });
                }
            });
    }
    
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            JTextField editor = (JTextField) super.getTableCellEditorComponent(
                table, value, isSelected, row, column);
            
            // Format value for editing - show integers without .0
            if (value instanceof Number) {
                double num = ((Number) value).doubleValue();
                if (Math.abs(num - Math.round(num)) < 0.000001) {
                    editor.setText(String.valueOf(Math.round(num)));
                } else {
                    editor.setText(String.valueOf(num));
                }
            } else if (value != null) {
                editor.setText(value.toString());
            }
            
            editor.selectAll();
            return editor;
        }
    });
        
        // Set column widths
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            matrixTable.getColumnModel().getColumn(i).setPreferredWidth(60);
        }
        
        JScrollPane scrollPane = new JScrollPane(matrixTable);
        add(scrollPane, BorderLayout.CENTER);
        
        dimensionsLabel = new JLabel("2 x 2 Matrix");
        add(dimensionsLabel, BorderLayout.SOUTH);
    }
    
    private void resizeMatrix() {
        int newRows = (int) rowsSpinner.getValue();
        int newCols = (int) colsSpinner.getValue();
        
        // Save current data
        double[][] oldData = getMatrixData();
        int oldRows = tableModel.getRowCount();
        int oldCols = tableModel.getColumnCount();
        
        // Resize table
        tableModel.setRowCount(newRows);
        tableModel.setColumnCount(newCols);
        
        // Restore data where possible
        for (int i = 0; i < Math.min(newRows, oldRows); i++) {
            for (int j = 0; j < Math.min(newCols, oldCols); j++) {
                tableModel.setValueAt(oldData[i][j], i, j);
            }
        }
        
        // Fill new cells with 0
        for (int i = oldRows; i < newRows; i++) {
            for (int j = 0; j < newCols; j++) {
                tableModel.setValueAt(0.0, i, j);
            }
        }
        for (int i = 0; i < Math.min(newRows, oldRows); i++) {
            for (int j = oldCols; j < newCols; j++) {
                tableModel.setValueAt(0.0, i, j);
            }
        }
        
        dimensionsLabel.setText(newRows + " x " + newCols + " Matrix");
    }
    
    private void fillRandom() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                double value = Math.round((Math.random() * 20 - 10) * 100.0) / 100.0;
                tableModel.setValueAt(value, i, j);
            }
        }
    }
    
    private void clearMatrix() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                tableModel.setValueAt(0.0, i, j);
            }
        }
    }
    
    public double[][] getMatrixData() {
        int rows = tableModel.getRowCount();
        int cols = tableModel.getColumnCount();
        double[][] data = new double[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Object value = tableModel.getValueAt(i, j);
                if (value instanceof Number) {
                    data[i][j] = ((Number) value).doubleValue();
                } else if (value instanceof String) {
                    try {
                        data[i][j] = Double.parseDouble((String) value);
                    } catch (NumberFormatException e) {
                        data[i][j] = 0.0;
                    }
                } else {
                    data[i][j] = 0.0;
                }
            }
        }
        return data;
    }
    
    public void setMatrixData(double[][] data) {
        tableModel.setRowCount(data.length);
        tableModel.setColumnCount(data[0].length);
        
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                tableModel.setValueAt(data[i][j], i, j);
            }
        }
        
        rowsSpinner.setValue(data.length);
        colsSpinner.setValue(data[0].length);
        dimensionsLabel.setText(data.length + " x " + data[0].length + " Matrix");
    }
    
    public int getRows() {
        return tableModel.getRowCount();
    }
    
    public int getCols() {
        return tableModel.getColumnCount();
    }
    
    public String getMatrixName() {
        return matrixName;
    }
}