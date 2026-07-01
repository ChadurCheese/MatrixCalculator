package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MatrixDisplayPanel extends VBox {
    private TextArea resultArea;
    private Label operationLabel;
    private Label titleLabel;

    public MatrixDisplayPanel() {
        initComponents();
    }

    private void initComponents() {
        setSpacing(8);
        setPadding(new Insets(8));
        setStyle("-fx-border-color: #d0d0d0; -fx-border-radius: 4;");

        titleLabel = new Label("Result");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 13));

        operationLabel = new Label("Operation: None");
        operationLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setFont(Font.font("Monospaced", 14));
        resultArea.setStyle("-fx-control-inner-background: #f5f5f5;");
        VBox.setVgrow(resultArea, Priority.ALWAYS);

        getChildren().addAll(titleLabel, operationLabel, resultArea);
    }

    public void displayResult(String operation, String result) {
        operationLabel.setText("Operation: " + operation);
        resultArea.setText(result);
        resultArea.positionCaret(0);
    }

    public void clear() {
        operationLabel.setText("Operation: None");
        resultArea.setText("");
    }

    public void displayError(String operation, String error) {
        operationLabel.setText("Operation: " + operation + " - ERROR");
        resultArea.setText("Error: " + error);
        resultArea.positionCaret(0);
    }
}
