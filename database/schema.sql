-- =============================================
-- Java Enterprise Showcase — Base de Datos Unificada
-- Módulos: Taller Mecánico + Gimnasio + Auditoría
-- =============================================
CREATE DATABASE IF NOT EXISTS java_showcase;
USE java_showcase;

-- =============================================
-- MÓDULO 1: TALLER MECÁNICO
-- =============================================

CREATE TABLE IF NOT EXISTS Clientes_Taller (
    ID_Cliente INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(100) NOT NULL,
    Telefono VARCHAR(20),
    Email VARCHAR(100),
    Direccion VARCHAR(255),
    Creado_En DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Vehiculos (
    ID_Vehiculo INT PRIMARY KEY AUTO_INCREMENT,
    ID_Cliente INT NOT NULL,
    Marca VARCHAR(50) NOT NULL,
    Modelo VARCHAR(50) NOT NULL,
    Anio INT,
    Placa VARCHAR(20) UNIQUE,
    Color VARCHAR(30),
    Kilometraje INT DEFAULT 0,
    FOREIGN KEY (ID_Cliente) REFERENCES Clientes_Taller(ID_Cliente) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Reparaciones (
    ID_Reparacion INT PRIMARY KEY AUTO_INCREMENT,
    ID_Vehiculo INT NOT NULL,
    Descripcion TEXT NOT NULL,
    Diagnostico TEXT,
    Costo DECIMAL(10,2) DEFAULT 0.00,
    Estado ENUM('Recibido', 'En Diagnostico', 'En Reparacion', 'Listo', 'Entregado') DEFAULT 'Recibido',
    Prioridad ENUM('Baja', 'Normal', 'Alta', 'Urgente') DEFAULT 'Normal',
    Fecha_Ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    Fecha_Entrega DATETIME,
    FOREIGN KEY (ID_Vehiculo) REFERENCES Vehiculos(ID_Vehiculo) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =============================================
-- MÓDULO 2: GIMNASIO (Gold's Gym)
-- =============================================

CREATE TABLE IF NOT EXISTS Miembros_Gym (
    ID_Miembro INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(50) NOT NULL,
    Apellido VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE,
    Telefono VARCHAR(20),
    Fecha_Nacimiento DATE,
    Genero ENUM('Masculino', 'Femenino', 'Otro'),
    Plan ENUM('Mensual', 'Trimestral', 'Semestral', 'Anual') DEFAULT 'Mensual',
    Fecha_Inicio DATE NOT NULL,
    Fecha_Fin DATE NOT NULL,
    Activo BOOLEAN DEFAULT TRUE,
    Creado_En DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Pagos_Gym (
    ID_Pago INT PRIMARY KEY AUTO_INCREMENT,
    ID_Miembro INT NOT NULL,
    Monto DECIMAL(10,2) NOT NULL,
    Metodo_Pago ENUM('Efectivo', 'Tarjeta', 'Transferencia') DEFAULT 'Efectivo',
    Concepto VARCHAR(100) DEFAULT 'Membresía',
    Fecha_Pago DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_Miembro) REFERENCES Miembros_Gym(ID_Miembro)
) ENGINE=InnoDB;

-- =============================================
-- SEGURIDAD: USUARIOS Y AUTENTICACIÓN (JWT + RBAC)
-- =============================================

CREATE TABLE IF NOT EXISTS Usuarios (
    ID_Usuario INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(50) NOT NULL,
    Apellido VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Contrasena VARCHAR(255) NOT NULL,
    Rol ENUM('admin', 'mecanico', 'instructor', 'recepcion') DEFAULT 'recepcion',
    Activo BOOLEAN DEFAULT TRUE,
    Creado_En DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =============================================
-- TRAZABILIDAD: AUDITORÍA EMPRESARIAL (ISO 27001)
-- =============================================

CREATE TABLE IF NOT EXISTS Audit_Logs (
    ID_Log INT PRIMARY KEY AUTO_INCREMENT,
    ID_Usuario INT,
    Accion VARCHAR(100) NOT NULL,
    Detalle TEXT,
    Direccion_IP VARCHAR(45),
    Fecha_Hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_Usuario) REFERENCES Usuarios(ID_Usuario)
) ENGINE=InnoDB;

-- =============================================
-- DATOS INICIALES (Seed)
-- =============================================

-- Usuarios del sistema
INSERT INTO Usuarios (Nombre, Apellido, Email, Contrasena, Rol) VALUES
('Admin', 'Sistema', 'admin@showcase.com', SHA2('Admin123', 256), 'admin'),
('Carlos', 'Méndez', 'carlos@taller.com', SHA2('Mecanico123', 256), 'mecanico'),
('Laura', 'Ríos', 'laura@gym.com', SHA2('Instructor123', 256), 'instructor');

-- Clientes del Taller
INSERT INTO Clientes_Taller (Nombre, Telefono, Email) VALUES
('Roberto García López', '555-0101', 'roberto@email.com'),
('María Fernández Cruz', '555-0202', 'maria.f@email.com'),
('Javier Herrera Ramos', '555-0303', 'javier.h@email.com'),
('Ana Lucía Torres', '555-0404', 'ana.torres@email.com');

-- Vehículos
INSERT INTO Vehiculos (ID_Cliente, Marca, Modelo, Anio, Placa, Color, Kilometraje) VALUES
(1, 'Toyota', 'Corolla', 2021, 'ABC-1234', 'Blanco', 45000),
(1, 'Honda', 'CR-V', 2019, 'DEF-5678', 'Negro', 72000),
(2, 'Volkswagen', 'Jetta', 2022, 'GHI-9012', 'Gris', 23000),
(3, 'Nissan', 'Sentra', 2020, 'JKL-3456', 'Rojo', 58000),
(4, 'Ford', 'Escape', 2023, 'MNO-7890', 'Azul', 12000);

-- Reparaciones
INSERT INTO Reparaciones (ID_Vehiculo, Descripcion, Diagnostico, Costo, Estado, Prioridad) VALUES
(1, 'Cambio de aceite y filtro', 'Mantenimiento preventivo cada 10,000 km', 850.00, 'Entregado', 'Normal'),
(2, 'Ruido en frenos traseros', 'Pastillas de freno desgastadas al 90%', 2400.00, 'Listo', 'Alta'),
(3, 'Revisión general pre-compra', 'Vehículo en buen estado, batería al 60%', 500.00, 'En Diagnostico', 'Baja'),
(4, 'Motor no enciende', 'Posible falla en bomba de gasolina', 4500.00, 'En Reparacion', 'Urgente'),
(5, 'Alineación y balanceo', 'Desgaste irregular en llanta delantera izq.', 1200.00, 'Recibido', 'Normal');

-- Miembros del Gimnasio
INSERT INTO Miembros_Gym (Nombre, Apellido, Email, Telefono, Genero, Plan, Fecha_Inicio, Fecha_Fin) VALUES
('Pedro', 'Martínez', 'pedro.m@email.com', '555-1001', 'Masculino', 'Anual', '2026-01-15', '2027-01-15'),
('Sofía', 'López', 'sofia.l@email.com', '555-1002', 'Femenino', 'Trimestral', '2026-03-01', '2026-06-01'),
('Diego', 'Ramírez', 'diego.r@email.com', '555-1003', 'Masculino', 'Mensual', '2026-04-01', '2026-05-01'),
('Valentina', 'Castro', 'val.c@email.com', '555-1004', 'Femenino', 'Semestral', '2026-02-10', '2026-08-10'),
('Andrés', 'Sánchez', 'andres.s@email.com', '555-1005', 'Masculino', 'Anual', '2025-12-01', '2026-12-01'),
('Camila', 'Herrera', 'camila.h@email.com', '555-1006', 'Femenino', 'Mensual', '2026-04-15', '2026-05-15');

-- Pagos del Gimnasio
INSERT INTO Pagos_Gym (ID_Miembro, Monto, Metodo_Pago, Concepto) VALUES
(1, 4800.00, 'Tarjeta', 'Membresía Anual'),
(2, 1500.00, 'Efectivo', 'Membresía Trimestral'),
(3, 600.00, 'Transferencia', 'Membresía Mensual'),
(4, 2800.00, 'Tarjeta', 'Membresía Semestral'),
(5, 4800.00, 'Transferencia', 'Membresía Anual'),
(6, 600.00, 'Efectivo', 'Membresía Mensual');
