/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mn_ejemplo1;

/**
 *
 * @author kotli
 */
     
import java.util.Scanner;
public class MN_Ejemploo11 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double[][] matriz = new double[3][3];
        System.out.println("=== inversion matriz gauss jordan ===\n");
        System.out.println("Ingresa datos de matriz");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print("Elemento [" + (i+1) + "][" + (j+1) + "]: ");
                matriz[i][j] = scanner.nextDouble();
            }
        }
        double[][] matrizOriginal = copiarMatriz(matriz);
        if (determinante3x3(matriz) == 0) {
            System.out.println("\nERROR: La matriz no es invertible (determinante = 0)");
            return;
        }
        System.out.println("\n1. Matriz original con matriz identidad");
        MatrizAumento(matriz, crearMatrizIdentidad());
        System.out.println("\n2. Desarrollo gaussJordan");
        double[][] matrizInversa = inversaGaussJordan(matriz);
        System.out.println("\n3. matriz inversa:");
        MostrarMat(matrizInversa);
        System.out.println("\n4. matriz origianl x matriz inversa:");
        double[][] verificacion = multiplicarMatrices(matrizOriginal, matrizInversa);
        MostrarMat(verificacion);
        scanner.close();
    }
    public static double[][] inversaGaussJordan(double[][] matriz) {
        int n = matriz.length;
        double[][] aumentada = new double[n][2*n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                aumentada[i][j] = matriz[i][j];
                aumentada[i][j + n] = (i == j) ? 1.0 : 0.0;
            }
        }
        for (int i = 0; i < n; i++) {
            int pivoteMaximo = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(aumentada[k][i]) > Math.abs(aumentada[pivoteMaximo][i])) {
                    pivoteMaximo = k;
                }
            }
            if (pivoteMaximo != i) {
                double[] temp = aumentada[i];
                aumentada[i] = aumentada[pivoteMaximo];
                aumentada[pivoteMaximo] = temp;
                System.out.println("Intercambio de filas " + (i+1) + " y " + (pivoteMaximo+1) + ":");
                MatrizAumento(extraerMatrizA(aumentada), extraerMatrizB(aumentada));
            }
            double pivote = aumentada[i][i];
            if (pivote != 0) {
                for (int j = 0; j < 2*n; j++) {
                    aumentada[i][j] /= pivote;
                }
                System.out.println("Dividir fila " + (i+1) + " por " + String.format("%.3f", pivote) + ":");
                MatrizAumento(extraerMatrizA(aumentada), extraerMatrizB(aumentada));
            }
            for (int k = 0; k < n; k++) {
                if (k != i && aumentada[k][i] != 0) {
                    double factor = aumentada[k][i];
                    for (int j = 0; j < 2*n; j++) {
                        aumentada[k][j] -= factor * aumentada[i][j];
                    }
                    System.out.println("R" + (k+1) + " = R" + (k+1) + " - (" + 
                                     String.format("%.3f", factor) + ") * R" + (i+1) + ":");
                    MatrizAumento(extraerMatrizA(aumentada), extraerMatrizB(aumentada));
                }
            }
        }
        double[][] inversa = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inversa[i][j] = aumentada[i][j + n];
            }
        }
        
        return inversa;
    }
    public static double[][] crearMatrizIdentidad() {
        double[][] identidad = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                identidad[i][j] = (i == j) ? 1.0 : 0.0;
            }
        }
        return identidad;
    }
    public static void MostrarMat(double[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            System.out.print("[ ");
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.printf("%8.3f ", matriz[i][j]);
            }
            System.out.println("]");
        }
    }
    public static void MatrizAumento(double[][] matrizA, double[][] matrizB) {
        for (int i = 0; i < matrizA.length; i++) {
            System.out.print("[ ");
            for (int j = 0; j < matrizA[i].length; j++) {
                System.out.printf("%8.3f ", matrizA[i][j]);
            }
            System.out.print(" | ");
            for (int j = 0; j < matrizB[i].length; j++) {
                System.out.printf("%8.3f ", matrizB[i][j]);
            }
            System.out.println("]");
        }
        System.out.println();
    }
    public static double[][] copiarMatriz(double[][] matriz) {
        double[][] copia = new double[matriz.length][matriz[0].length];
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                copia[i][j] = matriz[i][j];
            }
        }
        return copia;
    }
    public static double[][] multiplicarMatrices(double[][] a, double[][] b) {
        int n = a.length;
        double[][] resultado = new double[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    resultado[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return resultado;
    }
    public static double determinante3x3(double[][] matriz) {
        return matriz[0][0] * (matriz[1][1] * matriz[2][2] - matriz[1][2] * matriz[2][1]) -
               matriz[0][1] * (matriz[1][0] * matriz[2][2] - matriz[1][2] * matriz[2][0]) +
               matriz[0][2] * (matriz[1][0] * matriz[2][1] - matriz[1][1] * matriz[2][0]);
    }
    public static double[][] extraerMatrizA(double[][] aumentada) {
        int n = aumentada.length;
        double[][] matrizA = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrizA[i][j] = aumentada[i][j];
            }
        }
        return matrizA;
    }
    public static double[][] extraerMatrizB(double[][] aumentada) {
        int n = aumentada.length;
        double[][] matrizB = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrizB[i][j] = aumentada[i][j + n];
            }
        }
        return matrizB;
    }
}