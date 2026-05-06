# 🏢 Java Enterprise Showcase

> **Plataforma web empresarial** que moderniza proyectos Java académicos (Swing Desktop) a una arquitectura web moderna con Node.js API, React SPA y animaciones GSAP de alto rendimiento.

![Node.js](https://img.shields.io/badge/Node.js-20-339933?logo=nodedotjs&logoColor=white)
![React](https://img.shields.io/badge/React-19-61DAFB?logo=react&logoColor=white)
![GSAP](https://img.shields.io/badge/GSAP-3.15-88CE02?logo=greensock&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?logo=docker&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue)

---

## 📋 Descripción

Este proyecto transforma tres aplicaciones Java de escritorio en una **plataforma web unificada** con tres módulos empresariales:

| Módulo | Descripción | Legacy (Java) |
|--------|------------|---------------|
| 🔧 **Taller Mecánico** | Gestión de clientes, vehículos y reparaciones con máquina de estados | `TallerCRUD/` (Maven + JDBC) |
| 💪 **Gimnasio** | Control de miembros, membresías, pagos y KPIs operativos | `sistemagym/` (Java Swing) |
| 🌳 **BST Visualizer** | Árbol Binario de Búsqueda interactivo con animaciones GSAP | `Arbolnodo/` (Java Console) |

---

## 🏗️ Arquitectura

```
java-enterprise-showcase/
├── legacy/                  ← Código Java original (preservado)
│   ├── TallerCRUD/          ← Maven + JDBC + DAO Pattern
│   ├── sistemagym/          ← Java Swing + MySQL
│   ├── Arbolnodo/           ← BST en Java
│   └── Compiladores/        ← Análisis Léxico
├── backend/                 ← Node.js + Express API REST
│   ├── routes/              ← auth.js, taller.js, gym.js
│   ├── middleware/          ← JWT Auth + Audit Logs (ISO 27001)
│   └── config/              ← Database pool (mysql2)
├── frontend/                ← React 19 + Vite + GSAP
│   └── src/components/
│       ├── Taller/          ← Dashboard + KPIs + Reparaciones
│       ├── Gym/             ← Miembros + Pagos + Distribución
│       └── DSA/             ← BST Visualizer interactivo
├── database/                ← MySQL Schema + Seed Data
└── docker-compose.yml       ← MySQL + Backend + Frontend
```

---

## 🔐 Seguridad (ISO 27001)

- **JWT Authentication** — Login con tokens firmados y expiración configurable
- **SHA2-256** — Contraseñas cifradas en base de datos
- **Audit Logs** — Middleware silencioso que registra IP, usuario, acción y timestamp
- **Variables de Entorno** — Cero credenciales en código fuente (`.env.example`)
- **Prepared Statements** — Protección contra SQL Injection

---

## 🎨 UI/UX Premium

- **GSAP Animations** — Timeline, stagger, `back.out` easing en BST Visualizer
- **Design System** — Variables HSL, dark theme, glassmorphism cards
- **Recharts** — Gráficas interactivas (PieChart distribución de planes)
- **Responsive** — Grid system adaptable a cualquier resolución

---

## 🚀 Quick Start

### Desarrollo Local

```bash
# 1. Base de datos
mysql -u root -p < database/schema.sql

# 2. Backend
cd backend
cp .env.example .env    # Editar credenciales
npm install
npm run dev             # → http://localhost:3000

# 3. Frontend
cd frontend
npm install
npm run dev             # → http://localhost:5173
```

### Docker (Un comando)

```bash
docker-compose up -d
# Frontend:  http://localhost:5180
# API:       http://localhost:3010
# MySQL:     localhost:3310
```

---

## 📊 Credenciales de Prueba

| Email | Contraseña | Rol |
|-------|-----------|-----|
| `admin@showcase.com` | `Admin123` | Administrador |
| `carlos@taller.com` | `Mecanico123` | Mecánico |
| `laura@gym.com` | `Instructor123` | Instructor |

---

## 📜 Licencia

MIT License — Copyright (c) 2026 Leonardo Diaz
