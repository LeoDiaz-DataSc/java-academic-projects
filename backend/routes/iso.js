const express = require('express');
const router = express.Router();
const db = require('../config/database');
const audit = require('../middleware/audit');

// =============================================
// ISO 9001: CALIDAD (No Conformidades)
// =============================================
router.get('/9001/no-conformidades', audit('ISO_LIST_NC'), async (req, res) => {
    try {
        const [rows] = await db.query(`
            SELECT nc.*, s.Nombre AS Sucursal, u.Username AS Reportador 
            FROM ISO_No_Conformidades nc
            INNER JOIN Sucursales s ON nc.ID_Sucursal = s.ID_Sucursal
            INNER JOIN Usuarios u ON nc.Reportado_Por = u.ID_Usuario
            ORDER BY nc.Fecha_Apertura DESC
        `);
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

router.post('/9001/no-conformidades', audit('ISO_CREATE_NC'), async (req, res) => {
    try {
        const { id_sucursal, id_usuario, origen, descripcion } = req.body;
        const [result] = await db.query(
            'INSERT INTO ISO_No_Conformidades (ID_Sucursal, Reportado_Por, Origen, Descripcion) VALUES (?, ?, ?, ?)',
            [id_sucursal, id_usuario, origen, descripcion]
        );
        res.status(201).json({ success: true, id: result.insertId });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// =============================================
// ISO 45001: SEGURIDAD (Incidentes)
// =============================================
router.get('/45001/incidentes', audit('ISO_LIST_INCIDENTES'), async (req, res) => {
    try {
        const [rows] = await db.query(`
            SELECT i.*, s.Nombre AS Sucursal, e.Nombres, e.Apellidos 
            FROM ISO_Incidentes i
            INNER JOIN Sucursales s ON i.ID_Sucursal = s.ID_Sucursal
            LEFT JOIN Empleados e ON i.ID_Empleado_Afectado = e.ID_Empleado
            ORDER BY i.Fecha_Incidente DESC
        `);
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

router.post('/45001/incidentes', audit('ISO_CREATE_INCIDENTE'), async (req, res) => {
    try {
        const { id_sucursal, id_empleado, nivel_gravedad, descripcion, lugar, incapacidad, dias_incapacidad, fecha } = req.body;
        const [result] = await db.query(
            'INSERT INTO ISO_Incidentes (ID_Sucursal, ID_Empleado_Afectado, Nivel_Gravedad, Descripcion, Lugar_Especifico, Requiere_Incapacidad, Dias_Incapacidad, Fecha_Incidente) VALUES (?, ?, ?, ?, ?, ?, ?, ?)',
            [id_sucursal, id_empleado, nivel_gravedad, descripcion, lugar, incapacidad || false, dias_incapacidad || 0, fecha]
        );
        res.status(201).json({ success: true, id: result.insertId });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// =============================================
// ISO 14001: MEDIO AMBIENTE (Residuos)
// =============================================
router.get('/14001/residuos', audit('ISO_LIST_RESIDUOS'), async (req, res) => {
    try {
        const [rows] = await db.query(`
            SELECT r.*, s.Nombre AS Sucursal 
            FROM ISO_Residuos r
            INNER JOIN Sucursales s ON r.ID_Sucursal = s.ID_Sucursal
            ORDER BY r.Fecha_Recoleccion DESC
        `);
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

router.post('/14001/residuos', audit('ISO_CREATE_RESIDUO'), async (req, res) => {
    try {
        const { id_sucursal, tipo, cantidad, unidad, manifiesto, fecha } = req.body;
        const [result] = await db.query(
            'INSERT INTO ISO_Residuos (ID_Sucursal, Tipo_Residuo, Cantidad, Unidad_Medida, Manifiesto_Entrega, Fecha_Recoleccion) VALUES (?, ?, ?, ?, ?, ?)',
            [id_sucursal, tipo, cantidad, unidad, manifiesto, fecha]
        );
        res.status(201).json({ success: true, id: result.insertId });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// =============================================
// ISO 27001: SEGURIDAD DE LA INFORMACIÓN (Auditoría)
// =============================================
router.get('/27001/audit-logs', audit('ISO_LIST_AUDIT'), async (req, res) => {
    try {
        const [rows] = await db.query(`
            SELECT a.*, u.Username
            FROM Audit_Logs a
            LEFT JOIN Usuarios u ON a.ID_Usuario = u.ID_Usuario
            ORDER BY a.Fecha_Hora DESC LIMIT 100
        `);
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

module.exports = router;
