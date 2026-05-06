const express = require('express');
const router = express.Router();
const db = require('../config/database');
const audit = require('../middleware/audit');

// =============================================
// GYM: MEMBRESÍAS SOCIOS (Capa 3)
// =============================================
router.get('/miembros', audit('GYM_LIST_MIEMBROS'), async (req, res) => {
    try {
        const [rows] = await db.query(`
            SELECT sm.*, p.Nombres, p.Apellidos, p.Email, cp.Nombre AS PlanNombre,
                   DATEDIFF(sm.Fecha_Fin, CURDATE()) AS Dias_Restantes
            FROM Socio_Membresias sm
            INNER JOIN Personas p ON sm.ID_Persona = p.ID_Persona
            INNER JOIN Cat_Planes_Membresia cp ON sm.ID_Plan = cp.ID_Plan
        `);
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// =============================================
// GYM: CLASES Y RESERVAS
// =============================================
router.get('/clases', audit('GYM_LIST_CLASES'), async (req, res) => {
    try {
        const [rows] = await db.query('SELECT * FROM Clases_Grupales');
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// =============================================
// GYM: TIENDA
// =============================================
router.get('/tienda/productos', audit('GYM_LIST_TIENDA'), async (req, res) => {
    try {
        const [rows] = await db.query('SELECT * FROM Gym_Productos_Tienda');
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// =============================================
// GYM: RUTINAS
// =============================================
router.get('/rutinas', audit('GYM_LIST_RUTINAS'), async (req, res) => {
    try {
        const [rows] = await db.query('SELECT * FROM Gym_Rutinas_Asignadas');
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// =============================================
// GYM: ESTADÍSTICAS
// =============================================
router.get('/stats', async (req, res) => {
    try {
        const [[stats]] = await db.query(`
            SELECT 
                (SELECT COUNT(*) FROM Socio_Membresias WHERE Estado = 'Vigente') AS miembros_activos,
                (SELECT COUNT(*) FROM Socio_Membresias WHERE Estado IN ('Por Vencer', 'Vencida')) AS membresias_vencidas,
                (SELECT COUNT(*) FROM Socio_Membresias WHERE MONTH(Fecha_Inicio) = MONTH(CURDATE()) AND YEAR(Fecha_Inicio) = YEAR(CURDATE())) AS nuevos_este_mes,
                (SELECT COALESCE(SUM(cp.Precio), 0) FROM Socio_Membresias sm JOIN Cat_Planes_Membresia cp ON sm.ID_Plan = cp.ID_Plan) AS ingresos_mes,
                (SELECT COUNT(*) FROM Socio_Membresias sm JOIN Cat_Planes_Membresia cp ON sm.ID_Plan = cp.ID_Plan WHERE cp.Nombre LIKE '%Anual%' AND sm.Estado = 'Vigente') AS plan_anual,
                (SELECT COUNT(*) FROM Socio_Membresias sm JOIN Cat_Planes_Membresia cp ON sm.ID_Plan = cp.ID_Plan WHERE cp.Nombre NOT LIKE '%Anual%' AND sm.Estado = 'Vigente') AS plan_mensual
        `);
        res.json({ success: true, data: stats });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

module.exports = router;
