/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kotli
 */
class Nodo {
    String valor;
    Nodo izquierda, derecha;

    public Nodo(String valor) {
        this.valor = valor;
        izquierda = derecha = null;
    }
}

public class ArbolIfExpresion {

    // Evalúa el árbol
    public static int evaluar(Nodo nodo, int a, int b) {
        switch (nodo.valor) {

            // Operadores lógicos y relacionales
            case "<":
                return (evaluar(nodo.izquierda, a, b) < evaluar(nodo.derecha, a, b)) ? 1 : 0;

            // Operadores aritméticos
            case "+":
                return evaluar(nodo.izquierda, a, b) + evaluar(nodo.derecha, a, b);

            // Palabra reservada IF
            case "IF":
                int condicion = evaluar(nodo.izquierda, a, b);
                if (condicion == 1) {
                    a = evaluar(nodo.derecha, a, b);
                }
                return a;

            // Asignación: a = a + 1
            case "=":
                return evaluar(nodo.derecha, a, b);

            // Operandos (variables y números)
            case "a":
                return a;
            case "b":
                return b;
            case "1":
                return 1;
        }
        return 0;
    }

    public static void main(String[] args) {
        // Construcción del árbol IF (a<b){a=a+1;}

        Nodo raiz = new Nodo("IF");

        // Nodo condición (a < b)
        raiz.izquierda = new Nodo("<");
        raiz.izquierda.izquierda = new Nodo("a");
        raiz.izquierda.derecha = new Nodo("b");

        // Nodo asignación (a = a + 1)
        raiz.derecha = new Nodo("=");
        raiz.derecha.izquierda = new Nodo("a");
        raiz.derecha.derecha = new Nodo("+");
        raiz.derecha.derecha.izquierda = new Nodo("a");
        raiz.derecha.derecha.derecha = new Nodo("1");

        int a = 3;
        int b = 5;

        int resultado = evaluar(raiz, a, b);

        System.out.println("Resultado final de a = " + resultado);
    }
}