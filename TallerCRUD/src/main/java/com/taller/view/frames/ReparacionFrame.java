package com.taller.view.frames;

import com.taller.dao.ReparacionDAO;
import com.taller.dao.VehiculoDAO;
import com.taller.model.Reparacion;
import com.taller.model.Vehiculo;
import com.taller.config.DatabaseConfig;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ReparacionFrame extends JFrame {
    private ReparacionDAO reparacionDAO;
    private VehiculoDAO vehiculoDAO;
    private JTable tablaReparaciones;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;

    public ReparacionFrame() {
        try {
            Connection conexion = DatabaseConfig.getConnection();
            reparacionDAO = new ReparacionDAO(conexion);
            vehiculoDAO = new VehiculoDAO(conexion);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos: " + e.getMessage());
            System.exit(1);
        }

        setTitle("Gestión de Reparaciones");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");

        btnAgregar.addActionListener(e -> agregarReparacion());
        btnEditar.addActionListener(e -> editarReparacion());
        btnEliminar.addActionListener(e -> eliminarReparacion());

        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);

        // Tabla de reparaciones
        String[] columnNames = {"ID", "ID Vehículo", "Detalle", "Costo", "Método de Pago", "Fecha"};
        Object[][] data = obtenerDatosReparaciones();
        tablaReparaciones = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(tablaReparaciones);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private Object[][] obtenerDatosReparaciones() {
        try {
            List<Reparacion> reparaciones = reparacionDAO.listarTodas();
            Object[][] data = new Object[reparaciones.size()][6];
            for (int i = 0; i < reparaciones.size(); i++) {
                Reparacion reparacion = reparaciones.get(i);
                data[i][0] = reparacion.getIdReparacion();
                data[i][1] = reparacion.getIdVehiculo();
                data[i][2] = reparacion.getDetalle();
                data[i][3] = reparacion.getCosto();
                data[i][4] = reparacion.getMetodoPago();
                data[i][5] = reparacion.getFechaReparacion();
            }
            return data;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las reparaciones: " + e.getMessage());
            return new Object[0][0];
        }
    }

    private void agregarReparacion() {
        try {
            List<Vehiculo> vehiculos = vehiculoDAO.listarTodos();
            String[] vehiculosArray = new String[vehiculos.size()];
            for (int i = 0; i < vehiculos.size(); i++) {
                vehiculosArray[i] = vehiculos.get(i).getIdVehiculo() + " - " + 
                                  vehiculos.get(i).getMarca() + " " + 
                                  vehiculos.get(i).getModelo();
            }

            JComboBox<String> vehiculoCombo = new JComboBox<>(vehiculosArray);
            JTextArea detalleArea = new JTextArea(3, 20);
            JTextField costoField = new JTextField();
            JComboBox<Reparacion.MetodoPago> metodoCombo = new JComboBox<>(Reparacion.MetodoPago.values());

            Object[] message = {
                "Vehículo:", vehiculoCombo,
                "Detalle:", new JScrollPane(detalleArea),
                "Costo:", costoField,
                "Método de Pago:", metodoCombo
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Agregar Reparación",
                    JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String selectedVehiculo = (String) vehiculoCombo.getSelectedItem();
                int idVehiculo = Integer.parseInt(selectedVehiculo.split(" - ")[0]);

                Reparacion reparacion = new Reparacion();
                reparacion.setIdVehiculo(idVehiculo);
                reparacion.setDetalle(detalleArea.getText());
                reparacion.setCosto(Double.parseDouble(costoField.getText()));
                reparacion.setMetodoPago((Reparacion.MetodoPago) metodoCombo.getSelectedItem());

                reparacionDAO.insertar(reparacion);
                actualizarTabla();
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar la reparación: " + e.getMessage());
        }
    }

    private void editarReparacion() {
        int selectedRow = tablaReparaciones.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una reparación para editar");
            return;
        }

        int idReparacion = (int) tablaReparaciones.getValueAt(selectedRow, 0);
        try {
            Reparacion reparacion = reparacionDAO.obtenerPorId(idReparacion);
            if (reparacion != null) {
                List<Vehiculo> vehiculos = vehiculoDAO.listarTodos();
                String[] vehiculosArray = new String[vehiculos.size()];
                for (int i = 0; i < vehiculos.size(); i++) {
                    vehiculosArray[i] = vehiculos.get(i).getIdVehiculo() + " - " + 
                                      vehiculos.get(i).getMarca() + " " + 
                                      vehiculos.get(i).getModelo();
                }

                JComboBox<String> vehiculoCombo = new JComboBox<>(vehiculosArray);
                JTextArea detalleArea = new JTextArea(reparacion.getDetalle(), 3, 20);
                JTextField costoField = new JTextField(String.valueOf(reparacion.getCosto()));
                JComboBox<Reparacion.MetodoPago> metodoCombo = new JComboBox<>(Reparacion.MetodoPago.values());
                metodoCombo.setSelectedItem(reparacion.getMetodoPago());

                Object[] message = {
                    "Vehículo:", vehiculoCombo,
                    "Detalle:", new JScrollPane(detalleArea),
                    "Costo:", costoField,
                    "Método de Pago:", metodoCombo
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Editar Reparación",
                        JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    String selectedVehiculo = (String) vehiculoCombo.getSelectedItem();
                    int idVehiculo = Integer.parseInt(selectedVehiculo.split(" - ")[0]);

                    reparacion.setIdVehiculo(idVehiculo);
                    reparacion.setDetalle(detalleArea.getText());
                    reparacion.setCosto(Double.parseDouble(costoField.getText()));
                    reparacion.setMetodoPago((Reparacion.MetodoPago) metodoCombo.getSelectedItem());

                    reparacionDAO.actualizar(reparacion);
                    actualizarTabla();
                }
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al editar la reparación: " + e.getMessage());
        }
    }

    private void eliminarReparacion() {
        int selectedRow = tablaReparaciones.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una reparación para eliminar");
            return;
        }

        int idReparacion = (int) tablaReparaciones.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar esta reparación?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                reparacionDAO.eliminar(idReparacion);
                actualizarTabla();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar la reparación: " + e.getMessage());
            }
        }
    }

    private void actualizarTabla() {
        Object[][] data = obtenerDatosReparaciones();
        String[] columnNames = {"ID", "ID Vehículo", "Detalle", "Costo", "Método de Pago", "Fecha"};
        tablaReparaciones.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
} 