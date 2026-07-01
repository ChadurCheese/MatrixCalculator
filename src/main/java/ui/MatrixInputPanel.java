package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MatrixInputPanel extends VBox {
    private GridPane grid;
    private TextField[][] cells;
    private Label dimensionsLabel;
    private Spinner<Integer> rowsSpinner;
    private Spinner<Integer> colsSpinner;
    private String matrixName;
    private int rows = 2;
    private int cols = 2;

    public MatrixInputPanel(String name) {
        this.matrixName = name;
        initComponents();
    }

    private void initComponents() {
        setSpacing(8);
        setPadding(new Insets(8));
        setStyle("-fx-border-color: #d0d0d0; -fx-border-radius: 4;");

        Label title = new Label(matrixName);
        title.setFont(Font.font("System", FontWeight.BOLD, 13));

        HBox controlPanel = new HBox(6);
        controlPanel.setAlignment(Pos.CENTER_LEFT);

        rowsSpinner = new Spinner<>(1, 10, rows);
        rowsSpinner.setEditable(true);
        rowsSpinner.setPrefWidth(65);

        colsSpinner = new Spinner<>(1, 10, cols);
        colsSpinner.setEditable(true);
        colsSpinner.setPrefWidth(65);

        Button resizeButton = new Button("Resize");
        resizeButton.setOnAction(e -> resizeMatrix());

        Button randomButton = new Button("Random Fill");
        randomButton.setOnAction(e -> fillRandom());

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> clearMatrix());

        controlPanel.getChildren().addAll(
            new Label("Rows:"), rowsSpinner,
            new Label("Cols:"), colsSpinner,
            resizeButton, randomButton, clearButton
        );

        grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        buildGrid(rows, cols, null);

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);

        dimensionsLabel = new Label(rows + " x " + cols + " Matrix");

        getChildren().addAll(title, controlPanel, scrollPane, dimensionsLabel);
    }

    private void buildGrid(int newRows, int newCols, double[][] oldData) {
        grid.getChildren().clear();
        cells = new TextField[newRows][newCols];
        for (int i = 0; i < newRows; i++) {
            for (int j = 0; j < newCols; j++) {
                TextField field = new TextField();
                field.setPrefWidth(70);
                field.setAlignment(Pos.CENTER);
                double value = 0.0;
                if (oldData != null && i < oldData.length && j < oldData[i].length) {
                    value = oldData[i][j];
                }
                field.setText(formatNumber(value));
                field.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                    if (isFocused) {
                        field.selectAll();
                    } else {
                        normalizeField(field);
                    }
                });
                cells[i][j] = field;
                grid.add(field, j, i);
            }
        }
        rows = newRows;
        cols = newCols;
    }

    private void normalizeField(TextField field) {
        try {
            double value = Double.parseDouble(field.getText().trim());
            field.setText(formatNumber(value));
        } catch (NumberFormatException e) {
            field.setText("0");
        }
    }

    private String formatNumber(double num) {
        if (Math.abs(num - Math.round(num)) < 0.000001) {
            return String.valueOf(Math.round(num));
        }
        return String.valueOf(num);
    }

    private void resizeMatrix() {
        int newRows = rowsSpinner.getValue();
        int newCols = colsSpinner.getValue();
        double[][] oldData = getMatrixData();
        buildGrid(newRows, newCols, oldData);
        dimensionsLabel.setText(newRows + " x " + newCols + " Matrix");
    }

    private void fillRandom() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double value = Math.round((Math.random() * 20 - 10) * 100.0) / 100.0;
                cells[i][j].setText(formatNumber(value));
            }
        }
    }

    private void clearMatrix() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j].setText("0");
            }
        }
    }

    public double[][] getMatrixData() {
        double[][] data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                try {
                    data[i][j] = Double.parseDouble(cells[i][j].getText().trim());
                } catch (NumberFormatException e) {
                    data[i][j] = 0.0;
                }
            }
        }
        return data;
    }

    public void setMatrixData(double[][] data) {
        int newRows = data.length;
        int newCols = data[0].length;
        buildGrid(newRows, newCols, data);
        rowsSpinner.getValueFactory().setValue(newRows);
        colsSpinner.getValueFactory().setValue(newCols);
        dimensionsLabel.setText(newRows + " x " + newCols + " Matrix");
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public String getMatrixName() {
        return matrixName;
    }
}
