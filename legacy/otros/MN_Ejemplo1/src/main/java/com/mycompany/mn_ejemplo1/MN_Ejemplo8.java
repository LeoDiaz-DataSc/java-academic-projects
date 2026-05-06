/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mn_ejemplo1;

/**
 *
 * @author kotli
 */
    import java.util.*;
import java.util.regex.*;
import java.util.Scanner;

public class MN_Ejemplo8 {

    private String n1;
    private String[] ec1;
    private String n2;
    private String[] ec2;
    private Scanner scanner;
    
    public MN_Ejemplo8() {
        scanner = new Scanner(System.in);
    }
    
    public void ecuacion2x2() {
        System.out.println("Este es un codigo basico para ecuaciones 2x2");
        System.out.println("Las ecuaciones tienen que seguir este formato: (+/-)(coeficiente)(incognita)espacio(+/-)(coeficiente)(incognita)espacio(=)(+/-)(constante)");
        System.out.println("Oprimir enter despues de escribir ecuacion");
        
        n1 = scanner.nextLine();
        n2 = scanner.nextLine();
        
        ec1 = n1.split(" ");
        ec2 = n2.split(" ");
        
        System.out.println("Checando que la ecuacion sea lineal 2*2...");
        
        if (ec1.length == ec2.length) {
            System.out.println("Tienen el mismo tamaño, continua chequeo...");
            
            boolean incognitasIguales = true;
            for (int i = 0; i < ec1.length; i++) {
                String incognita1 = extraerIncognita(ec1[i]);
                String incognita2 = extraerIncognita(ec2[i]);
                
                if (!incognita1.equals(incognita2)) {
                    System.out.println("Incognitas distintas, las incognitas de la primer ecuacion deben ser iguales a las de la segunda");
                    System.out.println("Ecuación 1, término " + i + ": " + incognita1);
                    System.out.println("Ecuación 2, término " + i + ": " + incognita2);
                    incognitasIguales = false;
                    break;
                }
            }
            
            if (incognitasIguales) {
                System.out.println("Ecuaciones con incognitas iguales, continua chequeo...");
                int c1 = extraerPrimerCoeficiente(ec1[0]);
                int c2 = extraerPrimerCoeficiente(ec2[0]);
                validaPrimerCoeficiente(c1, c2);
            }
        } else {
            System.out.println("Ecuaciones de diferente tamaño, deben de contener el mismo");
        }
    }
    
    private String extraerIncognita(String termino) {
        Pattern pattern = Pattern.compile("[a-zA-Z]$");
        Matcher matcher = pattern.matcher(termino);
        return matcher.find() ? matcher.group() : "";
    }
    
    private int extraerPrimerCoeficiente(String termino) {
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(termino);
        return matcher.find() ? Integer.parseInt(matcher.group()) : 0;
    }
    
    private void validaPrimerCoeficiente(int c1, int c2) {
        System.out.println("Analizando el coeficiente de las 2 ecuaciones...");
        
        if (c1 > c2) {
            double constante = (double) c1 / c2;
            if (c1 == (constante * c2)) {
                System.out.println("El coeficiente de la primer ecuacion es mayor que el de la segunda");
                int convertirConstante = (int) constante;
                validaSignoPrimerTermino(ec1[0], ec2[0], convertirConstante);
            }
        } else if (c2 > c1) {
            System.out.println("c2 es mayor a c1");
            double constante = (double) c2 / c1;
            if (c2 == (constante * c1)) {
                System.out.println("El coeficiente de la segunda ecuación es mayor que el de la primera");
                int convertirConstante = (int) constante;
                validaSignoPrimerTermino(ec2[0], ec1[0], convertirConstante);
            }
        } else {
            System.out.println("c1 y c2 son iguales");
            validaSignoPrimerTermino(ec1[0], ec2[0], 1);
        }
    }
    
    private void validaSignoPrimerTermino(String primerTerminoEq1, String primerTerminoEq2, int constante) {
        List<String> eq2Vector = new ArrayList<>();
        System.out.println("Validando signo del primer termino de ambas ecuaciones...");
        
        String signo1 = extraerSigno(primerTerminoEq1);
        String signo2 = extraerSigno(primerTerminoEq2);
        
        if (signo1.equals(signo2)) {
            System.out.println("Signos iguales de ambas ecuaciones, cambiando signo de la constante y afectando la segunda ecuacion");
            String cambiaSigno = signo2.equals("+") ? "-" : "+";
            
            for (String item : ec2) {
                int coeficiente = extCoefComp(item);
                int constanteSigno = Integer.parseInt(cambiaSigno + constante);
                int coeficienteAfectado = coeficiente * constanteSigno;
                
                String pattern = extrCoeficienteS(item);
                eq2Vector.add(item.replace(pattern, String.valueOf(coeficienteAfectado)));
            }
            
            reduccion(eq2Vector);
        } else {
            System.out.println("Signos diferentes, multiplicando directamente");
            for (String item : ec2) {
                int coeficiente = extCoefComp(item);
                int coeficienteAfectado = coeficiente * constante;
                
                String pattern = extrCoeficienteS(item);
                eq2Vector.add(item.replace(pattern, String.valueOf(coeficienteAfectado)));
            }
            
            reduccion(eq2Vector);
        }
    }
    
    private String extraerSigno(String termino) {
        Pattern pattern = Pattern.compile("^\\+|^-");
        Matcher matcher = pattern.matcher(termino);
        return matcher.find() ? matcher.group() : "+";
    }
    
    private int extCoefComp(String termino) {
        Pattern pattern = Pattern.compile("(\\+|-|)\\d{1,}");
        Matcher matcher = pattern.matcher(termino);
        return matcher.find() ? Integer.parseInt(matcher.group()) : 0;
    }
    
    private String extrCoeficienteS(String termino) {
        Pattern pattern = Pattern.compile("(\\+|-|)\\d{1,}");
        Matcher matcher = pattern.matcher(termino);
        return matcher.find() ? matcher.group() : "";
    }
    
    private void reduccion(List<String> eq2Vector) {
        List<String> eqFin = new ArrayList<>();
        System.out.println("Haciendo reduccion para obtener solo una ecuacion");
        
        for (int i = 0; i < ec1.length; i++) {
            int co1 = extCoefComp(ec1[i]);
            int co2 = extCoefComp(eq2Vector.get(i));
            int operacion = co1 + co2;
            
            if (operacion != 0) {
                String variable = extVal(ec1[i]);
                eqFin.add(operacion + variable);
            }
        }
        
        desVal(eqFin);
    }
    
    private String extVal(String termino) {
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher matcher = pattern.matcher(termino);
        return matcher.find() ? matcher.group() : "";
    }
    
    private void desVal(List<String> eqFin) {
        System.out.println("Despejando variable");
        
        if (eqFin.size() >= 2) {    
            String primero = eqFin.get(0);
            String segundo = eqFin.get(1);
            
            String variable = extVal(primero);
            int co1 = Integer.parseInt(primero.replace(variable, ""));
            int co2 = Integer.parseInt(segundo);
            
            double operacion = (double) co2 / co1;
            System.out.println("El resultado es: " + variable + " = " + operacion);
        }
    }
    
    public static void main(String[] args) {
        MN_Ejemplo8 a = new MN_Ejemplo8();
        a.ecuacion2x2();
    }
}

