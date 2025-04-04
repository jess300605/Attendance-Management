import type React from "react"
import { NavLink } from "react-router-dom"
import "./Sidebar.css"

const Sidebar: React.FC = () => {
  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <h1>Asistencia</h1>
      </div>
      <nav className="sidebar-nav">
        <ul>
          <li>
            <NavLink to="/" end>
              <i className="fas fa-home"></i>
              <span>Dashboard</span>
            </NavLink>
          </li>
          <li>
            <NavLink to="/students">
              <i className="fas fa-user-graduate"></i>
              <span>Estudiantes</span>
            </NavLink>
          </li>
          <li>
            <NavLink to="/teachers">
              <i className="fas fa-chalkboard-teacher"></i>
              <span>Profesores</span>
            </NavLink>
          </li>
          <li>
            <NavLink to="/attendance">
              <i className="fas fa-clipboard-check"></i>
              <span>Asistencia</span>
            </NavLink>
          </li>
          <li>
            <NavLink to="/grades">
              <i className="fas fa-graduation-cap"></i>
              <span>Calificaciones</span>
            </NavLink>
          </li>
          <li>
            <NavLink to="/reports">
              <i className="fas fa-chart-bar"></i>
              <span>Reportes</span>
            </NavLink>
          </li>
        </ul>
      </nav>
      <div className="sidebar-footer">
        <p>© 2023 Sistema de Asistencia</p>
      </div>
    </aside>
  )
}

export default Sidebar

