package de.felix.gaussalgorithmus;

import de.felix.gaussalgorithmus.controller.GaussController;
import de.felix.gaussalgorithmus.controller.MenuController;
import de.felix.gaussalgorithmus.controller.SettingsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import net.yetihafen.javafx.customcaption.CustomCaption;

import java.io.IOException;
import java.net.URL;

public class App extends Application {

    private static App instance;
    private static Scene scene;
    private Object controller;

    @Override
    public void start(Stage stage) throws IOException {
        instance = this;

        scene = new Scene(MenuController.createLoader().load(), 800, 600);
        controller = MenuController.createLoader().getController();

        stage.getIcons().add(new Image(getResource("img/carlFriedrichGauss.jpg")));
        stage.setTitle("Gauß Elimination");
        stage.setResizable(false);
        stage.setScene(scene);

        stage.show();

        scene.addEventHandler(KeyEvent.KEY_TYPED, event -> {
            if (controller instanceof SettingsController settings) settings.onKeyPress(event);
            if (controller instanceof GaussController gauss) gauss.onKeyPress(event);
        });

        CustomCaption.useForStage(stage);
    }

    public void openScene(Class<? extends Initializable> clazz) {
        try {
            setRoot((FXMLLoader) clazz.getMethod("createLoader").invoke(null));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void setRoot(FXMLLoader loader) {
        try {
            scene.setRoot(loader.load());
            controller = loader.getController();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public String getResource(String file) {
        return String.valueOf(App.class.getResource(file));
    }

    public URL getViewResource(String file) {
        return App.class.getResource("view/" + file);
    }

    public static App getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        launch();
    }
}