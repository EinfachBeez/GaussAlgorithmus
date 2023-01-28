package de.felix.gaussalgorithmus.controller;

import de.felix.gaussalgorithmus.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author EinfachBeez | https://github.com/EinfachBeez
 */
public class SettingsController implements Initializable {

    @FXML private Label inputCount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void onKeyPress(KeyEvent event) {
        String number = inputCount.getText();
        // 0x08 = Backspace
        if (event.getCharacter().matches("\u0008")) {
            if (number.length() == 0) return;
            inputCount.setText(number.substring(0, number.length() -1));
            return;
        }
        if (event.getCharacter().matches("\u0000A")) onSaveButtonAction();
        if (!event.getCharacter().matches("[2-9]")) return;
        if (number.length() >= 3) return;
        inputCount.setText(number + event.getCharacter());
    }

    @FXML
    public void onSaveButtonAction() {
        try {
            int value;
            try {
                value = Integer.parseInt(inputCount.getText());
            } catch (NumberFormatException exception) {
                return;
            }
            GaussController.setMethodCount(value);
            App.getInstance().openScene(GaussController.class);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static FXMLLoader createLoader() {
        return new FXMLLoader(App.getInstance().getViewResource("settings.fxml"));
    }
}
