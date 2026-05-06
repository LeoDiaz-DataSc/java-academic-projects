const express = require('express');
const router = express.Router();
const db = require('../config/database');
const audit = require('../middleware/audit');

// =============================================
// CRM / CLIENTES (Capa 1)
// =============================================
router.get('/clientes', audit('CRM_LIST_CLIENTES'), async (req, res) => {
    try {
        const [rows] = await db.query('SELECT * FROM Personas ORDER BY Creado_En DESC');
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// =============================================
// TALLER: VEHÍCULOS (Capa 2)
// =============================================
router.get('/vehiculos', audit('TALLER_LIST_VEHICULOS'), async (req, res) => {
    try {
        const [rows] = await db.query(`
            SELECT v.*, p.Nombres, p.Apellidos 
            FROM Vehiculos v
            INNER JOIN Personas p ON v.ID_Persona = p.ID_Persona
        `);
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// =============================================
// TALLER: ÓRDENES DE TRABAJO
// =============================================
router.get('/reparaciones', audit('TALLER_LIST_OT'), async (req, res) => {
    try {
        const [rows] = await db.query(`
            SELECT ot.*, v.Marca, v.Modelo, v.Placa, p.Nombres, p.Apellidos
            FROM Ordenes_Trabajo ot
            INNER JOIN Vehiculos v ON ot.ID_Vehiculo = v.ID_Vehiculo
            INNER JOIN Personas p ON v.ID_Persona = p.ID_Persona
        `);
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

router.put('/reparaciones/:id/estado', audit('TALLER_UPDATE_ESTADO'), async (req, res) => {
    try {
        const { estado } = req.body;
        await db.query('UPDATE Ordenes_Trabajo SET Estado = ? WHERE ID_OT = ?', [estado, req.params.id]);
        res.json({ success: true, message: `Estado actualizado a: ${estado}` });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// =============================================
// TALLER: BAHÍAS E INVENTARIO
// =============================================
router.get('/bahias', audit('TALLER_LIST_BAHIAS'), async (req, res) => {
    try {
        const [rows] = await db.query('SELECT * FROM Taller_Bahias');
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

router.get('/inventario', audit('TALLER_LIST_INVENTARIO'), async (req, res) => {
    try {
        const [rows] = await db.query('SELECT * FROM Inventario_Refacciones');
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// =============================================
// KPI STATS
// =============================================
router.get('/stats', async (req, res) => {
    try {
        const [[stats]] = await db.query(`
            SELECT 
                (SELECT COUNT(*) FROM Personas) AS total_clientes,
                (SELECT COUNT(*) FROM Vehiculos) AS total_vehiculos,
                (SELECT COUNT(*) FROM Ordenes_Trabajo WHERE Estado NOT IN ('Entregado', 'Cancelada')) AS reparaciones_activas,
                (SELECT COALESCE(SUM(Costo_Estimado), 0) FROM Ordenes_Trabajo WHERE Estado = 'Entregado') AS ingresos_completados,
                (SELECT COUNT(*) FROM Ordenes_Trabajo WHERE Prioridad IN ('Alta', 'Siniestro Aseguradora') AND Estado NOT IN ('Entregado')) AS urgentes_pendientes
        `);
        res.json({ success: true, data: stats });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

module.exports = router;
