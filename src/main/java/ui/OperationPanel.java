package ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class OperationPanel extends VBox {
    private ComboBox<String> operationCombo;
    private Button calculateButton;
    private Button clearButton;
    private Spinner<Double> scalarSpinner;
    private Spinner<Integer> exponentSpinner;
    private Label scalarLabel;
    private Label exponentLabel;

    private OperationListener listener;

    public interface OperationListener {
        void onOperationSelected(String operation, double scalar, int exponent);
        void onClear();
    }

    public OperationPanel() {
        initComponents();
    }

    private void initComponents() {
        setSpacing(8);
        setPadding(new Insets(8));
        setStyle("-fx-border-color: #d0d0d0; -fx-border-radius: 4;");

        Label selectLabel = new Label("Select Operation:");

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

        operationCombo = new ComboBox<>();
        operationCombo.getItems().addAll(operations);
        operationCombo.getSelectionModel().selectFirst();
        operationCombo.setMaxWidth(Double.MAX_VALUE);
        operationCombo.setOnAction(e -> updateControls());

        scalarLabel = new Label("Scalar:");
        scalarSpinner = new Spinner<>(-100.0, 100.0, 1.0, 0.5);
        scalarSpinner.setEditable(true);
        scalarLabel.setVisible(false);
        scalarLabel.setManaged(false);
        scalarSpinner.setVisible(false);
        scalarSpinner.setManaged(false);

        exponentLabel = new Label("Exponent:");
        exponentSpinner = new Spinner<>(-10, 10, 2);
        exponentSpinner.setEditable(true);
        exponentLabel.setVisible(false);
        exponentLabel.setManaged(false);
        exponentSpinner.setVisible(false);
        exponentSpinner.setManaged(false);

        calculateButton = new Button("Calculate");
        calculateButton.setStyle("-fx-base: #4682b4; -fx-font-weight: bold;");
        calculateButton.setMaxWidth(Double.MAX_VALUE);

        clearButton = new Button("Reset");
        clearButton.setStyle("-fx-base: #dc143c;");
        clearButton.setMaxWidth(Double.MAX_VALUE);

        calculateButton.setOnAction(e -> performCalculation());
        clearButton.setOnAction(e -> {
            if (listener != null) {
                listener.onClear();
            }
        });

        getChildren().addAll(
            selectLabel, operationCombo,
            scalarLabel, scalarSpinner,
            exponentLabel, exponentSpinner,
            calculateButton, clearButton
        );
    }

    private void updateControls() {
        String selected = operationCombo.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        boolean showScalar = selected.contains("Scalar");
        boolean showExponent = selected.contains("Power");

        scalarLabel.setVisible(showScalar);
        scalarLabel.setManaged(showScalar);
        scalarSpinner.setVisible(showScalar);
        scalarSpinner.setManaged(showScalar);

        exponentLabel.setVisible(showExponent);
        exponentLabel.setManaged(showExponent);
        exponentSpinner.setVisible(showExponent);
        exponentSpinner.setManaged(showExponent);
    }

    private void performCalculation() {
        if (listener != null) {
            String operation = operationCombo.getSelectionModel().getSelectedItem();
            double scalar = scalarSpinner.getValue();
            int exponent = exponentSpinner.getValue();
            listener.onOperationSelected(operation, scalar, exponent);
        }
    }

    public void setOperationListener(OperationListener listener) {
        this.listener = listener;
    }

    public String getSelectedOperation() {
        return operationCombo.getSelectionModel().getSelectedItem();
    }
}
