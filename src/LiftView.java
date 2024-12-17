import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

class LiftView {
    private LiftController controller;
    private final Map<Integer, Label> liftLabels;

    public LiftView(Stage primaryStage, int liftCount, int floors) {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        liftLabels = new HashMap<>();
        for (int i = 1; i <= liftCount; i++) {
            Label liftLabel = new Label("Лифт " + i + ": Этаж 1, Статус: стоит");
            gridPane.add(liftLabel, 0, i);
            liftLabels.put(i, liftLabel);
        }

        controller = new LiftController(liftCount, this);

        GridPane buttonPane = createFloorButtons(floors);

        Label callLabel = new Label("Вызов лифта:");
        VBox root = new VBox(20, gridPane, callLabel, buttonPane);
        root.setStyle("-fx-padding: 20px");

        primaryStage.setTitle("Управление лифтами");
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.show();
    }

    public void updateLift(int liftId, int floor, int direction) {
        String status = direction == 0 ? "стоит" : (direction > 0 ? "едет вверх" : "едет вниз");
        liftLabels.get(liftId).setText("Лифт " + liftId + ": Этаж " + floor + ", Статус: " + status);
    }


    private GridPane createFloorButtons(int floors) {
        GridPane buttonPane = new GridPane();
        buttonPane.setHgap(10);
        buttonPane.setVgap(10);

        for (int i = 1; i <= floors; i++) {
            Button floorButton = new Button("Этаж " + i);
            floorButton.setPrefSize(80, 40);

            final int floor = i;
            floorButton.setOnAction(_ -> controller.callLift(floor));

            buttonPane.add(floorButton, (i - 1) % 5, (i - 1) / 5);
        }
        return buttonPane;
    }
}