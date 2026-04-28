package com.taller.model;

import java.sql.Timestamp;

public class Reparacion {
    private int idReparacion;
    private int idVehiculo;
    private String detalle;
    private double costo;
    private MetodoPago metodoPago;
    private Timestamp fechaReparacion;
    private Vehiculo vehiculo; // Referencia al vehículo reparado

    public enum MetodoPago {
        efectivo, tarjeta, transferencia
    }

    public Reparacion() {
    }

    public Reparacion(int idVehiculo, String detalle, double costo, MetodoPago metodoPago) {
        this.idVehiculo = idVehiculo;
        this.detalle = detalle;
        this.costo = costo;
        this.metodoPago = metodoPago;
    }

    // Getters y Setters
    public int getIdReparacion() {
        return idReparacion;
    }

    public void setIdReparacion(int idReparacion) {
        this.idReparacion = idReparacion;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Timestamp getFechaReparacion() {
        return fechaReparacion;
    }

    public void setFechaReparacion(Timestamp fechaReparacion) {
        this.fechaReparacion = fechaReparacion;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    @Override
    public String toString() {
        return "Reparación #" + idReparacion + " - Costo: $" + costo + " - Método: " + metodoPago;
    }
} 