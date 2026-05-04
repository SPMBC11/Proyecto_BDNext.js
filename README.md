# 🍽️ Sistema de Gestión de Restaurante

![Status](https://img.shields.io/badge/status-active-brightgreen)
![Version](https://img.shields.io/badge/version-1.0-blue)
![License](https://img.shields.io/badge/license-Educational-informational)

Sistema integral web para la administración y operación de un restaurante, desarrollado como proyecto académico para "Bases de Datos - Administración y Aplicaciones".

## 📋 Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Arquitectura Técnica](#arquitectura-técnica)
- [Base de Datos](#base-de-datos)
- [Roles y Módulos](#roles-y-módulos)
- [Instalación](#instalación)
- [Estructura de Archivos](#estructura-de-archivos)

## 🎯 Descripción General

Sistema de gestión completo para restaurantes que automatiza:
- Control de acceso por roles (Admin, Maitre, Mesero, Cocinero)
- Gestión de mesas y reservas con validación de horarios
- Catálogo de menú con asignación de especialidades
- Gestión de pedidos y seguimiento en cocina
- Reportería y analytics de ventas
- Control de usuarios y permisos (RBAC)

## 🏗️ Arquitectura Técnica

### Stack Actual (Producción)

- Backend: PHP 8.x
- Frontend: HTML5 + CSS3 + JavaScript Vanilla
- Base de Datos: PostgreSQL 14+
- Servidor: Apache/Nginx o PHP embebido
- Puerto BD: 5434
- Base de datos: restaurante

### Características

- Sin dependencias externas (facilita despliegue)
- Consultas preparadas (previene SQL injection)
- Transacciones ACID para operaciones críticas
- Constraints en BD para validación

## 📊 Base de Datos

### Conexión

Host: localhost
Puerto: 5434
Base de Datos: restaurante
Usuario: postgres
Contraseña: SPMBarcelona11*

### Esquema (11 Tablas)

**Gestión de Usuarios y Roles**
- usuarios: id, nombre, clave (SHA-256), fecha_clave
- roles: id, nombre
- actuaciones: id, rol_id, usuario_id (RBAC)

**Gestión de Mesas**
- mesas: id, sillas
- reservaciones: id, cliente_id, cantidad, estado
- horarios: id, mesa_id, reservacion_id, inicio, duracion

**Gestión de Menú**
- tipos: id, nombre
- platos: id, tipo_id, nombre, descripcion, tiempo, precio
- especialidades: id, cocinero_id, plato_id

**Gestión de Pedidos**
- pedidos: id, cliente_id, mesero_id
- ordenes: id, plato_id, pedido_id, estado, cantidad
- preparaciones: id, cocinero_id, orden_id

### Estados

Reservas: 0 (Cancelada) → 1 (Reservada) → 2 (Ocupada) → 3 (Liberada)
Órdenes: 1 (Solicitado) → 2 (Preparado) → 3 (Entregado)

### Datos de Ejemplo

- Mesas: 11
- Platos: 30
- Usuarios: 52
- Pedidos: 31

## 👥 Roles y Módulos

### 1. Administrador
Control total del sistema

Funcionalidades:
- Gestionar Usuarios (CRUD)
- Gestionar Mesas (CRUD)
- Gestionar Menú (CRUD platos)
- Especialidades (asignar cocineros)
- Reportería (ventas, plato estrella, mesas activas)
- Ver todas las reservas

Archivos:
- admin.html / admin.php
- gestionar_usuarios.php
- gestion_mesas.php
- gestionar_menu.php
- gestionar_especialidades.php
- reportes.php

### 2. Maitre
Gestión de mesas y reservas

Funcionalidades:
- Crear Reserva
- Editar Reserva
- Sentar Cliente (estado 1→2)
- Liberar Mesa (estado 2→3)
- Cancelar Reserva
- Ver Estado Mesas

Validaciones:
- Capacidad de mesa vs. cantidad
- Sin solapamiento de horarios
- Máximo 1h 59min por reserva
- Validación de cupo total

Archivos:
- gestionar_reservas.php
- maitre_view.php

### 3. Mesero
Gestión de pedidos

Funcionalidades:
- Realizar Pedido
- Ver Pedidos Asignados
- Ver Estado Cocina
- Entregar Pedido

Flujo:
1. Seleccionar mesa
2. Agregar platos
3. Registrar (INSERT pedidos + ordenes)
4. Monitorear estado
5. Entregar

Archivos:
- gestion_mesero.php
- mesero_view.php

### 4. Cocinero
Preparación de platos

Funcionalidades:
- Ver Órdenes Asignadas
- Marcar Como Listo
- Historial

Seguridad:
- Solo ve platos en especialidades

Archivos:
- gestion_cocina.php
- cocina_view.php

## 🚀 Instalación

### Prerequisitos

- PHP 8.0+ con extensión PostgreSQL
- PostgreSQL 14+ en localhost:5434
- Servidor web (Apache, Nginx) o PHP embebido

### Pasos

1. Clonar repositorio
   git clone https://github.com/SPMBC11/Proyecto_BDNext.js.git

2. Crear base de datos
   psql -U postgres -p 5434 -c "CREATE DATABASE restaurante;"

3. Restaurar schema
   psql -U postgres -p 5434 -d restaurante < proyecto/respaldo.sql

4. Configurar credenciales en proyecto/etc/parametros.php

5. Iniciar servidor
   cd proyecto
   php -S localhost:8000

6. Acceder
   http://localhost:8000/pagina_principal.php

## 🔑 Usuarios de Prueba

| Usuario | Rol | Contraseña |
|---|---|---|
| admin | Administrador | P123 |
| maitre | Maitre | P123 |
| mesero | Mesero | P123 |
| cocina | Cocinero | P123 |

## 📁 Estructura de Archivos

proyecto/
├── pagina_principal.php              # Login (entrada)
├── admin.html / admin.php            # Panel admin
│
├── lib/
│   ├── libreria.php                  # Funciones base
│   ├── usuarios.php                  # Gestión usuarios
│   └── restaurante.php               # Funciones negocio
│
├── etc/
│   └── parametros.php                # Credenciales BD
│
├── css/
│   └── estilo.css                    # Estilos
│
├── ADMINISTRADOR/
│   ├── gestionar_usuarios.php
│   ├── gestion_mesas.php
│   ├── gestionar_menu.php
│   ├── gestionar_especialidades.php
│   └── reportes.php
│
├── MAITRE/
│   ├── gestionar_reservas.php
│   └── maitre_view.php
│
├── MESERO/
│   ├── gestion_mesero.php
│   └── mesero_view.php
│
├── COCINERO/
│   ├── gestion_cocina.php
│   └── cocina_view.php
│
└── restaurante-next/                 # Next.js (en desarrollo)

## 🔐 Seguridad

| Mecanismo | Implementación |
|---|---|
| Hash de Contraseña | SHA-256 |
| SQL Injection | Consultas preparadas |
| Control de Acceso | Verificación de rol |
| Transacciones | ACID en operaciones críticas |
| Constraints BD | CHECK, UNIQUE, NOT NULL, FOREIGN KEY |

## 📈 Reportería

| Reporte | Métrica |
|---|---|
| Ventas del Día | SUM(precio * cantidad) estado=3 |
| Plato Más Pedido | TOP 1 por cantidad |
| Mesas Activas | Ocupadas actualmente |
| Rendimiento Meseros | Pedidos por mesero |

## 🚧 Stack Futuro (Next.js)

Ubicación: restaurante-next/

Tecnologías:
- Next.js 15.0.0
- React 19.0.0
- TypeScript 5.2.2
- Tailwind CSS 3.3.3
- PostgreSQL Driver (pg)
- Iron-session

Mejoras:
- API RESTful completa
- Type safety
- UI moderna
- SSR
- Mejor rendimiento

## ⚠️ Limitaciones Actuales

| Limitación | Solución Futura |
|---|---|
| Sin Framework | Migrar a Next.js |
| Sin API RESTful | Endpoints REST |
| Reportería básica | Charting library |
| Sin testing | Jest + React Testing Library |
| Código procedural | Refactorizar |

## 📝 Histórico

### v1.0 (Mayo 2026)
- Sistema base funcional
- Autenticación RBAC
- Gestión de reservas y pedidos
- Reportería básica
- Documentación técnica

### v1.1 (Próximo)
- Migración a Next.js
- API RESTful
- UI moderna

## 📞 Recursos

- documento_tecnico.md - Documentación técnica
- respaldo.sql - Schema y datos
- 2025.1-ProyectoRestaurante.pdf - Especificaciones

---

Última actualización: Mayo 3, 2026
Repositorio: https://github.com/SPMBC11/Proyecto_BDNext.js.git
Licencia: Proyecto Educativo
