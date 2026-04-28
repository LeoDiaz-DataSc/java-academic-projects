# ☕ Java Academic Projects — Colección de Proyectos Académicos

[![Java](https://img.shields.io/badge/Java-11+-orange?logo=openjdk)](https://openjdk.org/)
[![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?logo=apachemaven)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](./LICENSE)

A curated collection of **Java academic projects** covering CRUD operations, data structures, compiler design, numerical methods, and desktop GUI applications. Each project demonstrates specific computer science concepts and design patterns.

---

## 📚 Project Catalog

### 🏗️ CRUD & Database Applications

| Project | Description | Architecture | Tech |
|---------|-------------|-------------|------|
| **TallerCRUD** | Auto repair shop management | MVC (config/dao/model/view) | Maven, MySQL, JCalendar |
| **CRUD-TEST-MYSQL** | User & roles management | MVC (Controllers/Models/Views) | NetBeans, MySQL |
| **MAVEN-CRUD-JMYSQL** | CRUD with forms | Maven, MySQL | Maven, MySQL |
| **sistemagym** | Gym membership system | Basic (members, employees) | NetBeans |

### 🌳 Data Structures & Algorithms

| Project | Description | Concepts |
|---------|-------------|----------|
| **Arbolnodo** | Symbol tree nodes | Binary trees, AST |
| **Compiladores** | Expression tree parser | Compiler design, parsing |

### 📊 Numerical Methods

| Project | Description | Algorithms |
|---------|-------------|------------|
| **MN_Ejemplo1** | 11 numerical method implementations | Root finding, interpolation, integration |
| **Metodos_Numericos** | Numerical computation examples | Matrix operations |

### 🧩 UI Components

| Project | Description |
|---------|-------------|
| **combobox** | Dependent combo box implementation |

---

## 🏗️ Highlighted Architectures

### TallerCRUD — Auto Repair Shop (Best Architecture)
```
com.taller/
├── config/
│   └── DatabaseConfig.java     # Database configuration
├── dao/
│   ├── ClienteDAO.java         # Client data access
│   ├── VehiculoDAO.java        # Vehicle data access
│   └── ReparacionDAO.java      # Repair data access
├── model/
│   ├── Cliente.java            # Client entity
│   ├── Vehiculo.java           # Vehicle entity
│   └── Reparacion.java         # Repair entity
└── view/frames/
    ├── MainFrame.java          # Main window
    ├── ClienteFrame.java       # Client management
    ├── VehiculoFrame.java      # Vehicle management
    └── ReparacionFrame.java    # Repair tracking
```

### CRUD-TEST-MYSQL — Classic MVC
```
├── Configuracion/Miconexion.java  # DB connection
├── Controllers/
│   ├── PersonaController.java
│   └── RolesController.java
├── Models/
│   ├── Persona.java
│   └── Roles.java
├── Interface/AccionesCRUD.java    # CRUD interface
└── Vistas/Personas.java           # GUI
```

---

## 🚀 Getting Started

### Prerequisites
- Java 11+ (JDK)
- Maven 3.x (for Maven projects)
- MySQL 8.0
- NetBeans (for NetBeans projects)

### Running Maven Projects
```bash
cd [project-folder]
mvn clean compile exec:java
```

---

## 📄 License

This project is licensed under the MIT License — see [LICENSE](./LICENSE) for details.

---

**Developed by [Leonardo Diaz](https://github.com/LeoDiaz-DataSc)**
