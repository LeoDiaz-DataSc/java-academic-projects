-- ==============================================================================
-- ENTERPRISE ERP SCHEMA — CAPA 1 (NÚCLEO) & CAPA 4 (CUMPLIMIENTO ISO)
-- Arquitectura Multitenant & CRM Unificado
-- ==============================================================================

CREATE DATABASE IF NOT EXISTS java_showcase;
USE java_showcase;

-- ==============================================================================
-- 1. ESTRUCTURA ORGANIZACIONAL (Multi-Tenant)
-- ==============================================================================

CREATE TABLE IF NOT EXISTS Empresas (
    ID_Empresa INT PRIMARY KEY AUTO_INCREMENT,
    Razon_Social VARCHAR(150) NOT NULL,
    RFC VARCHAR(15) UNIQUE NOT NULL,
    Regimen_Fiscal VARCHAR(50),
    Direccion_Fiscal TEXT,
    Activo BOOLEAN DEFAULT TRUE,
    Creado_En DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Sucursales (
    ID_Sucursal INT PRIMARY KEY AUTO_INCREMENT,
    ID_Empresa INT NOT NULL,
    Nombre VARCHAR(100) NOT NULL,
    Tipo ENUM('Taller', 'Gimnasio', 'Mixto', 'Corporativo') NOT NULL,
    Direccion TEXT,
    Telefono VARCHAR(20),
    Activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (ID_Empresa) REFERENCES Empresas(ID_Empresa) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==============================================================================
-- 2. RECURSOS HUMANOS & NÓMINA (Capa 1)
-- ==============================================================================

CREATE TABLE IF NOT EXISTS Departamentos (
    ID_Departamento INT PRIMARY KEY AUTO_INCREMENT,
    ID_Sucursal INT NOT NULL,
    Nombre VARCHAR(50) NOT NULL,
    FOREIGN KEY (ID_Sucursal) REFERENCES Sucursales(ID_Sucursal)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Empleados (
    ID_Empleado INT PRIMARY KEY AUTO_INCREMENT,
    ID_Departamento INT,
    Nombres VARCHAR(50) NOT NULL,
    Apellidos VARCHAR(50) NOT NULL,
    CURP VARCHAR(18) UNIQUE,
    RFC VARCHAR(15) UNIQUE,
    NSS VARCHAR(15) UNIQUE, -- Número de Seguridad Social
    Fecha_Ingreso DATE NOT NULL,
    Salario_Base DECIMAL(12,2),
    Tipo_Contrato ENUM('Indeterminado', 'Temporal', 'Honorarios') DEFAULT 'Indeterminado',
    Huella_Biometrica TEXT, -- Template de huella dactilar o FaceID
    Activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (ID_Departamento) REFERENCES Departamentos(ID_Departamento)
) ENGINE=InnoDB;

-- ==============================================================================
-- 3. CRM UNIFICADO: PERSONAS (Capa 1)
-- Una persona puede ser cliente del taller, socio del gym, o ambos.
-- ==============================================================================

CREATE TABLE IF NOT EXISTS Personas (
    ID_Persona INT PRIMARY KEY AUTO_INCREMENT,
    Nombres VARCHAR(50) NOT NULL,
    Apellidos VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE,
    Telefono_Movil VARCHAR(20),
    Fecha_Nacimiento DATE,
    Genero ENUM('Masculino', 'Femenino', 'Otro', 'Prefiero no decirlo'),
    Acepta_Marketing BOOLEAN DEFAULT FALSE, -- GDPR / LFPDPPP
    Aviso_Privacidad_Aceptado DATETIME,
    Nivel_Lealtad ENUM('Bronce', 'Plata', 'Oro', 'Platino') DEFAULT 'Bronce',
    Puntos_Lealtad INT DEFAULT 0,
    Creado_En DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Datos_Facturacion_Clientes (
    ID_DatoFacturacion INT PRIMARY KEY AUTO_INCREMENT,
    ID_Persona INT NOT NULL,
    Razon_Social VARCHAR(150),
    RFC VARCHAR(15),
    Regimen_Fiscal VARCHAR(50),
    Codigo_Postal VARCHAR(10),
    Uso_CFDI VARCHAR(5),
    Predeterminado BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (ID_Persona) REFERENCES Personas(ID_Persona) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==============================================================================
-- 4. SEGURIDAD, IAM Y RBAC (Identity & Access Management)
-- ==============================================================================

CREATE TABLE IF NOT EXISTS Roles (
    ID_Rol INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(50) NOT NULL UNIQUE,
    Nivel_Acceso INT DEFAULT 1 -- 100=SuperAdmin, 50=Gerente, 10=Operativo
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Usuarios (
    ID_Usuario INT PRIMARY KEY AUTO_INCREMENT,
    ID_Empleado INT, -- Null si es usuario externo o superadmin de sistema
    ID_Rol INT NOT NULL,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Contrasena_Hash VARCHAR(255) NOT NULL,
    Requiere_MFA BOOLEAN DEFAULT FALSE, -- Multi-Factor Authentication
    Ultimo_Acceso DATETIME,
    Activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (ID_Empleado) REFERENCES Empleados(ID_Empleado) ON DELETE SET NULL,
    FOREIGN KEY (ID_Rol) REFERENCES Roles(ID_Rol)
) ENGINE=InnoDB;

-- ==============================================================================
-- 5. CAJA, PUNTO DE VENTA Y FACTURACIÓN (Capa 1)
-- ==============================================================================

CREATE TABLE IF NOT EXISTS Cajas (
    ID_Caja INT PRIMARY KEY AUTO_INCREMENT,
    ID_Sucursal INT NOT NULL,
    Nombre VARCHAR(50) NOT NULL,
    Fondo_Inicial DECIMAL(10,2) DEFAULT 0.00,
    Estado ENUM('Abierta', 'Cerrada') DEFAULT 'Cerrada',
    FOREIGN KEY (ID_Sucursal) REFERENCES Sucursales(ID_Sucursal)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Turnos_Caja (
    ID_Turno INT PRIMARY KEY AUTO_INCREMENT,
    ID_Caja INT NOT NULL,
    ID_Usuario_Apertura INT NOT NULL,
    Fecha_Apertura DATETIME NOT NULL,
    Fondo_Apertura DECIMAL(10,2) NOT NULL,
    ID_Usuario_Cierre INT,
    Fecha_Cierre DATETIME,
    Fondo_Cierre_Declarado DECIMAL(10,2),
    Fondo_Cierre_Calculado DECIMAL(10,2),
    FOREIGN KEY (ID_Caja) REFERENCES Cajas(ID_Caja),
    FOREIGN KEY (ID_Usuario_Apertura) REFERENCES Usuarios(ID_Usuario),
    FOREIGN KEY (ID_Usuario_Cierre) REFERENCES Usuarios(ID_Usuario)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Transacciones (
    ID_Transaccion INT PRIMARY KEY AUTO_INCREMENT,
    ID_Turno INT NOT NULL,
    ID_Persona INT, -- Quién pagó
    Tipo ENUM('Ingreso', 'Egreso', 'Devolucion') NOT NULL,
    Monto DECIMAL(12,2) NOT NULL,
    Metodo_Pago ENUM('Efectivo', 'Tarjeta', 'Transferencia', 'Link Pago', 'Multiple') NOT NULL,
    Referencia_Externa VARCHAR(100), -- ID de Stripe/Clip/SPEI
    Requiere_Factura BOOLEAN DEFAULT FALSE,
    Estado_Facturacion ENUM('No Solicitada', 'Pendiente', 'Timbrada', 'Cancelada') DEFAULT 'No Solicitada',
    UUID_CFDI VARCHAR(36),
    Fecha_Transaccion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_Turno) REFERENCES Turnos_Caja(ID_Turno),
    FOREIGN KEY (ID_Persona) REFERENCES Personas(ID_Persona)
) ENGINE=InnoDB;

-- ==============================================================================
-- 6. CUMPLIMIENTO ISO TRANSVERSAL (Capa 4)
-- ==============================================================================

-- ISO 27001 (Seguridad de la Información)
CREATE TABLE IF NOT EXISTS Audit_Logs (
    ID_Log INT PRIMARY KEY AUTO_INCREMENT,
    ID_Usuario INT,
    Modulo VARCHAR(50), -- ej: 'CRM', 'Caja', 'OT'
    Accion VARCHAR(100) NOT NULL,
    Detalle JSON, -- Guarda el snapshot del cambio (before/after)
    Direccion_IP VARCHAR(45),
    User_Agent VARCHAR(255),
    Fecha_Hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_Usuario) REFERENCES Usuarios(ID_Usuario)
) ENGINE=InnoDB;

-- ISO 9001 (Calidad)
CREATE TABLE IF NOT EXISTS ISO_No_Conformidades (
    ID_NC INT PRIMARY KEY AUTO_INCREMENT,
    ID_Sucursal INT NOT NULL,
    Reportado_Por INT NOT NULL, -- Usuario que reporta
    Origen ENUM('Auditoria Externa', 'Auditoria Interna', 'Queja Cliente', 'Operacion Diaria') NOT NULL,
    Descripcion TEXT NOT NULL,
    Analisis_Causa_Raiz TEXT,
    Estado ENUM('Abierta', 'En Analisis', 'Accion Correctiva', 'Verificada', 'Cerrada') DEFAULT 'Abierta',
    Fecha_Apertura DATETIME DEFAULT CURRENT_TIMESTAMP,
    Fecha_Cierre DATETIME,
    FOREIGN KEY (ID_Sucursal) REFERENCES Sucursales(ID_Sucursal),
    FOREIGN KEY (Reportado_Por) REFERENCES Usuarios(ID_Usuario)
) ENGINE=InnoDB;

-- ISO 45001 (Seguridad Ocupacional)
CREATE TABLE IF NOT EXISTS ISO_Incidentes (
    ID_Incidente INT PRIMARY KEY AUTO_INCREMENT,
    ID_Sucursal INT NOT NULL,
    ID_Empleado_Afectado INT,
    Nivel_Gravedad ENUM('Leve', 'Moderado', 'Grave', 'Fatal', 'Casi Accidente') NOT NULL,
    Descripcion TEXT NOT NULL,
    Lugar_Especifico VARCHAR(100),
    Requiere_Incapacidad BOOLEAN DEFAULT FALSE,
    Dias_Incapacidad INT DEFAULT 0,
    Fecha_Incidente DATETIME NOT NULL,
    Reportado_En DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_Sucursal) REFERENCES Sucursales(ID_Sucursal),
    FOREIGN KEY (ID_Empleado_Afectado) REFERENCES Empleados(ID_Empleado)
) ENGINE=InnoDB;

-- ISO 14001 (Medio Ambiente)
CREATE TABLE IF NOT EXISTS ISO_Residuos (
    ID_Residuo INT PRIMARY KEY AUTO_INCREMENT,
    ID_Sucursal INT NOT NULL,
    Tipo_Residuo ENUM('Aceite Quemado', 'Filtros', 'Baterias', 'Neumaticos', 'Solventes', 'Electronicos') NOT NULL,
    Cantidad DECIMAL(10,2) NOT NULL,
    Unidad_Medida ENUM('Litros', 'Kg', 'Piezas') NOT NULL,
    Manifiesto_Entrega VARCHAR(50), -- Folio del proveedor certificado
    Fecha_Recoleccion DATE NOT NULL,
    FOREIGN KEY (ID_Sucursal) REFERENCES Sucursales(ID_Sucursal)
) ENGINE=InnoDB;

-- ==============================================================================
-- 7. CAPA 2: MÓDULO TALLER AUTOMOTRIZ
-- ==============================================================================

CREATE TABLE IF NOT EXISTS Vehiculos (
    ID_Vehiculo INT PRIMARY KEY AUTO_INCREMENT,
    ID_Persona INT NOT NULL, -- Propietario actual
    VIN VARCHAR(17) UNIQUE NOT NULL,
    Placa VARCHAR(15),
    Marca VARCHAR(50) NOT NULL,
    Modelo VARCHAR(50) NOT NULL,
    Anio INT NOT NULL,
    Color VARCHAR(30),
    Kilometraje_Actual INT DEFAULT 0,
    Ultimo_Servicio DATETIME,
    FOREIGN KEY (ID_Persona) REFERENCES Personas(ID_Persona)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Ordenes_Trabajo (
    ID_OT INT PRIMARY KEY AUTO_INCREMENT,
    ID_Vehiculo INT NOT NULL,
    ID_Sucursal INT NOT NULL,
    ID_Asesor INT NOT NULL, -- Quien recibe
    ID_Mecanico INT, -- Asignado
    Sintomas_Reportados TEXT NOT NULL,
    Kilometraje_Ingreso INT NOT NULL,
    Nivel_Combustible ENUM('Vacio', '1/4', '1/2', '3/4', 'Lleno') NOT NULL,
    Estado ENUM('Recepción', 'Diagnóstico', 'En Espera Refacciones', 'En Reparación', 'Control Calidad', 'Lista Entrega', 'Entregado', 'Cancelada') DEFAULT 'Recepción',
    Prioridad ENUM('Baja', 'Normal', 'Alta', 'Siniestro Aseguradora') DEFAULT 'Normal',
    Costo_Estimado DECIMAL(10,2) DEFAULT 0.00,
    Notas_Diagnostico TEXT,
    Firma_Digital_Cliente TEXT,
    Fecha_Ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    Fecha_Promesa DATETIME,
    Fecha_Entrega DATETIME,
    FOREIGN KEY (ID_Vehiculo) REFERENCES Vehiculos(ID_Vehiculo),
    FOREIGN KEY (ID_Sucursal) REFERENCES Sucursales(ID_Sucursal),
    FOREIGN KEY (ID_Asesor) REFERENCES Empleados(ID_Empleado),
    FOREIGN KEY (ID_Mecanico) REFERENCES Empleados(ID_Empleado)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Inventario_Refacciones (
    ID_Refaccion INT PRIMARY KEY AUTO_INCREMENT,
    ID_Sucursal INT NOT NULL,
    Codigo_SKU VARCHAR(50) UNIQUE NOT NULL,
    Nombre VARCHAR(100) NOT NULL,
    Categoria VARCHAR(50),
    Marca VARCHAR(50),
    Stock_Actual INT DEFAULT 0,
    Stock_Minimo INT DEFAULT 2,
    Costo_Compra DECIMAL(10,2) NOT NULL,
    Precio_Venta DECIMAL(10,2) NOT NULL,
    Ubicacion_Pasillo VARCHAR(20),
    FOREIGN KEY (ID_Sucursal) REFERENCES Sucursales(ID_Sucursal)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS OT_Servicios_Refacciones (
    ID_OT_Item INT PRIMARY KEY AUTO_INCREMENT,
    ID_OT INT NOT NULL,
    ID_Refaccion INT, -- Puede ser null si es solo Mano de Obra
    Tipo ENUM('Refaccion', 'Mano Obra', 'Servicio Externo') NOT NULL,
    Descripcion VARCHAR(150) NOT NULL,
    Cantidad DECIMAL(8,2) DEFAULT 1.00,
    Precio_Unitario DECIMAL(10,2) NOT NULL,
    Subtotal DECIMAL(10,2) GENERATED ALWAYS AS (Cantidad * Precio_Unitario) STORED,
    Mecanico_Ejecutor INT,
    FOREIGN KEY (ID_OT) REFERENCES Ordenes_Trabajo(ID_OT) ON DELETE CASCADE,
    FOREIGN KEY (ID_Refaccion) REFERENCES Inventario_Refacciones(ID_Refaccion),
    FOREIGN KEY (Mecanico_Ejecutor) REFERENCES Empleados(ID_Empleado)
) ENGINE=InnoDB;

-- ==============================================================================
-- 8. CAPA 3: MÓDULO GIMNASIO Y BIENESTAR
-- ==============================================================================

CREATE TABLE IF NOT EXISTS Cat_Planes_Membresia (
    ID_Plan INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(100) NOT NULL,
    Descripcion TEXT,
    Duracion_Dias INT NOT NULL,
    Precio DECIMAL(10,2) NOT NULL,
    Horario_Acceso ENUM('Libre', 'Matutino', 'Vespertino', 'Fines de Semana') DEFAULT 'Libre',
    Activo BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Socio_Membresias (
    ID_Membresia INT PRIMARY KEY AUTO_INCREMENT,
    ID_Persona INT NOT NULL,
    ID_Plan INT NOT NULL,
    ID_Sucursal_Base INT NOT NULL,
    Fecha_Inicio DATE NOT NULL,
    Fecha_Fin DATE NOT NULL,
    Estado ENUM('Vigente', 'Por Vencer', 'Vencida', 'Congelada', 'Cancelada') DEFAULT 'Vigente',
    Renovacion_Automatica BOOLEAN DEFAULT FALSE,
    Token_Tarjeta VARCHAR(100), -- Integración pasarela de pagos
    FOREIGN KEY (ID_Persona) REFERENCES Personas(ID_Persona),
    FOREIGN KEY (ID_Plan) REFERENCES Cat_Planes_Membresia(ID_Plan),
    FOREIGN KEY (ID_Sucursal_Base) REFERENCES Sucursales(ID_Sucursal)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Control_Accesos (
    ID_Acceso INT PRIMARY KEY AUTO_INCREMENT,
    ID_Persona INT NOT NULL,
    ID_Sucursal INT NOT NULL,
    Tipo_Acceso ENUM('Entrada', 'Salida') NOT NULL,
    Metodo ENUM('App QR', 'Huella', 'Tarjeta NFC', 'Manual') NOT NULL,
    Estatus_Membresia_Al_Entrar ENUM('Valida', 'Vencida', 'Adeudo', 'Restringido por Horario') NOT NULL,
    Fecha_Hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_Persona) REFERENCES Personas(ID_Persona),
    FOREIGN KEY (ID_Sucursal) REFERENCES Sucursales(ID_Sucursal)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Clases_Grupales (
    ID_Clase INT PRIMARY KEY AUTO_INCREMENT,
    ID_Sucursal INT NOT NULL,
    ID_Instructor INT NOT NULL,
    Nombre VARCHAR(100) NOT NULL,
    Capacidad_Maxima INT NOT NULL,
    Fecha_Hora_Inicio DATETIME NOT NULL,
    Fecha_Hora_Fin DATETIME NOT NULL,
    Ubicacion VARCHAR(50), -- ej: Salon 1, Alberca
    Estado ENUM('Programada', 'En Curso', 'Finalizada', 'Cancelada') DEFAULT 'Programada',
    FOREIGN KEY (ID_Sucursal) REFERENCES Sucursales(ID_Sucursal),
    FOREIGN KEY (ID_Instructor) REFERENCES Empleados(ID_Empleado)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Reservas_Clases (
    ID_Reserva INT PRIMARY KEY AUTO_INCREMENT,
    ID_Clase INT NOT NULL,
    ID_Persona INT NOT NULL,
    Estado ENUM('Reservada', 'Asistio', 'Falta', 'Cancelada') DEFAULT 'Reservada',
    Fecha_Reserva DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_Clase) REFERENCES Clases_Grupales(ID_Clase),
    FOREIGN KEY (ID_Persona) REFERENCES Personas(ID_Persona)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Seguimiento_Fisico (
    ID_Medicion INT PRIMARY KEY AUTO_INCREMENT,
    ID_Persona INT NOT NULL,
    ID_Instructor INT, -- Quien tomó las medidas
    Peso_Kg DECIMAL(5,2),
    Porcentaje_Grasa DECIMAL(5,2),
    Masa_Muscular_Kg DECIMAL(5,2),
    Cintura_cm DECIMAL(5,2),
    Pecho_cm DECIMAL(5,2),
    Brazo_cm DECIMAL(5,2),
    Pierna_cm DECIMAL(5,2),
    Comentarios TEXT,
    Fecha_Medicion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_Persona) REFERENCES Personas(ID_Persona),
    FOREIGN KEY (ID_Instructor) REFERENCES Empleados(ID_Empleado)
) ENGINE=InnoDB;

-- ==============================================================================
-- 9. MÓDULOS ADICIONALES INDISPENSABLES (Basado en Arquitectura Completa)
-- ==============================================================================

-- Taller: Control de Bahías
CREATE TABLE IF NOT EXISTS Taller_Bahias (
    ID_Bahia INT PRIMARY KEY AUTO_INCREMENT,
    ID_Sucursal INT NOT NULL,
    Nombre VARCHAR(50) NOT NULL,
    Tipo ENUM('Diagnostico', 'Reparacion', 'Lavado', 'Entregas') NOT NULL,
    Estado ENUM('Disponible', 'Ocupada', 'Mantenimiento') DEFAULT 'Disponible',
    ID_OT_Actual INT, -- Qué OT se está trabajando aquí
    FOREIGN KEY (ID_Sucursal) REFERENCES Sucursales(ID_Sucursal),
    FOREIGN KEY (ID_OT_Actual) REFERENCES Ordenes_Trabajo(ID_OT) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Taller: Proveedores y Compras
CREATE TABLE IF NOT EXISTS Proveedores (
    ID_Proveedor INT PRIMARY KEY AUTO_INCREMENT,
    Razon_Social VARCHAR(150) NOT NULL,
    RFC VARCHAR(15) UNIQUE,
    Contacto_Principal VARCHAR(100),
    Telefono VARCHAR(20),
    Email VARCHAR(100),
    Dias_Credito INT DEFAULT 0
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Compras_Refacciones (
    ID_Compra INT PRIMARY KEY AUTO_INCREMENT,
    ID_Sucursal INT NOT NULL,
    ID_Proveedor INT NOT NULL,
    Factura_Proveedor VARCHAR(50),
    Monto_Total DECIMAL(12,2) NOT NULL,
    Fecha_Compra DATETIME DEFAULT CURRENT_TIMESTAMP,
    Estado ENUM('Pendiente', 'Recibido', 'Pagado') DEFAULT 'Pendiente',
    FOREIGN KEY (ID_Sucursal) REFERENCES Sucursales(ID_Sucursal),
    FOREIGN KEY (ID_Proveedor) REFERENCES Proveedores(ID_Proveedor)
) ENGINE=InnoDB;

-- Gym: Rutinas y Nutrición
CREATE TABLE IF NOT EXISTS Gym_Ejercicios (
    ID_Ejercicio INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(100) NOT NULL,
    Grupo_Muscular VARCHAR(50),
    URL_Video TEXT,
    Instrucciones TEXT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Gym_Rutinas_Asignadas (
    ID_Rutina INT PRIMARY KEY AUTO_INCREMENT,
    ID_Persona INT NOT NULL,
    ID_Instructor INT NOT NULL,
    Objetivo ENUM('Hipertrofia', 'Fuerza', 'Perdida de Peso', 'Rehabilitacion') NOT NULL,
    Fecha_Asignacion DATE NOT NULL,
    Fecha_Vencimiento DATE,
    FOREIGN KEY (ID_Persona) REFERENCES Personas(ID_Persona),
    FOREIGN KEY (ID_Instructor) REFERENCES Empleados(ID_Empleado)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Gym_Rutina_Detalle (
    ID_Detalle INT PRIMARY KEY AUTO_INCREMENT,
    ID_Rutina INT NOT NULL,
    ID_Ejercicio INT NOT NULL,
    Series INT NOT NULL,
    Repeticiones VARCHAR(50) NOT NULL,
    Descanso_Segundos INT DEFAULT 60,
    Observaciones TEXT,
    FOREIGN KEY (ID_Rutina) REFERENCES Gym_Rutinas_Asignadas(ID_Rutina) ON DELETE CASCADE,
    FOREIGN KEY (ID_Ejercicio) REFERENCES Gym_Ejercicios(ID_Ejercicio)
) ENGINE=InnoDB;

-- Gym: Tienda y Suplementos
CREATE TABLE IF NOT EXISTS Gym_Productos_Tienda (
    ID_Producto INT PRIMARY KEY AUTO_INCREMENT,
    ID_Sucursal INT NOT NULL,
    Codigo_Barras VARCHAR(50) UNIQUE,
    Nombre VARCHAR(100) NOT NULL,
    Categoria ENUM('Suplementos', 'Ropa', 'Bebidas', 'Accesorios') NOT NULL,
    Precio_Venta DECIMAL(10,2) NOT NULL,
    Stock_Actual INT DEFAULT 0,
    FOREIGN KEY (ID_Sucursal) REFERENCES Sucursales(ID_Sucursal)
) ENGINE=InnoDB;
