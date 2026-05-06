/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mn_ejemplo1;
public class MN_Ejemplo3 {
    public static void main(String[] args) {
        MI();
        MD();
    }

    static void MI() {
        System.out.println("Primer ejemplo de matriz");
        int[][] midentidad = {
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
        };
        for (int f = 0; f < midentidad.length; f++) {
            for (int c = 0; c < midentidad[f].length; c++) {
                if (f == c) {
                    System.out.print(midentidad[f][c] + " ");
                } else {
                    System.out.print("* ");
                }
            }
            System.out.println();
        }
     
        for (int f = 0; f < midentidad.length; f++) {
            for (int c = 0; c < midentidad[f].length; c++) {
                if (f == c) {
                    if (midentidad[f][c] == 1) {
                        System.out.print(midentidad[f][c] + " ");
                    } else {
                        System.out.println("No es una matriz identidad");
                        return;
                    }
                } else {
                    System.out.print("* ");
                }
            }
            
            System.out.println();
               System.out.println("Es una matriz identidad");
        }
        
    }
    static void MD() {
        System.out.println("Segundo Ejemplo matriz");
        int[][] mdiagonal = {
            {10, 11, 12, 13},
            {14, 15, 16, 17},
            {18, 19, 20, 21},
            {22, 23, 24, 25}
        };
        for (int f = 0; f < mdiagonal.length; f++) {
            for (int c = 0; c < mdiagonal[f].length; c++) {
                if (f == c) {
                    System.out.print(mdiagonal[f][c] + " ");
                } else {
                    System.out.print("* ");
                }
            }
            System.out.println();
        }
       
        for (int f = 0; f < mdiagonal.length; f++) {
            for (int c = 0; c < mdiagonal[f].length; c++) {
                if (f == c) {
                    if (mdiagonal[f][c] == 1) {
                        System.out.print(mdiagonal[f][c] + " ");
                    } else {
                        System.out.println("No es una matriz identidad");
                        return;
                    }
                } else {
                    System.out.print("* ");
                }
            }
            System.out.println();
        }
        
    }
}
