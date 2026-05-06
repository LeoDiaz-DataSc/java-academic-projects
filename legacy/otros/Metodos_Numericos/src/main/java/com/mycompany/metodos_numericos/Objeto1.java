/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.metodos_numericos;

import java.util.Scanner;

/**
 *
 * @author kotli
 */

public class Objeto1 {

    public static void main(String[] args) {
        System.out.println("Ejemplo 1");
        float conBase = 3.6F;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingresa velocidad");
        float intVelocidad;
        intVelocidad = Float.parseFloat(scanner.nextLine());

        float velocidad = (float)Math.floor((intVelocidad / conBase) * 100) / 100;

        System.out.println("Ingresa la masa");
        float intMasa;
        intMasa = Float.parseFloat(scanner.nextLine());

        float momentum = intMasa * velocidad;
        System.out.println("P = " + momentum + " kgm/s");

        scanner.close(); 
    }
}