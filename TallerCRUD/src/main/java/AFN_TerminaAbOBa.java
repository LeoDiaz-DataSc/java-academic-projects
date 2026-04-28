
import java.util.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kotli
 */
public class AFN_TerminaAbOBa {
    private static final int Q0 = 0;
    private static final int Q1 = 1;
    private static final int Q2 = 2;
    private static final int Q3 = 3;
    private static final int Q4 = 4;
    
    private static final Set<Integer> ESTADOS_FINALES = new HashSet<>(Arrays.asList(Q2, Q4));
    private static Set<Integer> transicion(int estado, char simbolo){
        Set<Integer> siguientes = new HashSet<>();
        switch (estado){
            case Q0:
                if (simbolo == 'a'){
                    siguientes.add(Q0);
                    siguientes.add(Q1);
                } else if (simbolo == 'b'){
                    siguientes.add(Q0);
                    siguientes.add(Q3);
                }
                break;
            case Q1:
                if (simbolo == 'b'){
                    siguientes.add(Q2);
                }
                break;
            case Q2:
                break;
            case Q3:
                if (simbolo == 'a'){
                    siguientes.add(Q4);
                }
                break;
            case Q4:
                break;
        }
        return siguientes;
    }
   public static boolean procesar(String cadena) {
        Set<Integer> estadosActuales = new HashSet<>();
        estadosActuales.add(Q0);
        
        System.out.println("\n=== Procesando cadena: \"" + cadena + "\" ===");
        System.out.println("Estados iniciales: " + estadosActuales);
        
        for (int i = 0; i < cadena.length(); i++) {
            char simbolo = cadena.charAt(i);
            Set<Integer> nuevosEstados = new HashSet<>();
            
            for (int estado : estadosActuales) {
                Set<Integer> transiciones = transicion(estado, simbolo);
                nuevosEstados.addAll(transiciones);
            }
            
            estadosActuales = nuevosEstados;
            System.out.println("Símbolo '" + simbolo + "' -> Estados: " + estadosActuales);
            
            if (estadosActuales.isEmpty()) {
                System.out.println("No hay estados alcanzables. RECHAZADA");
                return false;
            }
        }
        for (int estado : estadosActuales) {
            if (ESTADOS_FINALES.contains(estado)) {
                System.out.println("Estado final alcanzado: Q" + estado + ". ACEPTADA ✓");
                return true;
            }
        }
        
        System.out.println("Ningun estado final alcanzado. RECHAZADA ✗");
        return false;
    }
    public static void main(String[] args) {
       
        System.out.println("  AFN: Cadenas que terminan con 'ab' O terminan con 'ba'");
       
        
        String[] cadenasAceptadas = {"ab", "ba", "aab", "aba", "bab", "bba", "abab", "baba"};
        
        System.out.println("\n--- CADENAS QUE DEBEN SER ACEPTADAS ---");
        for (String cadena : cadenasAceptadas) {
            boolean resultado = procesar(cadena);
            assert resultado : "Error: '" + cadena + "' deberia ser aceptada";
        }
        
        String[] cadenasRechazadas = {"a", "b", "aa", "bb", "abb", "baa", ""};
        
        System.out.println("\n\n--- CADENAS QUE DEBEN SER RECHAZADAS ---");
        for (String cadena : cadenasRechazadas) {
            boolean resultado = procesar(cadena);
            assert !resultado : "Error: '" + cadena + "' debería ser rechazada";
        }
        
        // Prueba interactiva
        System.out.println("\n\n=== PRUEBA PERSONALIZADA ===");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingresa una cadena para probar (solo 'a' y 'b'): ");
        String cadenaUsuario = scanner.nextLine();
        
        // Validar entrada
        if (cadenaUsuario.matches("[ab]*")) {
            procesar(cadenaUsuario);
        } else {
            System.out.println("Error: La cadena debe contener solo 'a' y 'b'");
        }
        
        scanner.close();
        System.out.println("\n¡Programa finalizado!");
    }
}