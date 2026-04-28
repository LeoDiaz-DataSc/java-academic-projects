/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mn_ejemplo1;

/**
 *
 * @author kotli
 */
public class MN_Ejemplo5 {
       
     public static void main(String[] args) {
        mResta();
    }
    public static void mResta() {
        System.out.println("Restar matrices");
        int[][] m1 = { { 3, 3, 3, 3 }, { 3, 2, 3, 3 },
                                         { 5, 1, 1, 1 }, { 5, 5, 5, 5 } };
        int[][] m2 = { { 5, 5, 5, 5 }, { 5, 5, 5, 5 },
                                          { 5, 5, 5, 5 }, { 5, 5, 5, 5 } };
        int[][] restaM = new int[4][4];
        for (int f = 0; f < 4; f++) {
            for (int c = 0; c < 4; c++) {
                restaM[f][c] = m2[f][c] - m1[f][c];
            }
        }
        for (int f = 0; f < 4; f++) {
            for (int c = 0; c < 4; c++) {
                System.out.print(restaM[f][c]);
            }
            System.out.println();
        }
    }
    
   
}
