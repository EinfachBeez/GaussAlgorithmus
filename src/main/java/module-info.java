module de.felix.gaussalgorithmus {
    requires javafx.controls;
    requires javafx.fxml;
    requires net.yetihafen.javafx.customcaption;

    opens de.felix.gaussalgorithmus;
    exports de.felix.gaussalgorithmus;
    opens de.felix.gaussalgorithmus.controller to javafx.fxml;
    exports de.felix.gaussalgorithmus.controller;
    exports de.felix.gaussalgorithmus.algorithm;
    opens de.felix.gaussalgorithmus.algorithm;
}