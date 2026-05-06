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

public class ArbolExpresionCompleja {

    public static double evaluar(Nodo nodo, double a, double b, double c, double d, double e) {

        switch (nodo.valor) {
            case "+": return evaluar(nodo.izquierda, a, b, c, d, e) + evaluar(nodo.derecha, a, b, c, d, e);
            case "-": return evaluar(nodo.izquierda, a, b, c, d, e) - evaluar(nodo.derecha, a, b, c, d, e);
            case "*": return evaluar(nodo.izquierda, a, b, c, d, e) * evaluar(nodo.derecha, a, b, c, d, e);
            case "/": return evaluar(nodo.izquierda, a, b, c, d, e) / evaluar(nodo.derecha, a, b, c, d, e);
            case "^": return Math.pow(evaluar(nodo.izquierda, a, b, c, d, e), evaluar(nodo.derecha, a, b, c, d, e));
            case "a": return a;
            case "b": return b;
            case "c": return c;
            case "d": return d;
            case "e": return e;
            case "2": return 2;
        }
        return 0;
    }

    public static void main(String[] args) {

        // Construcción del árbol para [(a+b)*(c/d)]-(e-a)^2
        Nodo raiz = new Nodo("-");

        raiz.izquierda = new Nodo("*");
        raiz.izquierda.izquierda = new Nodo("+");
        raiz.izquierda.izquierda.izquierda = new Nodo("a");
        raiz.izquierda.izquierda.derecha = new Nodo("b");

        raiz.izquierda.derecha = new Nodo("/");
        raiz.izquierda.derecha.izquierda = new Nodo("c");
        raiz.izquierda.derecha.derecha = new Nodo("d");

        raiz.derecha = new Nodo("^");
        raiz.derecha.izquierda = new Nodo("-");
        raiz.derecha.izquierda.izquierda = new Nodo("e");
        raiz.derecha.izquierda.derecha = new Nodo("a");
        raiz.derecha.derecha = new Nodo("2");

        // Valores de prueba
        double a = 2, b = 3, c = 10, d = 2, e = 7;

        double resultado = evaluar(raiz, a, b, c, d, e);

        System.out.println("Resultado de la expresión es: " + resultado);
    }
}