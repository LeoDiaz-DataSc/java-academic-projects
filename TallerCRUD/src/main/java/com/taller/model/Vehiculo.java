package com.taller.model;

import java.sql.Timestamp;

public class Vehiculo {
    private int idVehiculo;
    private int idCliente;
    private String marca;
    private String modelo;
    private TipoVehiculo tipoVehiculo;
    private Cliente cliente; // Referencia al cliente propietario

    public enum TipoVehiculo {
        Sedan("Sedán"),
        SUV("SUV"),
        Pickup("Pickup"),
        Moto("Motocicleta"),
        Camion("Camión"),
        Otro("Otro");

        private final String descripcion;

        TipoVehiculo(String descripcion) {
            this.descripcion = descripcion;
        }

        @Override
        public String toString() {
            return descripcion;
        }
    }

    public Vehiculo() {
    }

    public Vehiculo(int idCliente, String marca, String modelo, TipoVehiculo tipoVehiculo) {
        this.idCliente = idCliente;
        this.marca = marca;
        this.modelo = modelo;
        this.tipoVehiculo = tipoVehiculo;
    }

    // Getters y Setters
    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return marca + " " + modelo + " (" + tipoVehiculo + ")";
    }
} 