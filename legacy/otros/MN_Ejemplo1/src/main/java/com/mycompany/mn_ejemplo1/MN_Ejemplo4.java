package com.mycompany.mn_ejemplo1;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kotli
 */
public class MN_Ejemplo4 {
    
      public static void main(String[] args) {
        mBandeada();
    }
    public static void mBandeada() {
        System.out.println("Matriz Bandeada");
        int[][] n1 = { { 1, 1, 1, 1 }, { 1, 1, 1, 1 },
                                          { 1, 1, 1, 1 }, { 1, 1, 1, 1 } };
        
        for (int f = 0; f < n1.length; f++) {
            for (int c = 0; c < n1[0].length; c++) {
                if ((f == c) || (f == (c+1)) || (f == (c - 1))) {
                    System.out.print(n1[f][c]);
                }
                else {
                    System.out.print("*");
                }
            }
            System.out.println();
        }
    }
    
  
}

