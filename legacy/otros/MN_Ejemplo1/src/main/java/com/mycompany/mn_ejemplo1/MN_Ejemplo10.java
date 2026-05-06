/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mn_ejemplo1;
    import java.util.Scanner;
public class MN_Ejemplo10 {
    private static final double EPSILON = 1e-10;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MN_Ejemplo10 a = new MN_Ejemplo10();
            System.out.println("=== Ecuaciones 3x3 ===");
        System.out.println("Por metodo Gauss Jordan");
        System.out.println();
        double[][] matriz = new double[3][4];
        a.leerSistema(scanner, matriz);
        System.out.println("\nMatriz aumentada inicial:");
        a.mostrarMatriz(matriz);
        if (a.gaussJordan(matriz)) {
            System.out.println("\nMatriz reducida (forma escalonada reducida):");
            a.mostrarMatriz(matriz);
            a.mostrarSolucion(matriz);
        } else {
            System.out.println("\nEl sistema no tiene solución única.");
        }
        scanner.close();
    }
    public void leerSistema(Scanner scanner, double[][] matriz) {
        System.out.println("Ingrese los coeficientes del sistema de ecuaciones:");
        System.out.println("Formato: ax + by + cz = k");
        System.out.println();
        for (int i = 0; i < 3; i++) {
            System.out.println("Ecuacion " + (i + 1) + ":");
            System.out.print("  Coeficiente de x: ");
            matriz[i][0] = scanner.nextDouble(); 
            System.out.print("  Coeficiente de y: ");
            matriz[i][1] = scanner.nextDouble();
            System.out.print("  Coeficiente de z: ");
            matriz[i][2] = scanner.nextDouble();
            System.out.print("  Termino independiente K: ");
            matriz[i][3] = scanner.nextDouble();
            System.out.println();
        }
        verificarSistema(matriz);
    }
    public void verificarSistema(double[][] matriz) {
        System.out.println("Verificando sistema de ecuaciones...");
        if (matriz.length != 3 || matriz[0].length != 4) {
            throw new IllegalArgumentException("El sistema debe tener exactamente 3 ecuaciones con 3 incognitas.");
        }
        boolean sistemaValido = false;
        for (int i = 0; i < 3; i++) {
            boolean ecuacionNoTrivial = false;
            for (int j = 0; j < 3; j++) {
                if (Math.abs(matriz[i][j]) > EPSILON) {
                    ecuacionNoTrivial = true;
                    break;
                }
            }
            if (ecuacionNoTrivial) {
                sistemaValido = true;
                break;
            }
        }
        if (!sistemaValido) {
            throw new IllegalArgumentException("El sistema no contiene ecuaciones validas.");
        }
        System.out.println("Todo bien");
        System.out.println("Tres ecuaciones con X,Y,Z");
        System.out.println("El formato esta bien implementado");
    }
    public boolean gaussJordan(double[][] matriz) {
        System.out.println("\nIniciando modo chamba");
        int n = matriz.length;
        for (int i = 0; i < n; i++) {
            int maxFila = encontrarPivote(matriz, i);
            if (maxFila != i) {
                intercambiarFilas(matriz, i, maxFila);
                System.out.println("Paso " + (i + 1) + "a: Intercambiando filas " + (i + 1) + " y " + (maxFila + 1));
                mostrarMatriz(matriz);
            }
            if (Math.abs(matriz[i][i]) < EPSILON) {
                return false;
            }
            if (Math.abs(matriz[i][i] - 1.0) > EPSILON) {
                double pivote = matriz[i][i];
                for (int j = 0; j < 4; j++) {
                    matriz[i][j] /= pivote;
                }
                System.out.println("Paso " + (i + 1) + "b: Normalizando fila " + (i + 1) + " (dividiendo por " + 
                                 String.format("%.3f", pivote) + ")");
                mostrarMatriz(matriz);
            }
            for (int k = 0; k < n; k++) {
                if (k != i && Math.abs(matriz[k][i]) > EPSILON) {
                    double factor = matriz[k][i];
                    for (int j = 0; j < 4; j++) {
                        matriz[k][j] -= factor * matriz[i][j];
                    }
                    System.out.println("Paso " + (i + 1) + "c: Eliminando elemento [" + (k + 1) + "][" + (i + 1) + 
                                     "] (factor: " + String.format("%.3f", factor) + ")");
                    mostrarMatriz(matriz);
                }
            }
        }
        return true;
    }
    private int encontrarPivote(double[][] matriz, int col) {
        int maxFila = col;
        for (int i = col + 1; i < matriz.length; i++) {
            if (Math.abs(matriz[i][col]) > Math.abs(matriz[maxFila][col])) {
                maxFila = i;
            }
        }
        return maxFila;
    }
    private void intercambiarFilas(double[][] matriz, int fila1, int fila2) {
        double[] temp = matriz[fila1];
        matriz[fila1] = matriz[fila2];
        matriz[fila2] = temp;
    }
    public void mostrarMatriz(double[][] matriz) {
        System.out.println("----");
        for (int i = 0; i < matriz.length; i++) {
            System.out.print("/ ");
            for (int j = 0; j < matriz[i].length; j++) {
                if (j == 3) {
                    System.out.print("_ ");
                }
                System.out.printf("%8.3f ", matriz[i][j]);
            }
            System.out.println("_");
        }
        System.out.println("└-----");
        System.out.println();
    }
    public void mostrarSolucion(double[][] matriz) {
        System.out.println("Resultado..");
        System.out.println();
        String[] variables = {"x", "y", "z"};
        for (int i = 0; i < 3; i++) {
            System.out.printf("%s = %.6f\n", variables[i], matriz[i][3]);
        }
        System.out.println();
        System.out.println("Verificacion:");
        System.out.printf("x = %.6f\n", matriz[0][3]);
        System.out.printf("y = %.6f\n", matriz[1][3]);
        System.out.printf("z = %.6f\n", matriz[2][3]);
        System.out.println("\nSistema original con valores sustituidos:");
        System.out.println("(Checar que las ecuaciones cumplan)");
    }
    public void resolverEjemplo() {
        System.out.println("--Ejemplo--");
        System.out.println("Sistema:");
        System.out.println("2x + 3y - z = 1");
        System.out.println("4x + 4y + z = 7");
        System.out.println("2x - 3y + z = 5");
        System.out.println();
        double[][] matriz = {
            {2, 3, -1, 1},
            {4, 4, 1, 7},
            {2, -3, 1, 5}
        };
        System.out.println("Matriz aumentada inicial:");
        mostrarMatriz(matriz);
        if (gaussJordan(matriz)) {
            System.out.println("Matriz reducida:");
            mostrarMatriz(matriz);
            mostrarSolucion(matriz);
        } else {
            System.out.println("El sistema no tiene solucion unica.");
        }
    }
}
