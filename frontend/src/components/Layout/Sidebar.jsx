import { NavLink, useLocation } from 'react-router-dom';
import { Wrench, Dumbbell, GitBranch } from 'lucide-react';

export default function Sidebar() {
  const location = useLocation();

  const modules = [
    {
      title: 'Taller Mecánico',
      links: [
        { to: '/taller', label: 'Dashboard', icon: Wrench },
      ]
    },
    {
      title: 'Gimnasio',
      links: [
        { to: '/gym', label: 'Dashboard', icon: Dumbbell },
      ]
    },
    {
      title: 'Estructuras de Datos',
      links: [
        { to: '/dsa', label: 'BST Visualizer', icon: GitBranch },
      ]
    }
  ];

  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <h1>Enterprise Showcase</h1>
        <span>Java → Web Platform</span>
      </div>

      {modules.map((mod) => (
        <div className="sidebar-section" key={mod.title}>
          <div className="sidebar-section-title">{mod.title}</div>
          {mod.links.map((link) => (
            <NavLink
              key={link.to}
              to={link.to}
              className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}
            >
              <link.icon className="link-icon" size={18} />
              {link.label}
            </NavLink>
          ))}
        </div>
      ))}
    </aside>
  );
}
