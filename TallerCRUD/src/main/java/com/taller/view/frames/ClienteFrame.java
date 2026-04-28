package com.taller.view.frames;

import com.taller.dao.ClienteDAO;
import com.taller.model.Cliente;
import com.taller.config.DatabaseConfig;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ClienteFrame extends JFrame {
    private ClienteDAO clienteDAO;
    private JTable tablaClientes;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;

    public ClienteFrame() {
        try {
            Connection conexion = DatabaseConfig.getConnection();
            clienteDAO = new ClienteDAO(conexion);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos: " + e.getMessage());
            System.exit(1);
        }

        setTitle("Gestión de Clientes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");

        btnAgregar.addActionListener(e -> agregarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());

        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);

        // Tabla de clientes
        String[] columnNames = {"ID", "Nombre", "Apellido", "Fecha Ingreso", "Fecha Egreso"};
        Object[][] data = obtenerDatosClientes();
        tablaClientes = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(tablaClientes);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private Object[][] obtenerDatosClientes() {
        try {
            List<Cliente> clientes = clienteDAO.listarTodos();
            Object[][] data = new Object[clientes.size()][5];
            for (int i = 0; i < clientes.size(); i++) {
                Cliente cliente = clientes.get(i);
                data[i][0] = cliente.getIdCliente();
                data[i][1] = cliente.getNombre();
                data[i][2] = cliente.getApellido();
                data[i][3] = cliente.getFechaIngreso();
                data[i][4] = cliente.getFechaEgreso();
            }
            return data;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los clientes: " + e.getMessage());
            return new Object[0][0];
        }
    }

    private void agregarCliente() {
        JTextField nombreField = new JTextField();
        JTextField apellidoField = new JTextField();

        Object[] message = {
            "Nombre:", nombreField,
            "Apellido:", apellidoField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Agregar Cliente",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                Cliente cliente = new Cliente();
                cliente.setNombre(nombreField.getText());
                cliente.setApellido(apellidoField.getText());
                clienteDAO.insertar(cliente);
                actualizarTabla();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al agregar el cliente: " + e.getMessage());
            }
        }
    }

    private void editarCliente() {
        int selectedRow = tablaClientes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente para editar");
            return;
        }

        int idCliente = (int) tablaClientes.getValueAt(selectedRow, 0);
        try {
            Cliente cliente = clienteDAO.obtenerPorId(idCliente);
            if (cliente != null) {
                JTextField nombreField = new JTextField(cliente.getNombre());
                JTextField apellidoField = new JTextField(cliente.getApellido());

                Object[] message = {
                    "Nombre:", nombreField,
                    "Apellido:", apellidoField
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Editar Cliente",
                        JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    cliente.setNombre(nombreField.getText());
                    cliente.setApellido(apellidoField.getText());
                    clienteDAO.actualizar(cliente);
                    actualizarTabla();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al editar el cliente: " + e.getMessage());
        }
    }

    private void eliminarCliente() {
        int selectedRow = tablaClientes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente para eliminar");
            return;
        }

        int idCliente = (int) tablaClientes.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar este cliente?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                clienteDAO.eliminar(idCliente);
                actualizarTabla();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el cliente: " + e.getMessage());
            }
        }
    }

    private void actualizarTabla() {
        Object[][] data = obtenerDatosClientes();
        String[] columnNames = {"ID", "Nombre", "Apellido", "Fecha Ingreso", "Fecha Egreso"};
        tablaClientes.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
} 