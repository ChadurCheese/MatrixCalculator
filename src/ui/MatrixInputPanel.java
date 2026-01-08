package ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

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
        tableModel = new DefaultTableModel(2, 2) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return Double.class;
            }
        };
        
        matrixTable = new JTable(tableModel) {
            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                TableCellEditor editor = super.getCellEditor(row, column);
                if (editor instanceof DefaultCellEditor) {
                    Component component = ((DefaultCellEditor) editor).getComponent();
                    if (component instanceof JTextField) {
                        JTextField textField = (JTextField) component;
                        // Remove the default formatter that causes 0.01 issue
                        textField.setHorizontalAlignment(JTextField.RIGHT);
                        
                        // Add focus listener for better UX
                        textField.addFocusListener(new FocusAdapter() {
                            @Override
                            public void focusGained(FocusEvent e) {
                                textField.selectAll();
                            }
                        });
                    }
                }
                return editor;
            }
        };

        matrixTable.setRowHeight(25);
        matrixTable.getTableHeader().setReorderingAllowed(false);
        
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