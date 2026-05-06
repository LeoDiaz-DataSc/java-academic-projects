/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mn_ejemplo1;

/**
 *
 * @author kotli
 */
public class MN_Ejemplo7 {
     public static void main(String[] args) {
        int[][] m1 = {
            {2, 0, 1},
            {3, 0, 0},
            {5, 1, 1}
        };
        int[][] tra = new int[3][3];
        int[][] aum = new int[3][6];
        System.out.println("Matriz Simetrica:");
        int i = 0;
        while (i < 3) {
            int j = 0;
            while (j < 3) {
                System.out.print(m1[i][j] + "\t");
                j++;
            }
            System.out.println();
            i++;
        }
        i = 0;
        while (i < 3) {
            int j = 0;
            while (j < 3) {
                tra[i][j] = m1[j][i];
                j++;
            }
            i++;
        }
        System.out.println("\nMatriz Transpuesta:");
        i = 0;
        while (i < 3) {
            int j = 0;
            while (j < 3) {
                System.out.print(tra[i][j] + "\t");
                j++;
            }
            System.out.println();
            i++;
        }
        i = 0;
        while (i < 3) {
            int j = 0;
            while (j < 3) {
                aum[i][j] = m1[i][j];
                aum[i][j + 3] = tra[i][j];
                j++;
            }
            i++;
        }
        System.out.println("\nMatriz Aumentada:");
        i = 0;
        while (i < 3) {
            int j = 0;
            while (j < 6) {
                System.out.print(aum[i][j] + "\t");
                j++;
            }
            System.out.println();
            i++;
        }
    }
}
