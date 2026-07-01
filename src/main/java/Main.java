import ui.MatrixCalculatorUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Matrix Calculator");

        MatrixCalculatorUI calculator = new MatrixCalculatorUI();
        Scene scene = new Scene(calculator, 1200, 700);

        try {
            primaryStage.getIcons().add(new Image(
                getClass().getResourceAsStream("/assets/matrixCalc.jpg")));
        } catch (Exception e) {
            // Use default icon if custom icon not found
        }

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
