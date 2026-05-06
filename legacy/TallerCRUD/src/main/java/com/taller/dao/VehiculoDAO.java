package com.taller.dao;

import com.taller.model.Vehiculo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {
    private final Connection conexion;

    public VehiculoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public void insertar(Vehiculo vehiculo) throws SQLException {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no puede ser nulo");
        }
        if (vehiculo.getIdCliente() <= 0) {
            throw new IllegalArgumentException("El ID del cliente debe ser válido");
        }
        if (vehiculo.getMarca() == null || vehiculo.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("La marca no puede estar vacía");
        }
        if (vehiculo.getModelo() == null || vehiculo.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo no puede estar vacío");
        }
        if (vehiculo.getTipoVehiculo() == null) {
            throw new IllegalArgumentException("El tipo de vehículo no puede ser nulo");
        }

        String sql = "INSERT INTO vehiculo (id_cliente, marca, modelo, tipo_vehiculo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, vehiculo.getIdCliente());
            stmt.setString(2, vehiculo.getMarca().trim());
            stmt.setString(3, vehiculo.getModelo().trim());
            stmt.setString(4, vehiculo.getTipoVehiculo().name());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    vehiculo.setIdVehiculo(rs.getInt(1));
                }
            }
        }
    }

    private Vehiculo.TipoVehiculo convertirTipoVehiculo(String tipoVehiculoStr) {
        if (tipoVehiculoStr == null) {
            return Vehiculo.TipoVehiculo.Otro;
        }
        
        // Normalizar el string eliminando caracteres especiales y espacios
        String tipoNormalizado = tipoVehiculoStr.trim()
            .replaceAll("[^a-zA-Z0-9]", "")
            .toLowerCase();
            
        // Mapeo de valores posibles a valores del enum
        switch (tipoNormalizado) {
            case "sedan":
            case "sedn":
                return Vehiculo.TipoVehiculo.Sedan;
            case "suv":
                return Vehiculo.TipoVehiculo.SUV;
            case "pickup":
                return Vehiculo.TipoVehiculo.Pickup;
            case "moto":
                return Vehiculo.TipoVehiculo.Moto;
            case "camion":
                return Vehiculo.TipoVehiculo.Camion;
            default:
                return Vehiculo.TipoVehiculo.Otro;
        }
    }

    public List<Vehiculo> listarTodos() throws SQLException {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT * FROM vehiculo";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setIdVehiculo(rs.getInt("id_vehiculo"));
                vehiculo.setIdCliente(rs.getInt("id_cliente"));
                vehiculo.setMarca(rs.getString("marca"));
                vehiculo.setModelo(rs.getString("modelo"));
                String tipoVehiculoStr = rs.getString("tipo_vehiculo");
                vehiculo.setTipoVehiculo(convertirTipoVehiculo(tipoVehiculoStr));
                vehiculos.add(vehiculo);
            }
        }
        return vehiculos;
    }

    public List<Vehiculo> listarPorCliente(int idCliente) throws SQLException {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT * FROM vehiculo WHERE id_cliente = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vehiculo vehiculo = new Vehiculo();
                    vehiculo.setIdVehiculo(rs.getInt("id_vehiculo"));
                    vehiculo.setIdCliente(rs.getInt("id_cliente"));
                    vehiculo.setMarca(rs.getString("marca"));
                    vehiculo.setModelo(rs.getString("modelo"));
                    String tipoVehiculoStr = rs.getString("tipo_vehiculo");
                    vehiculo.setTipoVehiculo(convertirTipoVehiculo(tipoVehiculoStr));
                    vehiculos.add(vehiculo);
                }
            }
        }
        return vehiculos;
    }

    public Vehiculo obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM vehiculo WHERE id_vehiculo = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Vehiculo vehiculo = new Vehiculo();
                    vehiculo.setIdVehiculo(rs.getInt("id_vehiculo"));
                    vehiculo.setIdCliente(rs.getInt("id_cliente"));
                    vehiculo.setMarca(rs.getString("marca"));
                    vehiculo.setModelo(rs.getString("modelo"));
                    String tipoVehiculoStr = rs.getString("tipo_vehiculo");
                    vehiculo.setTipoVehiculo(convertirTipoVehiculo(tipoVehiculoStr));
                    return vehiculo;
                }
            }
        }
        return null;
    }

    public void actualizar(Vehiculo vehiculo) throws SQLException {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no puede ser nulo");
        }
        if (vehiculo.getIdVehiculo() <= 0) {
            throw new IllegalArgumentException("El ID del vehículo debe ser válido");
        }
        if (vehiculo.getIdCliente() <= 0) {
            throw new IllegalArgumentException("El ID del cliente debe ser válido");
        }
        if (vehiculo.getMarca() == null || vehiculo.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("La marca no puede estar vacía");
        }
        if (vehiculo.getModelo() == null || vehiculo.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo no puede estar vacío");
        }
        if (vehiculo.getTipoVehiculo() == null) {
            throw new IllegalArgumentException("El tipo de vehículo no puede ser nulo");
        }

        String sql = "UPDATE vehiculo SET marca = ?, modelo = ?, tipo_vehiculo = ? WHERE id_vehiculo = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, vehiculo.getMarca().trim());
            stmt.setString(2, vehiculo.getModelo().trim());
            stmt.setString(3, vehiculo.getTipoVehiculo().name());
            stmt.setInt(4, vehiculo.getIdVehiculo());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM vehiculo WHERE id_vehiculo = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
} 