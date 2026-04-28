/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mn_ejemplo1;
 import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
/**
 *
 * @author kotli
 */
public class MN_Ejemploo9 {
    private double[][] matriz;
    private String[] variables = {"x", "y", "z"};
    private Scanner scanner;
    
    public MN_Ejemploo9() {
        matriz = new double[3][4];
        scanner = new Scanner(System.in);
    }
    public static void main(String[] args) {
        MN_Ejemploo9 sistema = new MN_Ejemploo9();
        sistema.ejecutar();
    }
    public void ejecutar() {
        System.out.println("=== SISTEMA 3x3 METODO GAUSS ===\n");
        cEcua();
        System.out.println("\n--- MATRIZ AUMENTADA INICIAL ---");
        mostrarMatriz();
        System.out.println("\n--- PROCESO DE ELIMINACIÓN DE GAUSS ---");
        eliminacionGauss();
        System.out.println("\n--- MATRIZ FINAL (FORMA ESCALONADA) ---");
        mostrarMatriz();
        System.out.println("\n--- SISTEMA DE ECUACIONES FINAL ---");
        mostrarSistemaFinal();
        System.out.println("\n--- SOLUCIÓN DEL SISTEMA ---");
        resolverSistema();
        scanner.close();
    }
    private void cEcua() {
        System.out.println("Ingrese las tres ecuaciones en el formato: ax + by + cz = d");
        System.out.println("Ejemplo: 2x + 3y - z = 5");
        System.out.println("Nota: Use siempre las variables x, y, z en ese orden\n");
        for (int i = 0; i < 3; i++) {
            boolean ecuacionValida = false;
            while (!ecuacionValida) {
                System.out.print("Ecuación " + (i + 1) + ": ");
                String ecuacion = scanner.nextLine().trim();
                if (VALPARecua(ecuacion, i)) {
                    ecuacionValida = true;
                } else {
                    System.out.println("ERROR: Formato invalido. Use el formato: ax + by + cz = d");
                }
            }
        }
        System.out.println("\n✓ Todas las ecuaciones han sido validadas correctamente");
        verificarConsistencia();
    }
    private boolean VALPARecua(String ecuacion, int fila) {
        ecuacion = ecuacion.replaceAll("\\s+", "").toLowerCase();
        if (!ecuacion.contains("=")) {
            return false;
        }
        String[] partes = ecuacion.split("=");
        if (partes.length != 2) {
            return false;
        }
        String ladoIzquierdo = partes[0];
        String ladoDerecho = partes[1];
        try {
            matriz[fila][3] = Double.parseDouble(ladoDerecho);
            matriz[fila][0] = extraerCoeficiente(ladoIzquierdo, "x");
            matriz[fila][1] = extraerCoeficiente(ladoIzquierdo, "y");
            matriz[fila][2] = extraerCoeficiente(ladoIzquierdo, "z");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private double extraerCoeficiente(String expresion, String variable) {
        if (!expresion.startsWith("+") && !expresion.startsWith("-")) {
            expresion = "+" + expresion;
        }
        String patron = "([+-]?[^+-]*)" + variable;
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(expresion);
        if (matcher.find()) {
            String coeficienteStr = matcher.group(1);
            coeficienteStr = coeficienteStr.replace(variable, "");
            if (coeficienteStr.equals("+") || coeficienteStr.isEmpty()) {
                return 1.0;
            } else if (coeficienteStr.equals("-")) {
                return -1.0;
            } else {
                return Double.parseDouble(coeficienteStr);
            }
        } else {
            return 0.0;
        }
    }
    private void verificarConsistencia() {
        boolean todasTienenVariables = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (matriz[i][0] == 0 && matriz[i][1] == 0 && matriz[i][2] == 0) {
                    System.out.println("ADVERTENCIA: La ecuacion " + (i + 1) + " no tiene variables validas");
                    todasTienenVariables = false;
                }
            }
        }
        if (todasTienenVariables) {
            System.out.println("Bien todas las ecuaciones contienen las variables x, y, z");
        }
    }
    private void mostrarMatriz() {
        System.out.println("    x      y      z    |  b");
        System.out.println("-------------------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("[ ");
            for (int j = 0; j < 4; j++) {
                if (j == 3) {
                    System.out.printf("| %6.2f", matriz[i][j]);
                } else {
                    System.out.printf("%6.2f ", matriz[i][j]);
                }
            }
            System.out.println(" ]");
        }
    }
    private void eliminacionGauss() {
        for (int k = 0; k < 3; k++) {
            int filaPivote = encontrarPivote(k);
            if (filaPivote != k) {
                intercambiarFilas(k, filaPivote);
                System.out.println("\nIntercambio de filas " + (k + 1) + " y " + (filaPivote + 1) + ":");
                mostrarMatriz();
            }
            if (Math.abs(matriz[k][k]) < 1e-10) {
                System.out.println("ADVERTENCIA: Pivote muy pequeño o cero en posicion [" + k + "][" + k + "]");
                continue;
            }
            for (int i = k + 1; i < 3; i++) {
                if (matriz[i][k] != 0) {
                    double factor = matriz[i][k] / matriz[k][k];
                    System.out.println("\nEliminando fila " + (i + 1) + " usando fila " + (k + 1) + 
                                     " (factor: " + String.format("%.3f", factor) + "):");
                    for (int j = k; j < 4; j++) {
                        matriz[i][j] -= factor * matriz[k][j];
                    }
                    mostrarMatriz();
                }
            }
        }
    }
    private int encontrarPivote(int columna) {
        int filaPivote = columna;
        double maxValor = Math.abs(matriz[columna][columna]);
        for (int i = columna + 1; i < 3; i++) {
            if (Math.abs(matriz[i][columna]) > maxValor) {
                maxValor = Math.abs(matriz[i][columna]);
                filaPivote = i;
            }
        }
        return filaPivote;
    }
    private void intercambiarFilas(int fila1, int fila2) {
        for (int j = 0; j < 4; j++) {
            double temp = matriz[fila1][j];
            matriz[fila1][j] = matriz[fila2][j];
            matriz[fila2][j] = temp;
        }
    }
    private void mostrarSistemaFinal() {
        for (int i = 0; i < 3; i++) {
            boolean primerTermino = true;
            for (int j = 0; j < 3; j++) {
                if (Math.abs(matriz[i][j]) > 1e-10) {
                    if (!primerTermino) {
                        if (matriz[i][j] > 0) {
                            System.out.print(" + ");
                        } else {
                            System.out.print(" - ");
                            matriz[i][j] = Math.abs(matriz[i][j]);
                        }
                    } else if (matriz[i][j] < 0) {
                        System.out.print("-");
                        matriz[i][j] = Math.abs(matriz[i][j]);
                    }
                    if (Math.abs(matriz[i][j] - 1.0) > 1e-10) {
                        System.out.printf("%.3f", matriz[i][j]);
                    }
                    System.out.print(variables[j]);
                    primerTermino = false;
                }
            }
            if (primerTermino) {
                System.out.print("0");
            }
            System.out.printf(" = %.3f%n", matriz[i][3]);
        }
    }
    private void resolverSistema() {
        double[] solucion = new double[3];
        for (int i = 2; i >= 0; i--) {
            solucion[i] = matriz[i][3];
            for (int j = i + 1; j < 3; j++) {
                solucion[i] -= matriz[i][j] * solucion[j];
            }
            if (Math.abs(matriz[i][i]) > 1e-10) {
                solucion[i] /= matriz[i][i];
            } else {
                System.out.println("El sistema no tiene solucion unica.");
                return;
            }
        }
        System.out.println("Solución del sistema:");
        for (int i = 0; i < 3; i++) {
            System.out.printf("%s = %.6f%n", variables[i], solucion[i]);
        }
        System.out.println("\n--- VERIFICACIÓN DE LA SOLUCION ---");
        verificarSolucion(solucion);
    }
    private void verificarSolucion(double[] solucion) {
        System.out.println("Sustituyendo los valores en las ecuaciones originales:");
        System.out.println("(Nota: Para una verificacion completa, se necesitaria guardar la matriz original)");
    }
}
