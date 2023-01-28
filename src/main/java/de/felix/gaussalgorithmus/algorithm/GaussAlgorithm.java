package de.felix.gaussalgorithmus.algorithm;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author EinfachBeez | https://github.com/EinfachBeez
 */
public class GaussAlgorithm {

    private Pane calcPane;
    private HBox resolveBox;
    private TextArea textArea;
    private int textPosition;

    private final int rows, colums;

    private double[][] matrix;

    private double[] result;

    public GaussAlgorithm(int rows, int columns, Pane calcPane, HBox solveBox) {
        this.rows = rows;
        this.colums = columns;
        this.calcPane = calcPane;
        this.resolveBox = solveBox;

        matrix = new double[rows][columns];
        result = new double[rows];
        textPosition = 0;
    }

    public void fillMatrix(int rows, int columns, double value) {
        matrix[rows][columns] = value;
    }

    public void solve()  {
        DecimalFormat formatter = new DecimalFormat();
        int steps = 0;

        // Loops through the count of functions / rows
        for (int i = 0; i < rows; i++) {
            int swaps = 0;

            /*
             * Checks if the matrix value at position i is 0.
             * When difference between the matrix value and 0 < 1e-10 then is the matrix value at i 0 to avoid
             * calculation inaccuracies
             */
            while (isEqual(matrix[i][i], 0)) {
                swaps++;

                /*
                 * Checks the count of swaps which have been performed
                 * If more swaps than actually the length - i are performed, the matrix is inconsistent
                 */
                if (swaps < rows - i) {

                    /*
                     * Checks if the row to swap with is also 0
                     * true -> skipp swap action
                     */
                    if (isEqual(matrix[i + 1][i], 0)) {
                        continue;
                    }

                    /*
                     * Swaps the row from position i with the row from position i + 1 (the next row)
                     */
                    double[] tempRow = matrix[i];
                    matrix[i] = matrix[i + 1];
                    matrix[i + 1] = tempRow;

                    printSteps("Tausche Reihe " + (i + 1) + " mit Reihe " + formatter.format(i + 2) + ".", steps);
                    steps++;
                    printCalcPath();
                } else {
                    /*
                     * If there are more or equal swaps than rows - i the solution
                     * set is either infinite or there is no solution set
                     */
                    String value = "";
                    switch (calcSolubility()) {
                        case INFINITE_SOLUTION -> value = "Unendliche";
                        case NO_SOLUTION -> value = "Keine";
                    }
                    textArea.setText(textArea.getText() + "Ein Fehler ist aufgetreten: " + value + " Lösungsmenge\n");
                    printCalcPath();
                    return;
                }
            }

            /*
             * Divides the row by the pivot value and reset to 1
             * (if pivot isn't already 1)
             */
            if (!isEqual(matrix[i][i], 1)) {
                double scalar = matrix[i][i];
                matrix[i] = divide(matrix[i], scalar);
                printSteps("Teile Reihe " + (i + 1) + " durch " + formatter.format(scalar) + ".", steps);
                steps++;
                printCalcPath();
            }

            /*
             * Calculates the values underneath the current pivot
             */
            for (int j = i + 1; j < rows; j++) {
                double scalar = matrix[j][i];

                System.out.println("i: " + i + " :: j: " + j + " :: Scalar: " + scalar);

                if (isEqual(scalar, 1)) {
                    matrix[j] = subtract(matrix[j], matrix[i]);
                    printSteps("Subtrahiere Reihe " + formatter.format(i + 1) + " von Reihe " +
                                    formatter.format(j + 1) + ".", steps);
                    steps++;
                    printCalcPath();
                } else if (!isEqual(scalar, 0)) {
                    matrix[j] = subtract(matrix[j], multiply(matrix[i], scalar));
                    printSteps("Multipliziere Reihe " + formatter.format(i + 1) + " mit " + formatter.format(scalar) +
                            " und subtrahiere von Reihe " + formatter.format(j + 1) + ".", steps);
                    steps++;
                    printCalcPath();
                }
            }
        }

        /*
         * Back substitution
         */
        for (int i = 0; i < rows; i++) {
            for (int j = i - 1; j >= 0; j--) {
                double scalar = matrix[j][i];

                if (isEqual(scalar, 1)) {
                    matrix[j] = subtract(matrix[j], matrix[i]);
                    printSteps("Rückwärtseinsetzten:\nSubtrahiere Reihe " + formatter.format(i + 1) + " von Reihe " +
                                    formatter.format(j + 1) + ".", steps);
                    steps++;
                    printCalcPath();
                } else if (!isEqual(scalar, 0)) {
                    matrix[j] = subtract(matrix[j], multiply(matrix[i], scalar));
                    printSteps("Rückwärtseinsetzten:\nMultipliziere Reihe " + formatter.format(i + 1) + " mit " +
                            formatter.format(scalar) + " und subtrahiere von Reihe " + formatter.format(j + 1) + ".", steps);
                    steps++;
                    printCalcPath();
                }
            }
        }

        for (int i = 0; i < rows; i++) {
            result[i] = matrix[i][colums - 1];
        }

    }

    private double[] subtract(double[] row1, double[] row2) {
        double[] temp = new double[row1.length];

        for (int i = 0; i < temp.length; i++) {
            temp[i] = row1[i] - row2[i];
        }

        return temp;
    }

    private double[] multiply(double[] row, double scalar) {
        double[] temp = new double[row.length];

        for (int i = 0; i < temp.length; i++) {
            temp[i] = row[i] * scalar;
        }

        return temp;
    }

    private double[] divide(double[] row, double scalar) {
        double[] temp = new double[row.length];

        for (int i = 0; i < temp.length; i++) {
            temp[i] = row[i] / scalar;
        }

        return temp;
    }

    enum SolubilityType {
        NO_SOLUTION,
        ONE_SOLUTION,
        INFINITE_SOLUTION,
        NONE;
    }

    private SolubilityType calcSolubility() {
        double[][] coefficientMatrix = new double[rows][colums - 1];
        int[] rank = new int[2];
        int counter = 0;

        for (int i = 0; i < matrix.length; i++) {
            if (matrix[0].length - 1 >= 0)
                System.arraycopy(matrix[i], 0, coefficientMatrix[i], 0, matrix[0].length - 1);
        }

        for (int i = 0; i < coefficientMatrix.length; i++) {
            for (int j = 0; j < coefficientMatrix[0].length; j++) {
                if (coefficientMatrix[i][j] != 0) counter++;
            }
            if (counter > 0) {
                counter = 0;
                rank[0]++;
                continue;
            }
            counter = 0;
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != 0) counter++;
            }
            if (counter > 0) {
                counter = 0;
                rank[1]++;
                continue;
            }
            counter = 0;
        }

        if (rank[0] == rank[1]) {
            if (rank[0] < rows) return SolubilityType.INFINITE_SOLUTION;
            return SolubilityType.ONE_SOLUTION;
        }
        else return SolubilityType.NO_SOLUTION;
    }

    private boolean isEqual(double first, double second) {
        return Math.abs(Double.compare(first, second)) < 1e-10;
    }

    public void createCalcPath() {
        textArea = new TextArea();
        textArea.setLayoutX(12);
        textArea.setLayoutY(82);
        textArea.getStyleClass().add("calc-path");
        textArea.setBackground(Background.fill(Color.BLACK));
        textArea.setEditable(false);
        calcPane.getChildren().add(textArea);
    }

    public void createResult() {
        resolveBox.setVisible(true);
        resolveBox.setSpacing(20);
        resolveBox.setStyle("-fx-alignment: center");

        DecimalFormat formatter = new DecimalFormat();
        formatter.setRoundingMode(RoundingMode.FLOOR);

        System.out.println(result.length);
        for (int i = 0; i < result.length; i++) {
            Text text = new Text();
            text.setFill(Color.WHITE);
            text.setStyle("-fx-font-size: " + 6 * (12 / result.length));
            text.setText("x" + Character.toString('\u2080' + (i + 1)) + " = " +
                    new DecimalFormat("#.##").format(result[i]));
            resolveBox.getChildren().add(text);
        }
    }

    private void printSteps(String text, int step) {
        textArea.setText(textArea.getText() + "Schritt " + (step + 1) + ":\n" + text + "\n");
    }

    public void printCalcPath() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colums; j++) {
                int xUnicode = '\u2080' + (j + 1);
                String prefix = "";
                DecimalFormat format = new DecimalFormat("#.##");
                if (!(colums == j + 1))
                    prefix = matrix[i][j] < 0 ? "- " : "+ ";

                if (colums == j + 1) {
                    textArea.setText(textArea.getText() + "= " + format.format(matrix[i][j]));
                    break;
                }
                if (j == 0) prefix = matrix[i][j] < 0 ? "-" : "";
                textArea.setText(textArea.getText() + prefix + format.format(Math.abs(matrix[i][j])) +
                        "x" + Character.toString(xUnicode) + " ");
            }
            textArea.setText(textArea.getText() + "\n");
        }
        textArea.setText(textArea.getText() + "\n");
    }

    public void clear() {
        textArea.setVisible(false);
        resolveBox.setVisible(false);
        resolveBox.getChildren().clear();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colums; j++) {
                builder.append(matrix[i][j]).append(" | ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public int getRows() {
        return rows;
    }

    public int getColums() {
        return colums;
    }
}
