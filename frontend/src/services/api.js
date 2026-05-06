import axios from 'axios';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL || 'http://localhost:3000/api',
    timeout: 10000,
});

// JWT interceptor
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('showcase_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// =============================================
// AUTH
// =============================================
export const login = async (email, contrasena) => {
    const res = await api.post('/auth/login', { email, contrasena });
    if (res.data.success) localStorage.setItem('showcase_token', res.data.token);
    return res.data;
};

export const logout = () => localStorage.removeItem('showcase_token');

// =============================================
// TALLER
// =============================================
export const getClientes = () => api.get('/taller/clientes').then(r => r.data);
export const createCliente = (data) => api.post('/taller/clientes', data).then(r => r.data);
export const getVehiculos = () => api.get('/taller/vehiculos').then(r => r.data);
export const createVehiculo = (data) => api.post('/taller/vehiculos', data).then(r => r.data);
export const getReparaciones = () => api.get('/taller/reparaciones').then(r => r.data);
export const createReparacion = (data) => api.post('/taller/reparaciones', data).then(r => r.data);
export const updateEstadoReparacion = (id, estado) => api.put(`/taller/reparaciones/${id}/estado`, { estado }).then(r => r.data);
export const getTallerStats = () => api.get('/taller/stats').then(r => r.data);

// =============================================
// GYM
// =============================================
export const getMiembros = () => api.get('/gym/miembros').then(r => r.data);
export const createMiembro = (data) => api.post('/gym/miembros', data).then(r => r.data);
export const toggleMiembro = (id) => api.put(`/gym/miembros/${id}/toggle`).then(r => r.data);
export const getPagos = () => api.get('/gym/pagos').then(r => r.data);
export const createPago = (data) => api.post('/gym/pagos', data).then(r => r.data);
export const getGymStats = () => api.get('/gym/stats').then(r => r.data);

export default api;
