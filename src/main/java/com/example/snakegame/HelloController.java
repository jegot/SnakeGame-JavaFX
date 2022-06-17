package com.example.snakegame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class HelloController {
    @FXML
    TextField playerName;

    @FXML
    ComboBox<Integer> snakeLength;

    @FXML
    ColorPicker snakeColorPicker;

    @FXML
    ColorPicker snakeHeadColor;

    @FXML
    ColorPicker fruitColor;

    @FXML
    ComboBox<String> speedChoice;

    @FXML
    protected void onBeginButtonClick(ActionEvent actionEvent) {
        Node src = (Node) actionEvent.getSource();
        Stage stage = (Stage) src.getScene().getWindow();
            SnakePane sp = new SnakePane(snakeLength.getValue(), snakeColorPicker.getValue(), snakeHeadColor.getValue(),
                    fruitColor.getValue(), speedChoice.getValue(), playerName.getText());
            Scene scene = new Scene(sp, 600, 400);
            stage.setScene(scene);
            sp.startGame();
    }

    public void initialize () {
        for (int i = 1; i < 21; i++) {
            snakeLength.getItems().add(i);
        }

        speedChoice.getItems().add("Slow");
        speedChoice.getItems().add("Medium");
        speedChoice.getItems().add("Fast");

        snakeLength.setValue(6);
        snakeColorPicker.setValue(Color.GREEN);
        snakeHeadColor.setValue(Color.RED);
        fruitColor.setValue(Color.PURPLE);
        speedChoice.setValue("Medium");
    }
}