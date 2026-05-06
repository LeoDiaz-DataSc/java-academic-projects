require('dotenv').config();
const express = require('express');
const cors = require('cors');

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware global
app.use(cors());
app.use(express.json());

// Rutas
app.use('/api/auth', require('./routes/auth'));
app.use('/api/taller', require('./routes/taller'));
app.use('/api/gym', require('./routes/gym'));
app.use('/api/iso', require('./routes/iso'));

// Health check
app.get('/api/health', (req, res) => {
    res.json({ status: 'ok', service: 'java-enterprise-showcase', timestamp: new Date() });
});

// Error handler global
app.use((err, req, res, next) => {
    console.error('Server error:', err.message);
    res.status(500).json({ success: false, message: 'Error interno del servidor' });
});

app.listen(PORT, () => {
    console.log(`🚀 Java Enterprise Showcase API running on port ${PORT}`);
    console.log(`📋 Modules: Taller Mecánico | Gimnasio | DSA`);
});
