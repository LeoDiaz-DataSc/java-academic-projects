/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemagym;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * Billie4b
 * @author ellio
 */
public class conexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/goldsgym";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Panzer8.1";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos.\nError: " + e.getMessage());
            return null;
        }
    }
}

