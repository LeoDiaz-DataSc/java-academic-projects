const express = require('express');
const router = express.Router();
const db = require('../config/database');
const jwt = require('jsonwebtoken');

// POST /api/auth/login
router.post('/login', async (req, res) => {
    try {
        const { username, contrasena } = req.body;

        if (!username || !contrasena) {
            return res.status(400).json({ success: false, message: 'Username y contraseña son requeridos' });
        }

        const [users] = await db.query(
            `SELECT u.*, r.Nombre AS RolNombre, e.Nombres, e.Apellidos 
             FROM Usuarios u
             INNER JOIN Roles r ON u.ID_Rol = r.ID_Rol
             LEFT JOIN Empleados e ON u.ID_Empleado = e.ID_Empleado
             WHERE u.Username = ? AND u.Contrasena_Hash = SHA2(?, 256) AND u.Activo = TRUE`,
            [username, contrasena]
        );

        if (users.length === 0) {
            // ISO 27001 - Log failed attempt
            await db.query("INSERT INTO Audit_Logs (Modulo, Accion, Detalle, Direccion_IP) VALUES ('IAM', 'LOGIN_FALLIDO', ?, ?)",
                [JSON.stringify({ username }), req.ip]);
            return res.status(401).json({ success: false, message: 'Credenciales incorrectas' });
        }

        const user = users[0];
        const token = jwt.sign(
            { id: user.ID_Usuario, username: user.Username, rol: user.RolNombre, nombre: user.Nombres ? `${user.Nombres} ${user.Apellidos}` : 'Admin' },
            process.env.JWT_SECRET,
            { expiresIn: process.env.JWT_EXPIRES_IN || '8h' }
        );

        // ISO 27001 - Log success
        await db.query("INSERT INTO Audit_Logs (ID_Usuario, Modulo, Accion, Detalle, Direccion_IP) VALUES (?, 'IAM', 'LOGIN_EXITOSO', ?, ?)",
            [user.ID_Usuario, JSON.stringify({ rol: user.RolNombre }), req.ip]);

        res.json({
            success: true,
            token,
            user: { id: user.ID_Usuario, nombre: user.Nombres ? `${user.Nombres} ${user.Apellidos}` : 'Admin', username: user.Username, rol: user.RolNombre }
        });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

module.exports = router;
