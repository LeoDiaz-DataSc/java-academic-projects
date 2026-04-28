package com.taller.view.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JButton btnClientes;
    private JButton btnVehiculos;
    private JButton btnReparaciones;

    public MainFrame() {
        setTitle("Sistema de Gestión de Taller");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        btnClientes = new JButton("Gestión de Clientes");
        btnVehiculos = new JButton("Gestión de Vehículos");
        btnReparaciones = new JButton("Gestión de Reparaciones");

        btnClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ClienteFrame().setVisible(true);
            }
        });

        btnVehiculos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VehiculoFrame().setVisible(true);
            }
        });

        btnReparaciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ReparacionFrame().setVisible(true);
            }
        });

        mainPanel.add(btnClientes);
        mainPanel.add(btnVehiculos);
        mainPanel.add(btnReparaciones);

        add(mainPanel);
    }
} 