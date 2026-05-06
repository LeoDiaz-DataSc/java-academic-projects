/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mn_ejemplo1;

/**
 *
 * @author kotli
 */
public class MN_Ejemplo2 {
    public static void main(String[] args) {
        MD();
    }

    static void MD() {
        System.out.println("Matriz Diagonal");

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
    }
}
