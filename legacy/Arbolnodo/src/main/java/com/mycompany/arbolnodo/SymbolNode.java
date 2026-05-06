/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.arbolnodo;

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