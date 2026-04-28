class SymbolNode {
    String name;
    String type;
    String scope;
    String category; // función, parámetro, variable
    int address;
    SymbolNode left, right;

    public SymbolNode(String name, String type, String scope, String category, int address) {
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.category = category;
        this.address = address;
        this.left = null;
        this.right = null;
    }

    public void displaySymbol() {
        System.out.printf("%-15s %-10s %-15s %-12s %d%n", 
            name, type, scope, category, address);
    }
}

// Clase que implementa el árbol binario de búsqueda para la tabla de símbolos
class SymbolTree {
    private SymbolNode root;
    private int nextAddress;

    public SymbolTree() {
        root = null;
        nextAddress = 1000; // Dirección inicial
    }

    // Método mejorado para agregar símbolos
    public boolean addSymbol(String name, String type, String scope, String category) {
        int currentAddress = nextAddress;
        SymbolNode newRoot = insertNode(root, name, type, scope, category, currentAddress);
        
        if (newRoot != null && (root == null || !root.equals(newRoot) || root != newRoot)) {
            root = newRoot;
            nextAddress += 4; // Incrementar en 4 bytes (típico para int)
            return true;
        }
        return false;
    }

    private SymbolNode insertNode(SymbolNode node, String name, String type, 
                                   String scope, String category, int address) {
        if (node == null) {
            return new SymbolNode(name, type, scope, category, address);
        }

        int comparison = name.compareTo(node.name);
        
        if (comparison < 0) {
            node.left = insertNode(node.left, name, type, scope, category, address);
        } else if (comparison > 0) {
            node.right = insertNode(node.right, name, type, scope, category, address);
        } else {
            // Símbolo duplicado
            System.out.println("⚠️  Error: El símbolo '" + name + "' ya existe en la tabla.");
            return node; // No modificar el árbol
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
        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              TABLA DE SÍMBOLOS - Función: sumar                    ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-15s %-10s %-15s %-12s %-8s ║%n", 
            "Nombre", "Tipo", "Ámbito", "Categoría", "Dirección");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        inorderTraversal(root);
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
    }

    private void inorderTraversal(SymbolNode node) {
        if (node != null) {
            inorderTraversal(node.left);
            System.out.print("║ ");
            node.displaySymbol();
            System.out.print(" ║\n");
            inorderTraversal(node.right);
        }
    }

    public int getSymbolCount() {
        return countNodes(root);
    }

    private int countNodes(SymbolNode node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }
}

// Clase principal
public class Main {
    public static void main(String[] args) {
        // Crear la tabla de símbolos
        SymbolTree symbolTree = new SymbolTree();

        System.out.println("\n🔍 Analizando función: int sumar(int a, int b) { return a + b; }\n");
        
        // Agregar la función sumar
        System.out.println("➤ Registrando función 'sumar'...");
        symbolTree.addSymbol("sumar", "int", "global", "función");

        // Agregar los parámetros de la función
        System.out.println("➤ Registrando parámetro 'a'...");
        symbolTree.addSymbol("a", "int", "sumar", "parámetro");

        System.out.println("➤ Registrando parámetro 'b'...");
        symbolTree.addSymbol("b", "int", "sumar", "parámetro");

        // Mostrar la tabla completa
        symbolTree.displayTable();

        // Estadísticas
        System.out.println("\n📊 Estadísticas:");
        System.out.println("   Total de símbolos: " + symbolTree.getSymbolCount());

        // Búsqueda de símbolos específicos
        System.out.println("\n🔎 Búsqueda de símbolos:");
        
        String[] searchSymbols = {"sumar", "a", "b", "c"};
        for (String symbol : searchSymbols) {
            SymbolNode found = symbolTree.findSymbol(symbol);
            if (found != null) {
                System.out.println("✓ Símbolo '" + symbol + "' encontrado:");
                System.out.print("  ");
                found.displaySymbol();
            } else {
                System.out.println("✗ Símbolo '" + symbol + "' NO encontrado");
            }
        }

        // Intentar agregar un duplicado
        System.out.println("\n🧪 Prueba de duplicado:");
        symbolTree.addSymbol("a", "float", "sumar", "parámetro");
    }
}
