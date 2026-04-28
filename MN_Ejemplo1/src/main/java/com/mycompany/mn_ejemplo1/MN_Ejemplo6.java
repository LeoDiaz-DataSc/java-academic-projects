/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mn_ejemplo1;

/**
 *
 * @author kotli
 */
public class MN_Ejemplo6 {
 
    public static void main(String[] args) {
        System.out.println("Multiplicacion de dos matrices");
        int[][] m1 = {
            {2, 3, 4},
            {6, 6, 7},
            {5, 8, 9}
        };
        int[][] m2 = {
            {3, 3, 3},
            {3, 3, 3},
            {3, 3, 3}
        };
        int[][] mR = new int[3][3];
        int f = 0;
        while (f < 3) {
            int c = 0;
            while (c < 3) {
                int k = 0;
                while (k < 3) {
                    mR[f][c] += m1[f][k] * m2[k][c];
                    k++;
                }
                c++;
            }
            f++;
        }
        f = 0;
        while (f < 3) {
            int c = 0;
            while (c < 3) {
                System.out.print(mR[f][c] + "\t");
                c++;
            }
            System.out.println();
            f++;
        }
    }
}

