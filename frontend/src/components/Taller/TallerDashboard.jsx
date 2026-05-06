import { useState, useEffect, useRef } from 'react';
import { getTallerStats, getReparaciones } from '../../services/api';
import gsap from 'gsap';
import { useGSAP } from '@gsap/react';

gsap.registerPlugin(useGSAP);

const estadoConfig = {
  'Recepción': { badge: 'badge-info', label: 'Recepción' },
  'Diagnóstico': { badge: 'badge-purple', label: 'Diagnóstico' },
  'En Espera Refacciones': { badge: 'badge-warning', label: 'Espera Refacciones' },
  'En Reparación': { badge: 'badge-warning', label: 'En Reparación' },
  'Control Calidad': { badge: 'badge-primary', label: 'Control Calidad' },
  'Lista Entrega': { badge: 'badge-success', label: 'Lista Entrega' },
  'Entregado': { badge: 'badge-success', label: 'Entregado' },
  'Cancelada': { badge: 'badge-danger', label: 'Cancelada' },
};

const prioridadConfig = {
  'Siniestro Aseguradora': { badge: 'badge-danger' },
  'Alta': { badge: 'badge-warning' },
  'Normal': { badge: 'badge-info' },
  'Baja': { badge: 'badge-success' },
};

export default function TallerDashboard() {
  const container = useRef();
  const [stats, setStats] = useState(null);
  const [reparaciones, setReparaciones] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([getTallerStats(), getReparaciones()])
      .then(([statsRes, repRes]) => {
        if (statsRes.success) setStats(statsRes.data);
        if (repRes.success) setReparaciones(repRes.data);
      })
      .catch(() => {
        setStats({ total_clientes: 0, total_vehiculos: 0, reparaciones_activas: 0, ingresos_completados: 0, urgentes_pendientes: 0 });
        setReparaciones([]);
      })
      .finally(() => setLoading(false));
  }, []);

  useGSAP(() => {
    if (loading) return;
    const tl = gsap.timeline({ defaults: { ease: 'power3.out' } });
    tl.from('.taller-title', { y: -30, opacity: 0, duration: 0.5 })
      .from('.stat-card', { y: 30, opacity: 0, stagger: 0.08, duration: 0.5 }, '-=0.2')
      .from('.taller-table', { y: 20, opacity: 0, duration: 0.6 }, '-=0.2');
  }, { scope: container, dependencies: [loading] });

  if (loading) return <div className="loading-spinner"><div className="spinner"></div></div>;

  const statCards = [
    { icon: '🚗', label: 'Clientes CRM', value: stats?.total_clientes || 0, color: '#6366f1', bg: 'rgba(99,102,241,0.12)' },
    { icon: '🔧', label: 'Vehículos', value: stats?.total_vehiculos || 0, color: '#06b6d4', bg: 'rgba(6,182,212,0.12)' },
    { icon: '⚙️', label: 'OT Activas', value: stats?.reparaciones_activas || 0, color: '#f59e0b', bg: 'rgba(245,158,11,0.12)' },
    { icon: '💰', label: 'Ingresos Previstos', value: `$${(stats?.ingresos_completados || 0).toLocaleString()}`, color: '#10b981', bg: 'rgba(16,185,129,0.12)' },
    { icon: '🚨', label: 'OT Urgentes/Siniestro', value: stats?.urgentes_pendientes || 0, color: '#ef4444', bg: 'rgba(239,68,68,0.12)' },
  ];

  return (
    <div ref={container}>
      <div className="page-header">
        <h1 className="page-title taller-title">Taller Mecánico (Enterprise)</h1>
        <p className="page-subtitle">Gestión de Órdenes de Trabajo (OT) y Expediente Vehicular</p>
      </div>

      <div className="stats-grid">
        {statCards.map((s, i) => (
          <div className="stat-card" key={i}>
            <div className="stat-icon" style={{ background: s.bg, color: s.color }}>{s.icon}</div>
            <div>
              <div className="stat-value">{s.value}</div>
              <div className="stat-label">{s.label}</div>
            </div>
          </div>
        ))}
      </div>

      <div className="card taller-table">
        <div className="card-header">
          <h3 className="card-title">Órdenes de Trabajo Activas</h3>
          <span className="badge badge-primary">{reparaciones.length} registros</span>
        </div>
        {reparaciones.length === 0 ? (
          <div className="card-body" style={{ textAlign: 'center', padding: 48, color: 'var(--color-text-muted)' }}>
            <p style={{ fontSize: '2rem', marginBottom: 8 }}>🔧</p>
            <p>Sin datos — Ingresa un registro en la BD para ver la tabla</p>
          </div>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>OT #</th>
                <th>Vehículo</th>
                <th>Cliente (CRM)</th>
                <th>Síntomas Reportados</th>
                <th>Costo Est.</th>
                <th>Prioridad</th>
                <th>Estado OT</th>
              </tr>
            </thead>
            <tbody>
              {reparaciones.map((r) => (
                <tr key={r.ID_OT}>
                  <td style={{ fontWeight: 800, color: 'var(--color-text-muted)' }}>#{r.ID_OT}</td>
                  <td style={{ color: 'var(--color-text-primary)', fontWeight: 600 }}>{r.Marca} {r.Modelo} <span style={{color:'var(--color-text-muted)'}}>({r.Placa})</span></td>
                  <td>{r.Nombres} {r.Apellidos}</td>
                  <td>{r.Sintomas_Reportados}</td>
                  <td style={{ fontWeight: 600 }}>${r.Costo_Estimado?.toLocaleString()}</td>
                  <td><span className={`badge ${prioridadConfig[r.Prioridad]?.badge || 'badge-info'}`}>{r.Prioridad}</span></td>
                  <td><span className={`badge ${estadoConfig[r.Estado]?.badge || 'badge-info'}`}>{estadoConfig[r.Estado]?.label || r.Estado}</span></td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
