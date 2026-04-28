# Java Academic Projects

[![Java](https://img.shields.io/badge/Java-11+-orange?logo=openjdk)](https://openjdk.org/)
[![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?logo=apachemaven)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](./LICENSE)

## Overview

A curated collection of Java academic projects covering CRUD operations with relational databases, data structures, compiler design (expression tree parsing), numerical methods, and desktop GUI development. Each project demonstrates specific computer science fundamentals and software design patterns.

## Project Catalog

### CRUD and Database Applications

| Project | Description | Architecture |
|---------|-------------|-------------|
| **TallerCRUD** | Auto repair shop: clients, vehicles, repairs | MVC with config/dao/model/view layers |
| **CRUD-TEST-MYSQL** | User and role management | MVC with Controllers/Models/Views |
| **MAVEN-CRUD-JMYSQL** | CRUD with GUI forms | Maven, MySQL |
| **sistemagym** | Gym membership: employees, members | NetBeans desktop |

### Data Structures and Compiler Design

| Project | Description |
|---------|-------------|
| **Arbolnodo** | Symbol tree node implementation for AST construction |
| **Compiladores** | Expression tree parser for compiler design |

### Numerical Methods

| Project | Description |
|---------|-------------|
| **MN_Ejemplo1** | 11 implementations: root finding, interpolation, numerical integration |
| **Metodos_Numericos** | Matrix-based numerical computation |

## Highlighted Architecture: TallerCRUD

```
com.taller/
    config/DatabaseConfig.java       # Database configuration
    dao/
        ClienteDAO.java              # Client data access
        VehiculoDAO.java             # Vehicle data access
        ReparacionDAO.java           # Repair data access
    model/
        Cliente.java                 # Client entity
        Vehiculo.java                # Vehicle entity
        Reparacion.java              # Repair entity
    view/frames/
        MainFrame.java               # Main application window
        ClienteFrame.java            # Client management
        VehiculoFrame.java           # Vehicle management
        ReparacionFrame.java         # Repair tracking
```

## Installation

### Prerequisites
- JDK 11+, Maven 3.x, MySQL 8.0

```bash
cd [project-folder]
mvn clean compile exec:java
```

## License

MIT License. See [LICENSE](./LICENSE).

**Developed by [Leonardo Diaz](https://github.com/LeoDiaz-DataSc)**

---

# Version en Espanol

## Descripcion General

Coleccion curada de proyectos academicos en Java que cubren operaciones CRUD con bases de datos relacionales, estructuras de datos, diseno de compiladores (analisis de arboles de expresion), metodos numericos y desarrollo de interfaces graficas de escritorio. Cada proyecto demuestra fundamentos especificos de ciencias de la computacion y patrones de diseno de software.

## Catalogo de Proyectos

### Aplicaciones CRUD y Base de Datos

| Proyecto | Descripcion | Arquitectura |
|----------|-------------|-------------|
| **TallerCRUD** | Taller mecanico: clientes, vehiculos, reparaciones | MVC con capas config/dao/model/view |
| **CRUD-TEST-MYSQL** | Gestion de usuarios y roles | MVC con Controllers/Models/Views |
| **MAVEN-CRUD-JMYSQL** | CRUD con formularios GUI | Maven, MySQL |
| **sistemagym** | Sistema de gimnasio: empleados, miembros | Escritorio NetBeans |

### Estructuras de Datos y Diseno de Compiladores

| Proyecto | Descripcion |
|----------|-------------|
| **Arbolnodo** | Implementacion de nodo de arbol de simbolos para construccion de AST |
| **Compiladores** | Analizador de arbol de expresiones para diseno de compiladores |

### Metodos Numericos

| Proyecto | Descripcion |
|----------|-------------|
| **MN_Ejemplo1** | 11 implementaciones: busqueda de raices, interpolacion, integracion numerica |
| **Metodos_Numericos** | Computacion numerica basada en matrices |

## Instalacion

### Requisitos Previos
- JDK 11+, Maven 3.x, MySQL 8.0

```bash
cd [carpeta-del-proyecto]
mvn clean compile exec:java
```

**Desarrollado por [Leonardo Diaz](https://github.com/LeoDiaz-DataSc)**
