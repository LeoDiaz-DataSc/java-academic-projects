package com.taller.dao;

import com.taller.config.DatabaseConfig;
import com.taller.model.Reparacion;
import com.taller.model.Reparacion.MetodoPago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReparacionDAO {
    
    private final Connection conexion;

    public ReparacionDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public void insertar(Reparacion reparacion) throws SQLException {
        String sql = "INSERT INTO reparacion (id_vehiculo, detalle, costo, metodo_pago) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reparacion.getIdVehiculo());
            stmt.setString(2, reparacion.getDetalle());
            stmt.setDouble(3, reparacion.getCosto());
            stmt.setString(4, reparacion.getMetodoPago().name());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    reparacion.setIdReparacion(rs.getInt(1));
                }
            }
        }
    }
    
    public Reparacion obtenerPorId(int idReparacion) throws SQLException {
        String sql = "SELECT * FROM reparacion WHERE id_reparacion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idReparacion);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return crearReparacionDesdeResultSet(rs);
                }
            }
        }
        return null;
    }
    
    public List<Reparacion> listarTodas() throws SQLException {
        List<Reparacion> reparaciones = new ArrayList<>();
        String sql = "SELECT * FROM reparacion";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Reparacion reparacion = new Reparacion();
                reparacion.setIdReparacion(rs.getInt("id_reparacion"));
                reparacion.setIdVehiculo(rs.getInt("id_vehiculo"));
                reparacion.setDetalle(rs.getString("detalle"));
                reparacion.setCosto(rs.getDouble("costo"));
                reparacion.setMetodoPago(Reparacion.MetodoPago.valueOf(rs.getString("metodo_pago")));
                reparacion.setFechaReparacion(rs.getTimestamp("fecha_reparacion"));
                reparaciones.add(reparacion);
            }
        }
        return reparaciones;
    }
    
    public void actualizar(Reparacion reparacion) throws SQLException {
        String sql = "UPDATE reparacion SET detalle = ?, costo = ?, metodo_pago = ? WHERE id_reparacion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, reparacion.getDetalle());
            stmt.setDouble(2, reparacion.getCosto());
            stmt.setString(3, reparacion.getMetodoPago().name());
            stmt.setInt(4, reparacion.getIdReparacion());
            stmt.executeUpdate();
        }
    }
    
    public void eliminar(int idReparacion) throws SQLException {
        String sql = "DELETE FROM reparacion WHERE id_reparacion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idReparacion);
            stmt.executeUpdate();
        }
    }
    
    public List<Reparacion> listarPorVehiculo(int idVehiculo) throws SQLException {
        List<Reparacion> reparaciones = new ArrayList<>();
        String sql = "SELECT * FROM reparacion WHERE id_vehiculo = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idVehiculo);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reparacion reparacion = new Reparacion();
                    reparacion.setIdReparacion(rs.getInt("id_reparacion"));
                    reparacion.setIdVehiculo(rs.getInt("id_vehiculo"));
                    reparacion.setDetalle(rs.getString("detalle"));
                    reparacion.setCosto(rs.getDouble("costo"));
                    reparacion.setMetodoPago(Reparacion.MetodoPago.valueOf(rs.getString("metodo_pago")));
                    reparacion.setFechaReparacion(rs.getTimestamp("fecha_reparacion"));
                    reparaciones.add(reparacion);
                }
            }
        }
        return reparaciones;
    }
    
    public double calcularTotalReparaciones(int idVehiculo) throws SQLException {
        String sql = "SELECT SUM(costo) FROM reparacion WHERE id_vehiculo = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idVehiculo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }

    private Reparacion crearReparacionDesdeResultSet(ResultSet rs) throws SQLException {
        Reparacion reparacion = new Reparacion();
        reparacion.setIdReparacion(rs.getInt("id_reparacion"));
        reparacion.setIdVehiculo(rs.getInt("id_vehiculo"));
        reparacion.setDetalle(rs.getString("detalle"));
        reparacion.setCosto(rs.getDouble("costo"));
        reparacion.setMetodoPago(Reparacion.MetodoPago.valueOf(rs.getString("metodo_pago")));
        reparacion.setFechaReparacion(rs.getTimestamp("fecha_reparacion"));
        return reparacion;
    }
} 