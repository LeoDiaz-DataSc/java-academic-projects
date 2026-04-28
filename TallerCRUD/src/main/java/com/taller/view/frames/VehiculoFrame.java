package com.taller.view.frames;

import com.taller.dao.VehiculoDAO;
import com.taller.dao.ClienteDAO;
import com.taller.model.Vehiculo;
import com.taller.model.Cliente;
import com.taller.config.DatabaseConfig;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class VehiculoFrame extends JFrame {
    private VehiculoDAO vehiculoDAO;
    private ClienteDAO clienteDAO;
    private Connection conexion;
    private JTable tablaVehiculos;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;

    public VehiculoFrame() {
        try {
            conexion = DatabaseConfig.getConnection();
            vehiculoDAO = new VehiculoDAO(conexion);
            clienteDAO = new ClienteDAO(conexion);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos: " + e.getMessage());
            System.exit(1);
        }

        setTitle("Gestión de Vehículos");
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

        btnAgregar.addActionListener(e -> agregarVehiculo());
        btnEditar.addActionListener(e -> editarVehiculo());
        btnEliminar.addActionListener(e -> eliminarVehiculo());

        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);

        // Tabla de vehículos
        String[] columnNames = {"ID", "ID Cliente", "Marca", "Modelo", "Tipo"};
        Object[][] data = obtenerDatosVehiculos();
        tablaVehiculos = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(tablaVehiculos);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                DatabaseConfig.closeConnection(conexion);
            }
        });
    }

    private Object[][] obtenerDatosVehiculos() {
        try {
            List<Vehiculo> vehiculos = vehiculoDAO.listarTodos();
            Object[][] data = new Object[vehiculos.size()][5];
            for (int i = 0; i < vehiculos.size(); i++) {
                Vehiculo vehiculo = vehiculos.get(i);
                data[i][0] = vehiculo.getIdVehiculo();
                data[i][1] = vehiculo.getIdCliente();
                data[i][2] = vehiculo.getMarca();
                data[i][3] = vehiculo.getModelo();
                data[i][4] = vehiculo.getTipoVehiculo().toString();
            }
            return data;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar los vehículos: " + e.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE);
            return new Object[0][0];
        }
    }

    private void agregarVehiculo() {
        try {
            List<Cliente> clientes = clienteDAO.listarTodos();
            if (clientes.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No hay clientes registrados. Por favor, registre un cliente primero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] clientesArray = new String[clientes.size()];
            for (int i = 0; i < clientes.size(); i++) {
                clientesArray[i] = clientes.get(i).getIdCliente() + " - " + 
                                 clientes.get(i).getNombre() + " " + 
                                 clientes.get(i).getApellido();
            }

            JComboBox<String> clienteCombo = new JComboBox<>(clientesArray);
            JTextField marcaField = new JTextField();
            JTextField modeloField = new JTextField();
            JComboBox<Vehiculo.TipoVehiculo> tipoCombo = new JComboBox<>(Vehiculo.TipoVehiculo.values());
            tipoCombo.setSelectedItem(Vehiculo.TipoVehiculo.Sedan); // Valor por defecto

            Object[] message = {
                "Cliente:", clienteCombo,
                "Marca:", marcaField,
                "Modelo:", modeloField,
                "Tipo:", tipoCombo
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Agregar Vehículo",
                    JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                // Validar campos
                if (marcaField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "La marca no puede estar vacía", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (modeloField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El modelo no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String selectedCliente = (String) clienteCombo.getSelectedItem();
                int idCliente = Integer.parseInt(selectedCliente.split(" - ")[0]);

                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setIdCliente(idCliente);
                vehiculo.setMarca(marcaField.getText().trim());
                vehiculo.setModelo(modeloField.getText().trim());
                vehiculo.setTipoVehiculo((Vehiculo.TipoVehiculo) tipoCombo.getSelectedItem());

                try {
                    vehiculoDAO.insertar(vehiculo);
                    actualizarTabla();
                    JOptionPane.showMessageDialog(this, "Vehículo agregado exitosamente");
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al agregar el vehículo: " + e.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarVehiculo() {
        int selectedRow = tablaVehiculos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un vehículo para editar");
            return;
        }

        int idVehiculo = (int) tablaVehiculos.getValueAt(selectedRow, 0);
        try {
            Vehiculo vehiculo = vehiculoDAO.obtenerPorId(idVehiculo);
            if (vehiculo != null) {
                List<Cliente> clientes = clienteDAO.listarTodos();
                String[] clientesArray = new String[clientes.size()];
                for (int i = 0; i < clientes.size(); i++) {
                    clientesArray[i] = clientes.get(i).getIdCliente() + " - " + 
                                     clientes.get(i).getNombre() + " " + 
                                     clientes.get(i).getApellido();
                }

                JComboBox<String> clienteCombo = new JComboBox<>(clientesArray);
                JTextField marcaField = new JTextField(vehiculo.getMarca());
                JTextField modeloField = new JTextField(vehiculo.getModelo());
                JComboBox<Vehiculo.TipoVehiculo> tipoCombo = new JComboBox<>(Vehiculo.TipoVehiculo.values());
                tipoCombo.setSelectedItem(vehiculo.getTipoVehiculo());

                Object[] message = {
                    "Cliente:", clienteCombo,
                    "Marca:", marcaField,
                    "Modelo:", modeloField,
                    "Tipo:", tipoCombo
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Editar Vehículo",
                        JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    String selectedCliente = (String) clienteCombo.getSelectedItem();
                    int idCliente = Integer.parseInt(selectedCliente.split(" - ")[0]);

                    vehiculo.setIdCliente(idCliente);
                    vehiculo.setMarca(marcaField.getText());
                    vehiculo.setModelo(modeloField.getText());
                    vehiculo.setTipoVehiculo((Vehiculo.TipoVehiculo) tipoCombo.getSelectedItem());

                    vehiculoDAO.actualizar(vehiculo);
                    actualizarTabla();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al editar el vehículo: " + e.getMessage());
        }
    }

    private void eliminarVehiculo() {
        int selectedRow = tablaVehiculos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un vehículo para eliminar");
            return;
        }

        int idVehiculo = (int) tablaVehiculos.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar este vehículo?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                vehiculoDAO.eliminar(idVehiculo);
                actualizarTabla();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el vehículo: " + e.getMessage());
            }
        }
    }

    private void actualizarTabla() {
        Object[][] data = obtenerDatosVehiculos();
        String[] columnNames = {"ID", "ID Cliente", "Marca", "Modelo", "Tipo"};
        tablaVehiculos.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
} 