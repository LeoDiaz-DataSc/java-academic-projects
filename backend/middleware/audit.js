const db = require('../config/database');

/**
 * Audit Log Middleware — ISO 27001 Trazabilidad Avanzada
 * Registra en Audit_Logs de la Capa 4 ISO
 */
module.exports = (modulo_accion) => {
    return async (req, res, next) => {
        res.on('finish', async () => {
            try {
                const userId = req.user?.id || null;
                
                // Parseamos por ejemplo 'TALLER_CREATE_OT' en modulo 'TALLER' y accion 'CREATE_OT'
                let modulo = 'CORE';
                let accion = modulo_accion;
                const splitIndex = modulo_accion.indexOf('_');
                if (splitIndex > 0) {
                    modulo = modulo_accion.substring(0, splitIndex);
                    accion = modulo_accion.substring(splitIndex + 1);
                }

                const detalle = {
                    method: req.method,
                    url: req.originalUrl,
                    status: res.statusCode,
                    body: (req.method === 'POST' || req.method === 'PUT') ? req.body : null
                };
                
                const ip = req.ip || req.connection?.remoteAddress || 'unknown';
                const userAgent = req.get('user-agent');

                await db.query(
                    'INSERT INTO Audit_Logs (ID_Usuario, Modulo, Accion, Detalle, Direccion_IP, User_Agent) VALUES (?, ?, ?, ?, ?, ?)',
                    [userId, modulo, accion, JSON.stringify(detalle), ip, userAgent]
                );
            } catch (error) {
                console.error('Audit log error:', error.message);
            }
        });
        next();
    };
};
