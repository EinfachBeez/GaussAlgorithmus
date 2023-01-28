package de.felix.gaussalgorithmus;

import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import de.felix.gaussalgorithmus.controller.GaussController;
import de.felix.gaussalgorithmus.controller.MenuController;
import de.felix.gaussalgorithmus.controller.SettingsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import net.yetihafen.javafx.customcaption.CustomCaption;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

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

        // Remove white border around window & add show effect
        stage.getScene().getRoot().setEffect(new DropShadow());
        stage.show();

        // Because Florian hasn't fixed this in javafx-customcaption yet
        stage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) stage.setMaximized(false);
        });

        scene.addEventHandler(KeyEvent.KEY_TYPED, event -> {
            if (controller instanceof SettingsController settings) settings.onKeyPress(event);
            if (controller instanceof GaussController gauss) gauss.onKeyPress(event);
        });

        WinDef.HWND hwnd = getHwnd(stage);
        BaseTSD.LONG_PTR style = User32Ex.INSTANCE.GetWindowLongPtr(hwnd, WinUser.GWL_STYLE);
        BaseTSD.LONG_PTR newStyle = new BaseTSD.LONG_PTR(style.longValue() & ~(WinUser.WS_DLGFRAME | WinUser.WS_BORDER));
        System.out.println(User32Ex.INSTANCE.SetWindowLongPtr(hwnd, WinUser.GWL_STYLE, newStyle));

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

    public static WinDef.HWND getHwnd(Stage stage) {
        // bad hack to use when jar is not shaded and can't access internal functions
        String randomId = UUID.randomUUID().toString();
        String title = stage.getTitle();
        stage.setTitle(randomId);
        WinDef.HWND hWnd = User32.INSTANCE.FindWindow(null, randomId);
        stage.setTitle(title);
        return hWnd;
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