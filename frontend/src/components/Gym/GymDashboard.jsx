import { useState, useEffect, useRef } from 'react';
import { getGymStats, getMiembros } from '../../services/api';
import { PieChart, Pie, Cell, ResponsiveContainer, Tooltip } from 'recharts';
import gsap from 'gsap';
import { useGSAP } from '@gsap/react';

gsap.registerPlugin(useGSAP);

const COLORS = ['#ef4444', '#f59e0b', '#06b6d4', '#10b981', '#6366f1', '#8b5cf6'];

export default function GymDashboard() {
  const container = useRef();
  const [stats, setStats] = useState(null);
  const [miembros, setMiembros] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([getGymStats(), getMiembros()])
      .then(([statsRes, miembrosRes]) => {
        if (statsRes.success) setStats(statsRes.data);
        if (miembrosRes.success) setMiembros(miembrosRes.data);
      })
      .catch(() => {
        setStats({ miembros_activos: 0, membresias_vencidas: 0, nuevos_este_mes: 0, ingresos_mes: 0, plan_anual: 0, plan_mensual: 0 });
        setMiembros([]);
      })
      .finally(() => setLoading(false));
  }, []);

  useGSAP(() => {
    if (loading) return;
    const tl = gsap.timeline({ defaults: { ease: 'power3.out' } });
    tl.from('.gym-title', { y: -30, opacity: 0, duration: 0.5 })
      .from('.stat-card', { y: 30, opacity: 0, stagger: 0.08, duration: 0.5 }, '-=0.2')
      .from('.gym-content .card', { y: 20, opacity: 0, stagger: 0.1, duration: 0.6 }, '-=0.2');
  }, { scope: container, dependencies: [loading] });

  if (loading) return <div className="loading-spinner"><div className="spinner"></div></div>;

  const statCards = [
    { icon: '💪', label: 'Membresías Activas', value: stats?.miembros_activos || 0, color: '#10b981', bg: 'rgba(16,185,129,0.12)' },
    { icon: '⚠️', label: 'Membresías Vencidas', value: stats?.membresias_vencidas || 0, color: '#ef4444', bg: 'rgba(239,68,68,0.12)' },
    { icon: '🆕', label: 'Nuevos este Mes', value: stats?.nuevos_este_mes || 0, color: '#6366f1', bg: 'rgba(99,102,241,0.12)' },
    { icon: '💰', label: 'Ingresos Renovaciones', value: `$${(stats?.ingresos_mes || 0).toLocaleString()}`, color: '#f59e0b', bg: 'rgba(245,158,11,0.12)' },
  ];

  const planData = [
    { name: 'Anual', value: stats?.plan_anual || 0 },
    { name: 'Mensual/Otros', value: stats?.plan_mensual || 0 },
  ].filter(d => d.value > 0);

  const renderData = planData.length > 0 ? planData : [{name: 'Sin datos', value: 1}];

  return (
    <div ref={container}>
      <div className="page-header">
        <h1 className="page-title gym-title">Gimnasio (Enterprise)</h1>
        <p className="page-subtitle">Gestión de CRM unificado, Membresías y Control de Acceso</p>
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

      <div className="gym-content" style={{ display: 'grid', gridTemplateColumns: '1fr 320px', gap: 20 }}>
        <div className="card">
          <div className="card-header">
            <h3 className="card-title">Socios y Membresías</h3>
            <span className="badge badge-success">{miembros.length} registros</span>
          </div>
          {miembros.length === 0 ? (
            <div className="card-body" style={{ textAlign: 'center', padding: 48, color: 'var(--color-text-muted)' }}>
              <p style={{ fontSize: '2rem', marginBottom: 8 }}>💪</p>
              <p>Sin datos — Ingresa un registro en la BD para ver la tabla</p>
            </div>
          ) : (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Socio (CRM)</th>
                  <th>Email</th>
                  <th>Plan Actual</th>
                  <th>Días Restantes</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                {miembros.map((m, index) => (
                  <tr key={m.ID_Membresia || index}>
                    <td style={{ color: 'var(--color-text-primary)', fontWeight: 600 }}>{m.Nombres} {m.Apellidos}</td>
                    <td>{m.Email}</td>
                    <td><span className="badge" style={{ background: `rgba(99,102,241,0.1)`, color: '#6366f1' }}>{m.PlanNombre}</span></td>
                    <td style={{ fontWeight: 600 }}>{m.Dias_Restantes > 0 ? `${m.Dias_Restantes} días` : 'Vencida'}</td>
                    <td><span className={`badge ${m.Estado === 'Vigente' ? 'badge-success' : 'badge-danger'}`}>{m.Estado}</span></td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        <div className="card">
          <div className="card-header">
            <h3 className="card-title">Distribución por Plan</h3>
          </div>
          <div className="card-body" style={{ display: 'flex', justifyContent: 'center' }}>
            <ResponsiveContainer width="100%" height={220}>
              <PieChart>
                <Pie data={renderData} cx="50%" cy="50%" innerRadius={50} outerRadius={80} dataKey="value" stroke="none">
                  {renderData.map((entry, i) => (
                    <Cell key={i} fill={COLORS[i % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip contentStyle={{ background: '#1a2332', border: '1px solid #1e293b', borderRadius: 8, color: '#f1f5f9' }} />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>
    </div>
  );
}
