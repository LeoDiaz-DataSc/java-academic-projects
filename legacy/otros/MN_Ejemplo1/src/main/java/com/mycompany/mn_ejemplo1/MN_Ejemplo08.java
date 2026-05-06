/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mn_ejemplo1;

/**
 *
 * @author kotli
 */   import java.util.*;
import java.util.regex.*;

public class MN_Ejemplo08 {
 

    static String[] eq1Split;
    static String[] eq2Split;

    public static void main(String[] args) {
        EcuacionLinealEliminarE1();
    }

    static void EcuacionLinealEliminarE1() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Primer ejemplo de ecuaciones lineales 2x2");
        System.out.println("Ingresa la primer y segunda ecuación en formato: (+/-)(coeficiente)(incognita) espacio (+/-)(coeficiente)(incognita) espacio (=) (+/-)(constante)");
        System.out.println("Nota: oprime enter después de ingresar la primer ecuación");

        String eq1 = sc.nextLine();
        String eq2 = sc.nextLine();

        eq1Split = eq1.split(" ");
        eq2Split = eq2.split(" ");

        System.out.println("Inicia validación de ecuación lineal 2x2...");
        if (eq1Split.length == eq2Split.length) {
            System.out.println("Ecuaciones con tamaño igual, continua validación...");
            for (int i = 0; i < eq1Split.length; i++) {
                String inc1 = getIncognita(eq1Split[i]);
                String inc2 = getIncognita(eq2Split[i]);

                if (inc1.equals(inc2)) {
                    if (i == eq1Split.length - 1) {
                        System.out.println("Ecuaciones con incógnitas iguales, continua validación...");
                        int c1 = Integer.parseInt(getCoeficiente(eq1Split[0]));
                        int c2 = Integer.parseInt(getCoeficiente(eq2Split[0]));
                        ValidaPrimerCoeficiente(c1, c2);
                    }
                } else {
                    System.out.println("Incógnitas distintas, deben coincidir.");
                    System.out.println(inc1);
                    System.out.println(inc2);
                    return;
                }
            }
        } else {
            System.out.println("Ecuaciones de distinto tamaño.");
        }
    }

    static void ValidaPrimerCoeficiente(int c1, int c2) {
        System.out.println("Validando el primer coeficiente...");
        if (c1 > c2 && c1 % c2 == 0) {
            int constante = c1 / c2;
            System.out.println("Coeficiente válido, obteniendo constante...");
            ValidaSignoPrimerTermino(eq1Split[0], eq2Split[0], constante);
        } else if (c2 < c1) {
            System.out.println("c2 es menor a c1");
        } else if (c1 == c2) {
            System.out.println("c1 y c2 son iguales");
        }
    }

    static void ValidaSignoPrimerTermino(String t1, String t2, int constante) {
        List<String> eq2Vector = new ArrayList<>();
        System.out.println("Validando signos...");

        String signo1 = getSigno(t1);
        String signo2 = getSigno(t2);

        if (signo1.equals(signo2)) {
            System.out.println("Signos iguales, cambiando signo de la constante...");
            int constanteSigno = -constante;

            for (String item : eq2Split) {
                String coefStr = getCoeficiente(item);
                int coef = Integer.parseInt(coefStr);
                int coefNuevo = coef * constanteSigno;

                String nuevaParte = coefNuevo + getIncognita(item);
                eq2Vector.add(nuevaParte);
            }

            Reduccion(eq2Vector);
        }
    }

    static void Reduccion(List<String> eq2Vector) {
        List<String> eqFin = new ArrayList<>();
        System.out.println("Haciendo reducción...");

        for (int i = 0; i < eq1Split.length; i++) {
            int co1 = Integer.parseInt(getCoeficiente(eq1Split[i]));
            int co2 = Integer.parseInt(getCoeficiente(eq2Vector.get(i)));
            int suma = co1 + co2;
            if (suma != 0) {
                eqFin.add(suma + getIncognita(eq1Split[i]));
            }
        }
        DespejarValor(eqFin);
    }

    static void DespejarValor(List<String> eqFin) {
        System.out.println("Despejando variable...");
        if (eqFin.size() >= 2) {
            String primero = eqFin.get(0);
            String segundo = eqFin.get(1);

            String inc = getIncognita(primero);
            int co1 = Integer.parseInt(primero.replace(inc, ""));
            int co2 = Integer.parseInt(segundo);

            int resultado = co2 / co1;
            System.out.println("Resultado: " + inc + " = " + resultado);
        }
    }

    // Utilidades
    static String getIncognita(String term) {
        Matcher m = Pattern.compile("[a-zA-Z]$").matcher(term);
        return m.find() ? m.group() : "";
    }

    static String getCoeficiente(String term) {
        Matcher m = Pattern.compile("[-+]?\\d+").matcher(term);
        return m.find() ? m.group() : "0";
    }

    static String getSigno(String term) {
        Matcher m = Pattern.compile("^[+-]").matcher(term);
        return m.find() ? m.group() : "+";
    }
}


