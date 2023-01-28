module de.felix.gaussalgorithmus {
    requires javafx.controls;
    requires javafx.fxml;
    requires net.yetihafen.javafx.customcaption;
    requires com.sun.jna;
    requires com.sun.jna.platform;

    opens de.felix.gaussalgorithmus;
    exports de.felix.gaussalgorithmus;
    opens de.felix.gaussalgorithmus.controller to javafx.fxml;
    exports de.felix.gaussalgorithmus.controller;
    exports de.felix.gaussalgorithmus.algorithm;
    opens de.felix.gaussalgorithmus.algorithm;
}