/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mn_ejemplo1;

/**
 *
 * @author kotli
 */
public class MN_Ejemplo5_2 {
    public static void main(String[] args) {
        int[][] matriz1 = {
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1},
    
        };
        int[][] matriz2 = {
            {9, 5, 5, 5},
            {9, 5, 5, 5},
            {7, 5, 5, 5},
            {6, 5, 5, 5}
        };
        if (tienenDime(matriz1, matriz2)) {
            System.out.println("Las matrices tienen las mismas dimensiones.");
            int[][] r = restaM(matriz2, matriz1);
            System.out.println("El resultado de la resta es:");
            imprimeM(r);
        } else {
            System.out.println("Lamentablemente no tienen las mismas dimensiones");
        }
    }
    public static boolean tienenDime(int[][] m1, int[][] m2) {
        if (m1.length != m2.length) return false;

        for (int i = 0; i < m1.length; i++) {
            if (m1[i].length != m2[i].length) return false;
        }
        return true;
    }
    public static int[][] restaM(int[][] m1, int[][] m2) {
        int filas = m1.length;
        int columnas = m1[0].length;
        int[][] r = new int[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                r[i][j] = m1[i][j] - m2[i][j];
            }
        }
        return r;
    }
  public static void imprimeM(int[][] matriz) {
      int i =0;
 
      for (int[] fila : matriz) {
            for (int valor : fila) {
                System.out.print(valor + " ");
            }
            System.out.println();
        }
    }
}

