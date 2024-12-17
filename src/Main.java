import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        int liftCount = 3;
        int floors = 10;

        new LiftView(primaryStage, liftCount, floors);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
