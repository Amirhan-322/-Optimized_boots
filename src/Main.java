import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static final double EPSILON = 1e-6;

    public static class Tableau {
        private final double[][] data;

        public Tableau(double[][] data) {
            this.data = data;
        }

        public double getElement(int row, int col) {
            return data[row][col];
        }
    }

    public static Tableau simplexMethod(float[] C, float[][] A, float[] B, int numVariables, int numConstraints) {
        double[][] tableau = initializeTableau(C, A, B, numVariables, numConstraints);

        while (true) {
            int enteringVariable = findEnteringVariable(tableau);
            if (enteringVariable == -1) {
                return new Tableau(tableau);
            }

            int leavingVariable = findLeavingVariable(tableau, enteringVariable);
            if (leavingVariable == -1) {
                return null;
            }

            pivot(tableau, leavingVariable, enteringVariable);
        }
    }

    private static double[][] initializeTableau(float[] C, float[][] A, float[] B, int numVariables, int numConstraints) {
        double[][] tableau = new double[numConstraints + 1][numVariables + numConstraints + 1];

        for (int i = 0; i < numVariables; i++) {
            tableau[0][i] = -C[i];
        }

        for (int i = 0; i < numConstraints; i++) {
            for (int j = 0; j < numVariables; j++) {
                tableau[i + 1][j] = A[i][j];
            }
        }

        for (int i = 0; i < numConstraints; i++) {
            tableau[i + 1][numVariables + i] = 1;
        }

        for (int i = 0; i < numConstraints; i++) {
            tableau[i + 1][numVariables + numConstraints] = B[i];
        }

        return tableau;
    }
    private static int findEnteringVariable(double[][] tableau) {
        int enteringVariable = -1;
        double minCoefficient = 0;
        for (int i = 0; i < tableau[0].length - 1; i++) {
            if (tableau[0][i] < minCoefficient) {
                minCoefficient = tableau[0][i];
                enteringVariable = i;
            }
        }
        return enteringVariable;
    }

    private static int findLeavingVariable(double[][] tableau, int enteringVariable) {
        int leavingVariable = -1;
        double minRatio = Double.MAX_VALUE;
        for (int i = 1; i < tableau.length; i++) {
            if (tableau[i][enteringVariable] > EPSILON) {
                double ratio = tableau[i][tableau[i].length - 1] / tableau[i][enteringVariable];
                if (ratio < minRatio) {
                    minRatio = ratio;
                    leavingVariable = i;
                }
            }
        }
        return leavingVariable;
    }

    private static void pivot(double[][] tableau, int leavingVariable, int enteringVariable) {
        double pivotElement = tableau[leavingVariable][enteringVariable];

        for (int j = 0; j < tableau[leavingVariable].length; j++) {
            tableau[leavingVariable][j] /= pivotElement;
        }

        for (int i = 0; i < tableau.length; i++) {
            if (i != leavingVariable) {
                double factor = tableau[i][enteringVariable];
                for (int j = 0; j < tableau[i].length; j++) {
                    tableau[i][j] -= factor * tableau[leavingVariable][j];
                }
            }
        }
    }

    public static void main(String[] args) {
        boolean isMaximization = true;
        Scanner scanner = new Scanner(System.in);
        int numVariables, numConstraints;
        numVariables = scanner.nextInt();
        numConstraints = scanner.nextInt();
        float[] C = new float[numVariables];
        float[][] A = new float[numConstraints][numVariables];
        float[] B = new float[numConstraints];
        for (int i = 0; i < numVariables; i++) {
            C[i] = scanner.nextFloat();
        }
        for (int i = 0; i < numConstraints; i++) {
            for (int j = 0; j < numVariables; j++) {
                A[i][j] = scanner.nextFloat();
            }
        }
        for (int i = 0; i < numConstraints; i++) {
            B[i] = scanner.nextFloat();
        }
        if (!isMaximization) {
            for (int i = 0; i < C.length; i++) {
                C[i] *= -1;
            }
        }
        if (isMaximization) {
            System.out.print("Maximize z = ");
        } else {
            System.out.print("Minimize z = ");
        }
        System.out.print(C[0] + "x" + 1);
        for (int i = 1; i < C.length; i++) {
            if (C[i] >= 0) {
                System.out.print(" + ");
            }
            System.out.print(C[i] + "x" + (i + 1));
        }
        System.out.println();
        System.out.println("Subject to the constraints:");
        for (int i = 0; i < numConstraints; i++) {
            System.out.print(A[i][0] + "x" + 1);
            for (int j = 1; j < numVariables; j++) {
                if (A[i][j] >= 0) {
                    System.out.print(" + ");
                }
                System.out.print(A[i][j] + "x" + (j + 1));
            }
            System.out.println(" <= " + B[i]);
        }
        Tableau result = simplexMethod(C, A, B, numVariables, numConstraints);

        if (result == null) {
            System.out.println("The method is not applicable!");
        } else {
            float[] solution = new float[numVariables];
            for (int i = 1; i <= numConstraints; i++) {
                for (int j = 0; j < numVariables; j++) {
                    if (result.getElement(i, j) == 1) {
                        solution[j] = (float) result.getElement(i, numVariables + numConstraints);
                        break;
                    }
                }
            }

            System.out.println("Solution:");
            for (int i = 0; i < solution.length; i++) {
                System.out.println("x" + (i + 1) + " = " + solution[i]);
            }
            System.out.println("Value: ");
            System.out.println(result.getElement(0, numVariables + numConstraints));
        }
        scanner.close();
    }
}
