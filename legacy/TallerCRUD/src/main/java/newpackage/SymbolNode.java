package newpackage;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kotli
 */
public class SymbolNode {
    String name;
    String type;
    String scope;
    int address;
    SymbolNode left, right;
    public SymbolNode(String name, String type, String scope, int address){
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.address = address;
        this.left = null;
        this.right = null;
    }
    public void displaySymbol(){
        System.out.println("Name: " + name + ", Type: " + type +
                            ", Scope: " + scope + ", Address: "+ address);
    }
}
class SymbolTree {
    private SymbolNode root;  
    private int nextAddress;  
    public SymbolTree() {
        root = null;
        nextAddress = 0;
    }
    public void addSymbol(String name, String type, String scope) {
        root = insertNode(root, name, type, scope, nextAddress++);
    }
    private SymbolNode insertNode(SymbolNode node, String name, String type, String scope, int address) {
        if (node == null) {
            return new SymbolNode(name, type, scope, address); // Crear nuevo nodo si está vacío
        }
        if (name.compareTo(node.name) < 0) {
            node.left = insertNode(node.left, name, type, scope, address); // Insertar en el subárbol izquierdo
        } else if (name.compareTo(node.name) > 0) {
            node.right = insertNode(node.right, name, type, scope, address); // Insertar en el subárbol derecho
        } else {
            System.out.println("Error: El símbolo '" + name + "' ya existe en la tabla.");
        }
        return node;
    }
    public SymbolNode findSymbol(String name) {
        return searchNode(root, name);
    }
    private SymbolNode searchNode(SymbolNode node, String name) {
        if (node == null || name.equals(node.name)) {
            return node;
        }
        if (name.compareTo(node.name) < 0) {
            return searchNode(node.left, name);
        } else {
            return searchNode(node.right, name);
        }
    }
    public void displayTable() {
        System.out.println("Tabla de Símbolos:");
        inorderTraversal(root); 
    }
    private void inorderTraversal(SymbolNode node) {
        if (node != null) {
            inorderTraversal(node.left);   
            node.displaySymbol();         
            inorderTraversal(node.right);  
        }
    }
}
public class Main {
    public static void main(String[] args) {
        SymbolTree symbolTree = new SymbolTree();
        symbolTree.addSymbol("x", "int", "global");
        symbolTree.addSymbol("y", "float", "global");
        symbolTree.addSymbol("z", "int", "local");
        symbolTree.displayTable();
        SymbolNode foundSymbol = symbolTree.findSymbol("y");
        if (foundSymbol != null) {
            System.out.println("\nSímbolo encontrado:");
            foundSymbol.displaySymbol();
        } else {
            System.out.println("\nSímbolo no encontrado.");
        }
    }
}