// This code is a Java implementation of the Simplex method for linear programming.
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    static class Tableau {
        float[][] data;

        Tableau(int rows, int columns) {
            data = new float[rows][columns];
        }

        float[] getRow(int index) {
            return data[index];
        }

        void setRow(int index, float[] row) {
            data[index] = row;
        }

        float getElement(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        void setElement(int rowIndex, int columnIndex, float value) {
            data[rowIndex][columnIndex] = value;
        }
    }

    static Tableau recSimplexMethod(Tableau oldTableau, int numVariables, int numConstraints) {
        boolean hasNegative = false;
        for (int i = 0; i < numVariables; i++) {
            if (oldTableau.getElement(0, i) < 0) {
                hasNegative = true;
                break;
            }
        }
        if (!hasNegative) {
            return oldTableau;
        } else {
            Tableau newTableau = oldTableau;
            // Find pivot column
            int pivotColumn = 0;
            for (int i = 0; i < numVariables; i++) {
                if (oldTableau.getElement(0, i) < oldTableau.getElement(0, pivotColumn)) {
                    pivotColumn = i;
                }
            }
            // Find solution ratio and store it in the last column of newTableau
            boolean isUnbounded = true;
            for (int i = 0; i < numConstraints; i++) {
                if (oldTableau.getElement(i + 1, pivotColumn) > 0) {
                    newTableau.setElement(i + 1, 2 * numVariables, oldTableau.getElement(i + 1, numVariables) / oldTableau.getElement(i + 1, pivotColumn));
                    isUnbounded = false;
                } else {
                    newTableau.setElement(i + 1, 2 * numVariables, Float.MAX_VALUE);
                }
            }
            if (isUnbounded) {
                System.out.println("The problem is unbounded.");
                System.exit(0);
            }
            // Find pivot row using the smallest ratio
            int pivotRow = 1;
            for (int i = 0; i < numConstraints; i++) {
                if (newTableau.getElement(i + 1, 2 * numVariables) < newTableau.getElement(pivotRow, 2 * numVariables)) {
                    pivotRow = i + 1;
                }
            }

            // Divide every variable in pivot row by pivot element
            float pivotElement = newTableau.getElement(pivotRow, pivotColumn);
            for (int i = 0; i <= numVariables; i++) {
                newTableau.setElement(pivotRow, i, newTableau.getElement(pivotRow, i) / pivotElement);
            }

            // Subtract pivot row from every other row
            for (int i = 0; i <= numConstraints; i++) {
                if (i != pivotRow) {
                    float factor = newTableau.getElement(i, pivotColumn);
                    for (int j = 0; j <= numVariables; j++) {
                        newTableau.setElement(i, j, newTableau.getElement(i, j) - factor * newTableau.getElement(pivotRow, j));
                    }
                }
            }
            return recSimplexMethod(newTableau, numVariables, numConstraints);
        }
    }

    static Tableau simplexMethod(float[] C, float[][] A, float[] B, int numVariables, int numConstraints) {
        Tableau tableau = new Tableau(numConstraints + 1, 2 * numVariables + 1);

        // Initialize z-row
        for (int i = 0; i < numVariables; i++) {
            tableau.setElement(0, i, -C[i]);
        }

        // Initialize all constraint rows, their solutions and slack variables
        for (int i = 0; i < numConstraints; i++) {
            for (int j = 0; j < numVariables; j++) {
                tableau.setElement(i + 1, j, A[i][j]);
            }
            // Add identity matrix for slack variables
            for (int j = numVariables; j < 2 * numVariables; j++) {
                if (j == numVariables + i) {
                    tableau.setElement(i + 1, j, 1);
                } else {
                    tableau.setElement(i + 1, j, 0);
                }
            }
            tableau.setElement(i + 1, numVariables, B[i]);
        }
        return recSimplexMethod(tableau, numVariables, numConstraints);
    }

    public static void main(String[] args) {
        // if maximization, boolean max = true
        // if value = false, multiply every value in vector C by -1
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
        // printing the problem
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

        // Extract the solution vector
        float[] solution = new float[numVariables];
        for (int i = 1; i <= numConstraints; i++) {
            for (int j = 0; j < numVariables; j++) {
                if (result.getElement(i, j) == 1) {
                    solution[j] = result.getElement(i, numVariables);
                    break;
                }
            }
        }

        // Print the solution vector
        System.out.println("Solution:");
        for (int i = 0; i < solution.length; i++) {
            System.out.println("x" + (i + 1) + " = " + solution[i]);
        }
        System.out.println("Value: ");
        System.out.println(result.getElement(0, numVariables));
        scanner.close();
    }
}