package de.felix.gaussalgorithmus.controller;

import de.felix.gaussalgorithmus.App;
import de.felix.gaussalgorithmus.algorithm.GaussAlgorithm;
import de.felix.gaussalgorithmus.algorithm.LabelMatrix;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class GaussController implements Initializable {

    private GaussAlgorithm gaussAlgorithm;
    private LabelMatrix labelMatrix;
    private int x = 0, y = 0;
    private String labelValue = "";
    private static int methodCount;

    @FXML private StackPane matrixPane;
    @FXML private Text alertText;
    @FXML private Pane calcPathPane;
    @FXML private Button solveButton;
    @FXML private Button newCalcButton;
    @FXML private HBox resultHBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gaussAlgorithm = new GaussAlgorithm(methodCount, methodCount + 1, calcPathPane, resultHBox);
        labelMatrix = new LabelMatrix(matrixPane, methodCount, methodCount + 1);
        labelMatrix.activateLabel(labelMatrix.getLabelMap()[y][x]);
        onMouseClick();

        newCalcButton.setVisible(false);
    }

    public void onKeyPress(KeyEvent event) {
        if (!solveButton.isVisible()) return;
        // 0x08 = Backspace
        Label label = labelMatrix.getLabelMap()[y][x];
        String number = label.getText();
        System.out.println(labelValue);
        if (event.getCharacter().matches("\u0008")) {
            if (number.length() == 0) return;
            label.setText(label.getText().substring(0, number.length() - 1));
            labelValue = labelValue.substring(0, labelValue.length() - 1);
            return;
        }
        if (event.getCharacter().matches("\u0009")) {
            Label[][] tempList = labelMatrix.getLabelMap();
            if (x == tempList[0].length - 1 && y == tempList.length - 1) return;
            labelMatrix.deactivateLabel(tempList[y][x]);
            if (x == tempList[0].length - 1) {
                x = 0;
                y++;
                labelValue = "";
                labelMatrix.activateLabel(labelMatrix.getLabelMap()[y][x]);
                return;
            }
            x++;
            labelValue = "";
            labelMatrix.activateLabel(labelMatrix.getLabelMap()[y][x]);
        }
        if (event.getCharacter().matches("\u0000A")) onSolveButtonAction();

        labelValue += event.getCharacter();
        // ^[+-]?\d*\.?\d*$
        if ((!labelValue.matches("^[+-]?\\d*\\.?\\d*$") || labelValue.replaceAll("[+-]", "").length() > 4)) {
            labelValue = labelValue.substring(0, labelValue.length() - 1);
            return;
        }
        label.setText(number + event.getCharacter());
    }

    private void onMouseClick() {
        if (!solveButton.isVisible()) return;
        Label[][] labels = labelMatrix.getLabelMap();
        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[0].length; j++) {
                int finalI = i, finalJ = j;
                labels[i][j].setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        labelMatrix.deactivateLabel(labels[y][x]);
                        x = finalJ;
                        y = finalI;
                        labelMatrix.activateLabel(labels[y][x]);
                    }
                });
            }
        }
    }

    @FXML
    public void onSolveButtonAction() {
        Label[][] labels = labelMatrix.getLabelMap();
        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[0].length; j++) {
                double value;
                try {
                    value = Double.parseDouble(labels[i][j].getText());
                    labels[i][j].setOnMouseClicked(null);

                } catch (NumberFormatException exception) {
                    alertText.setText("Die Matrix ist nicht ganz ausgefüllt");
                    return;
                }
                gaussAlgorithm.fillMatrix(i, j, value);
            }
        }
        solveButton.setVisible(false);
        newCalcButton.setVisible(true);
        gaussAlgorithm.createCalcPath();
        gaussAlgorithm.solve();
        gaussAlgorithm.createResult();
        System.out.println(gaussAlgorithm);
    }

    @FXML
    public void onNewCalcButtonAction() {
        solveButton.setVisible(true);
        newCalcButton.setVisible(false);
        gaussAlgorithm.clear();
        labelMatrix.deactivateLabel(labelMatrix.getLabelMap()[y][x]);
        gaussAlgorithm = new GaussAlgorithm(methodCount, methodCount + 1, calcPathPane, resultHBox);
        labelMatrix = new LabelMatrix(matrixPane, methodCount, methodCount + 1);
        y = 0;
        x = 0;
        labelMatrix.activateLabel(labelMatrix.getLabelMap()[y][x]);
    }

    public static void setMethodCount(int count) {
        methodCount = count;
    }

    public static FXMLLoader createLoader() {
        return new FXMLLoader(App.getInstance().getViewResource("gauss.fxml"));
    }
}