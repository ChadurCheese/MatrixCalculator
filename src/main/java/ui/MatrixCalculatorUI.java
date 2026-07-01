package ui;

import core.Matrix;
import core.MatrixOperations;
import core.MatrixException;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class MatrixCalculatorUI extends BorderPane {
    private MatrixInputPanel matrixAPanel;
    private MatrixInputPanel matrixBPanel;
    private OperationPanel operationPanel;
    private MatrixDisplayPanel resultPanel;

    public MatrixCalculatorUI() {
        initUI();
    }

    private void initUI() {
        MenuBar menuBar = createMenuBar();
        setTop(menuBar);

        matrixAPanel = new MatrixInputPanel("Matrix A");
        matrixBPanel = new MatrixInputPanel("Matrix B");
        operationPanel = new OperationPanel();
        resultPanel = new MatrixDisplayPanel();

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

        GridPane mainPanel = new GridPane();
        mainPanel.setHgap(10);
        mainPanel.setVgap(10);
        mainPanel.setPadding(new Insets(10));

        ColumnConstraints colA = new ColumnConstraints();
        colA.setPercentWidth(30);
        ColumnConstraints colCenter = new ColumnConstraints();
        colCenter.setPercentWidth(40);
        ColumnConstraints colB = new ColumnConstraints();
        colB.setPercentWidth(30);
        mainPanel.getColumnConstraints().addAll(colA, colCenter, colB);

        RowConstraints rowTop = new RowConstraints();
        rowTop.setPercentHeight(25);
        RowConstraints rowBottom = new RowConstraints();
        rowBottom.setPercentHeight(75);
        mainPanel.getRowConstraints().addAll(rowTop, rowBottom);

        mainPanel.add(matrixAPanel, 0, 0, 1, 2);
        mainPanel.add(operationPanel, 1, 0);
        mainPanel.add(resultPanel, 1, 1);
        mainPanel.add(matrixBPanel, 2, 0, 1, 2);

        GridPane.setHalignment(matrixAPanel, HPos.CENTER);
        GridPane.setValignment(matrixAPanel, VPos.TOP);
        GridPane.setFillWidth(matrixAPanel, true);
        GridPane.setFillHeight(matrixAPanel, true);
        GridPane.setFillWidth(matrixBPanel, true);
        GridPane.setFillHeight(matrixBPanel, true);
        GridPane.setFillWidth(operationPanel, true);
        GridPane.setFillHeight(operationPanel, true);
        GridPane.setFillWidth(resultPanel, true);
        GridPane.setFillHeight(resultPanel, true);

        setCenter(mainPanel);

        Label statusBar = new Label(" Ready");
        statusBar.setPadding(new Insets(4));
        statusBar.setStyle("-fx-border-color: #d0d0d0; -fx-border-width: 1 0 0 0;");
        setBottom(statusBar);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        newItem.setOnAction(e -> clearAll());
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().addAll(newItem, new SeparatorMenuItem(), exitItem);

        Menu examplesMenu = new Menu("Examples");
        MenuItem identity2x2 = new MenuItem("2x2 Identity Matrix");
        identity2x2.setOnAction(e -> loadExample("identity2x2"));
        MenuItem random3x3 = new MenuItem("Random 3x3 Matrix");
        random3x3.setOnAction(e -> loadExample("random3x3"));
        MenuItem systemEq = new MenuItem("System of Equations");
        systemEq.setOnAction(e -> loadExample("system"));
        examplesMenu.getItems().addAll(identity2x2, random3x3, systemEq);

        Menu helpMenu = new Menu("Help");
        MenuItem helpItem = new MenuItem("Help Contents");
        helpItem.setOnAction(e -> showHelpDialog());
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAboutDialog());
        helpMenu.getItems().addAll(helpItem, new SeparatorMenuItem(), aboutItem);

        menuBar.getMenus().addAll(fileMenu, examplesMenu, helpMenu);
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
                matrixAPanel.setMatrixData(new double[][]{{2, 1}, {1, 2}});
                matrixBPanel.setMatrixData(new double[][]{{5}, {4}});
                break;
        }
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Matrix Calculator");
        alert.setHeaderText(null);
        alert.setContentText(
            "Matrix Calculator\n" +
            "Version 1.0\n\n" +
            "A comprehensive matrix operations calculator\n" +
            "Supporting addition, multiplication, transpose,\n" +
            "inverse, determinant, and more!\n\n" +
            "Created with JavaFX"
        );
        alert.showAndWait();
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

        TextArea textArea = new TextArea(helpText);
        textArea.setEditable(false);
        textArea.setFont(javafx.scene.text.Font.font("Monospaced", 12));
        textArea.setPrefSize(400, 300);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }
}
