"use client"

import type React from "react"
import { useState, useEffect } from "react"
import { Link } from "react-router-dom"
import { toast } from "react-toastify"
import { getAttendance, deleteAttendance } from "../../services/attendanceService"
import type { Attendance } from "../../types"
import "./AttendanceList.css"

const AttendanceList: React.FC = () => {
  const [attendanceRecords, setAttendanceRecords] = useState<Attendance[]>([])
  const [loading, setLoading] = useState<boolean>(true)
  const [error, setError] = useState<string | null>(null)
  const [searchTerm, setSearchTerm] = useState<string>("")
  const [filterType, setFilterType] = useState<"ALL" | "STUDENT" | "TEACHER">("ALL")
  const [filterDate, setFilterDate] = useState<string>("")

  useEffect(() => {
    fetchAttendance()
  }, [])

  const fetchAttendance = async () => {
    try {
      setLoading(true)
      const data = await getAttendance()
      setAttendanceRecords(data)
      setLoading(false)
    } catch (err) {
      setError("Error al cargar los registros de asistencia")
      setLoading(false)
      console.error("Error fetching attendance:", err)
    }
  }

  const handleDelete = async (id: number) => {
    if (window.confirm("¿Estás seguro de que deseas eliminar este registro de asistencia?")) {
      try {
        await deleteAttendance(id)
        setAttendanceRecords(attendanceRecords.filter((record) => record.id !== id))
        toast.success("Registro de asistencia eliminado correctamente")
      } catch (err) {
        toast.error("Error al eliminar el registro de asistencia")
        console.error("Error deleting attendance:", err)
      }
    }
  }

  const filteredAttendance = attendanceRecords.filter((record) => {
    // Filter by type
    if (filterType !== "ALL" && record.type !== filterType) {
      return false
    }

    // Filter by date
    if (filterDate && record.date !== filterDate) {
      return false
    }

    // Filter by search term (name or notes)
    if (searchTerm) {
      const studentName = record.student
        ? `${(record.student as any).firstName || ""} ${(record.student as any).lastName || ""}`.toLowerCase()
        : ""
      const teacherName = record.teacher
        ? `${(record.teacher as any).firstName || ""} ${(record.teacher as any).lastName || ""}`.toLowerCase()
        : ""
      const notes = record.notes?.toLowerCase() || ""

      return (
        studentName.includes(searchTerm.toLowerCase()) ||
        teacherName.includes(searchTerm.toLowerCase()) ||
        notes.includes(searchTerm.toLowerCase())
      )
    }

    return true
  })

  if (loading) {
    return <div className="loading">Cargando registros de asistencia...</div>
  }

  if (error) {
    return <div className="error">{error}</div>
  }

  return (
    <div className="attendance-list">
      <div className="list-header">
        <h1>Registros de Asistencia</h1>
        <Link to="/attendance/new" className="btn btn-primary">
          <i className="fas fa-plus"></i> Nuevo Registro
        </Link>
      </div>

      <div className="filters">
        <div className="search-container">
          <input
            type="text"
            placeholder="Buscar por nombre o notas..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
          />
        </div>

        <div className="filter-options">
          <div className="filter-group">
            <label>Tipo:</label>
            <select
              value={filterType}
              onChange={(e) => setFilterType(e.target.value as "ALL" | "STUDENT" | "TEACHER")}
              className="filter-select"
            >
              <option value="ALL">Todos</option>
              <option value="STUDENT">Estudiantes</option>
              <option value="TEACHER">Profesores</option>
            </select>
          </div>

          <div className="filter-group">
            <label>Fecha:</label>
            <input
              type="date"
              value={filterDate}
              onChange={(e) => setFilterDate(e.target.value)}
              className="filter-date"
            />
            {filterDate && (
              <button className="clear-filter" onClick={() => setFilterDate("")} title="Limpiar filtro de fecha">
                <i className="fas fa-times"></i>
              </button>
            )}
          </div>
        </div>
      </div>

      {filteredAttendance.length === 0 ? (
        <div className="no-data">
          {searchTerm || filterType !== "ALL" || filterDate
            ? "No se encontraron registros con los filtros aplicados"
            : "No hay registros de asistencia"}
        </div>
      ) : (
        <div className="table-responsive">
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Tipo</th>
                <th>Nombre</th>
                <th>Fecha</th>
                <th>Hora Entrada</th>
                <th>Hora Salida</th>
                <th>Presente</th>
                <th>Notas</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {filteredAttendance.map((record) => {
                const name =
                  record.type === "STUDENT"
                    ? record.student && (record.student as any).firstName
                      ? `${(record.student as any).firstName} ${(record.student as any).lastName}`
                      : "Estudiante"
                    : record.teacher && (record.teacher as any).firstName
                      ? `${(record.teacher as any).firstName} ${(record.teacher as any).lastName}`
                      : "Profesor"

                return (
                  <tr key={record.id}>
                    <td>{record.id}</td>
                    <td>{record.type === "STUDENT" ? "Estudiante" : "Profesor"}</td>
                    <td>{name}</td>
                    <td>{new Date(record.date).toLocaleDateString()}</td>
                    <td>{record.timeIn}</td>
                    <td>{record.timeOut || "-"}</td>
                    <td>
                      <span className={`badge ${record.present ? "badge-success" : "badge-danger"}`}>
                        {record.present ? "Sí" : "No"}
                      </span>
                    </td>
                    <td>{record.notes || "-"}</td>
                    <td className="actions">
                      <Link to={`/attendance/edit/${record.id}`} className="btn-action edit">
                        <i className="fas fa-edit"></i>
                      </Link>
                      <button className="btn-action delete" onClick={() => record.id && handleDelete(record.id)}>
                        <i className="fas fa-trash"></i>
                      </button>
                    </td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}

export default AttendanceList

