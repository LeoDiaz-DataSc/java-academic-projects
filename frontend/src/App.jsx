import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Sidebar from './components/Layout/Sidebar';
import TallerDashboard from './components/Taller/TallerDashboard';
import GymDashboard from './components/Gym/GymDashboard';
import BSTVisualizer from './components/DSA/BSTVisualizer';

export default function App() {
  return (
    <BrowserRouter>
      <div className="app-layout">
        <Sidebar />
        <main className="app-main">
          <div className="page-container">
            <Routes>
              <Route path="/" element={<TallerDashboard />} />
              <Route path="/taller" element={<TallerDashboard />} />
              <Route path="/gym" element={<GymDashboard />} />
              <Route path="/dsa" element={<BSTVisualizer />} />
            </Routes>
          </div>
        </main>
      </div>
    </BrowserRouter>
  );
}
