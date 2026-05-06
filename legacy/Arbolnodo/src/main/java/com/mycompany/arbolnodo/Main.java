package com.mycompany.arbolnodo;

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
