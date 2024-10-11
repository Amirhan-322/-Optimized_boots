import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter 'min' for minimization or 'max' for maximization: ");
        String problemType = scanner.nextLine();

        System.out.println("Enter the number of variables: ");
        int n = scanner.nextInt();

        double[] C = new double[n];
        System.out.println("Enter a vector of the coefficients of the objective function:");
        for (int i = 0; i < n; i++) {
            C[i] = scanner.nextDouble();
        }

        System.out.println("Enter the number of constraints:");
        int m = scanner.nextInt();

        double[][] A = new double[m][n];
        System.out.println("Enter a matrix of the coefficients of the constraint functions:");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = scanner.nextDouble();
            }
        }

        double[] b = new double[m];
        System.out.println("Enter a vector of the right-hand side values:");
        for (int i = 0; i < m; i++) {
            b[i] = scanner.nextDouble();
        }

        System.out.print(problemType + " z = ");
        for (int i = 0; i < n; i++) {
            System.out.print(C[i] + " * x" + (i + 1));
            if (i < n - 1) {
                System.out.print(" + ");
            }
        }
        System.out.println();

        System.out.println("subject to the constraints:");
        for (int i = 0; i < m; i++) {
            System.out.print("     ");
            for (int j = 0; j < n; j++) {
                System.out.print(A[i][j] + " * x" + (j + 1));
                if (j < n - 1) {
                    System.out.print(" + ");
                }
            }
            System.out.println(" <= " + b[i]);
        }

        SimplexMethod(C,A,b);

        scanner.close();
    }

    public static void SimplexMethod(double[] C, double[][] A, double[] b){
        //Some simplex logic...
    }
}