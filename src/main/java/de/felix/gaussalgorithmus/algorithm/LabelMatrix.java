package de.felix.gaussalgorithmus.algorithm;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

/**
 * @author EinfachBeez | https://github.com/EinfachBeez
 */
public class LabelMatrix {

    private final Pane pane;
    private GridPane gridPane;
    private final BooleanLabel[][] labelMap;
    private final int rows, colums;

    public LabelMatrix(Pane pane, int rows, int colums) {
        this.pane = pane;
        this.rows = rows;
        this.colums = colums;
        labelMap = new BooleanLabel[rows][colums];
        generateLabelMatrix();
    }

    private void generateLabelMatrix() {
        gridPane = createGridPane();

        fillMatrix(gridPane);
    }

    private void fillMatrix(GridPane pane) {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colums; j++) {
                BooleanLabel label = createLabel();
                labelMap[i][j] = label;
                pane.add(label, j, i);
            }
        }
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("matrix-grid");

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        separator.setValignment(VPos.CENTER);
        separator.setPadding(new Insets(0, 0, 0, -5));
        GridPane.setConstraints(separator, colums - 1, 0 , 1, colums);

        HBox leftCircle = new HBox(generateCurve(0, 0, 0, (60 + 5) * rows, 1));
        leftCircle.setPadding(new Insets(0, 0, 0 , -30));
        GridPane.setConstraints(leftCircle, 0, 0, 1, colums);

        HBox rightCircle = new HBox(generateCurve(0, 0, 0, (60 + 5) * rows, -1));
        GridPane.setConstraints(rightCircle, colums, 0, 1, colums);

        RowConstraints row = new RowConstraints();
        ColumnConstraints column = new ColumnConstraints();
        gridPane.getRowConstraints().add(row);
        gridPane.getColumnConstraints().add(column);

        gridPane.getChildren().addAll(separator, leftCircle, rightCircle);

        pane.getChildren().add(gridPane);
        return gridPane;
    }

    public CubicCurve generateCurve(double startX, double startY, double stopX, double stopY, int prefix) {
        CubicCurve cubicCurve = new CubicCurve();
        cubicCurve.setStartX(startX);
        cubicCurve.setStartY(startY);
        cubicCurve.setEndX(stopX);
        cubicCurve.setEndY(stopY);


        cubicCurve.setControlX1(startX - (20 * prefix));
        cubicCurve.setControlY1(startY + 40);
        cubicCurve.setControlX2(stopX - (20 * prefix));
        cubicCurve.setControlY2(stopY - 40);

        cubicCurve.setStroke(Color.WHITE);
        cubicCurve.setStrokeWidth(4);
        cubicCurve.setFill(Color.TRANSPARENT);
        return cubicCurve;
    }

    private BooleanLabel createLabel() {
        BooleanLabel label = new BooleanLabel();
        label.getStyleClass().add("matrix-label");
        return label;
    }

    public void activateLabel(BooleanLabel label) {
        label.getStyleClass().clear();
        label.getStyleClass().add("matrix-label-activated");
        label.setActivated(true);
    }

    public void deactivateLabel(BooleanLabel label) {
        label.getStyleClass().clear();
        label.getStyleClass().add("matrix-label");
        label.setActivated(false);
    }

    public int getRows() {
        return rows;
    }

    public int getColums() {
        return colums;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public BooleanLabel[][] getLabelMap() {
        return labelMap;
    }
}
