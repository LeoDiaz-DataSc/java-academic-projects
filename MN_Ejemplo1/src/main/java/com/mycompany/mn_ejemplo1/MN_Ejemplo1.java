/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mn_ejemplo1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class MN_Ejemplo1 extends JFrame implements ActionListener {

    private JTextField velocidadField;
    private JTextField masaField;
    private JLabel resultadoLabel;
    private final float conBase = 3.6F;

    public MN_Ejemplo1() {
        super("Calculadora de Momento");

        // Configuración de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setLocationRelativeTo(null); // Centrar la ventana

        // Crear componentes
        JLabel velocidadLabel = new JLabel("Ingresa velocidad:");
        velocidadField = new JTextField(10);

        JLabel masaLabel = new JLabel("Ingresa la masa:");
        masaField = new JTextField(10);

        JButton calcularButton = new JButton("Calcular Momento");
        calcularButton.addActionListener(this);

        resultadoLabel = new JLabel("P = ");

        // Añadir componentes al panel
        add(velocidadLabel);
        add(velocidadField);
        add(Box.createRigidArea(new Dimension(0, 10))); // Espacio vertical
        add(masaLabel);
        add(masaField);
        add(Box.createRigidArea(new Dimension(0, 10))); // Espacio vertical
        add(calcularButton);
        add(Box.createRigidArea(new Dimension(0, 10))); // Espacio vertical
        add(resultadoLabel);

        // Hacer visible la ventana
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Obtener valores de los campos de texto
            float intVelocidad = Float.parseFloat(velocidadField.getText());
            float intMasa = Float.parseFloat(masaField.getText());

            // Calcular velocidad con dos decimales (truncando)
            float velocidad = (float) Math.floor((intVelocidad / conBase) * 100) / 100;

            // Calcular momento
            float momentum = intMasa * velocidad;

            // Formatear el resultado a dos decimales
            DecimalFormat df = new DecimalFormat("#.##");
            String momentumFormatted = df.format(momentum);

            // Mostrar resultado
            resultadoLabel.setText("P = " + momentumFormatted + " kgm/s");

        } catch (NumberFormatException ex) {
            resultadoLabel.setText("Error: Ingresa números válidos.");
        }
    }

    public static void main(String[] args) {
        // Ejecutar la GUI en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MN_Ejemplo1();
            }
        });
    }
}
