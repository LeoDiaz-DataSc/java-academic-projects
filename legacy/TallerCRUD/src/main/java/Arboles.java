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

public class Arboles {
    // Evalúa el árbol de forma recursiva
    public static int evaluar(Nodo nodo, int a, int b, int c) {
        // Si el nodo es hoja, retorna el valor de la variable
        switch (nodo.valor) {
            case "a": return a;
            case "b": return b;
            case "c": return c;
        }

        // Evaluamos subárbol izquierdo y derecho
        int izq = evaluar(nodo.izquierda, a, b, c);
        int der = evaluar(nodo.derecha, a, b, c);

        // Ejecutamos el operador del nodo actual
        switch (nodo.valor) {
            case "+": return izq + der;
            case "*": return izq * der;
        }

        return 0;
    }

    public static void main(String[] args) {
        // Construcción manual del árbol para a + b * c
        Nodo raiz = new Nodo("+");
        raiz.izquierda = new Nodo("a");

        raiz.derecha = new Nodo("*");
        raiz.derecha.izquierda = new Nodo("b");
        raiz.derecha.derecha = new Nodo("c");

        // Valores para a, b, c
        int a = 2;
        int b = 3;
        int c = 4;

        // Evalúa la expresión
        int resultado = evaluar(raiz, a, b, c);

        System.out.println("Resultado de a + b * c = " + resultado);
    }
}