package de.felix.gaussalgorithmus.controller;

import de.felix.gaussalgorithmus.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author EinfachBeez | https://github.com/EinfachBeez
 */
public class MenuController implements Initializable {

    @FXML Circle image;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        image.setFill(new ImagePattern(new Image(App.getInstance().getResource("img/carlFriedrichGauss.jpg"))));
    }

    @FXML
    public void onStartButtonAction() {
        try {
            App.getInstance().openScene(SettingsController.class);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void onEndButtonAction() {
        Platform.exit();
    }

    public static FXMLLoader createLoader() {
        return new FXMLLoader(App.getInstance().getViewResource("menu.fxml"));
    }
}
