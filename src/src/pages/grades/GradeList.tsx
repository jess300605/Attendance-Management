"use client"

import type React from "react"
import { useState, useEffect } from "react"
import { Link } from "react-router-dom"
import { toast } from "react-toastify"
import { getGrades, deleteGrade } from "../../services/gradeService"
import type { Grade } from "../../types"
import "./GradeList.css"

const GradeList: React.FC = () => {
  const [grades, setGrades] = useState<Grade[]>([])
  const [loading, setLoading] = useState<boolean>(true)
  const [error, setError] = useState<string | null>(null)
  const [searchTerm, setSearchTerm] = useState<string>("")
  const [filterSubject, setFilterSubject] = useState<string>("")
  const [subjects, setSubjects] = useState<string[]>([])

  useEffect(() => {
    fetchGrades()
  }, [])

  const fetchGrades = async () => {
    try {
      setLoading(true)
      const data = await getGrades()
      setGrades(data)

      // Extract unique subjects for filter
      const uniqueSubjects = Array.from(new Set(data.map((grade) => grade.subject)))
      setSubjects(uniqueSubjects)

      setLoading(false)
    } catch (err) {
      setError("Error al cargar las calificaciones")
      setLoading(false)
      console.error("Error fetching grades:", err)
    }
  }

  const handleDelete = async (id: number) => {
    if (window.confirm("¿Estás seguro de que deseas eliminar esta calificación?")) {
      try {
        await deleteGrade(id)
        setGrades(grades.filter((grade) => grade.id !== id))
        toast.success("Calificación eliminada correctamente")
      } catch (err) {
        toast.error("Error al eliminar la calificación")
        console.error("Error deleting grade:", err)
      }
    }
  }

  const filteredGrades = grades.filter((grade) => {
    // Filter by subject
    if (filterSubject && grade.subject !== filterSubject) {
      return false
    }

    // Filter by search term (student name or comments)
    if (searchTerm) {
      const studentName = (grade.student as any).firstName
        ? `${(grade.student as any).firstName} ${(grade.student as any).lastName}`.toLowerCase()
        : ""
      const comments = grade.comments?.toLowerCase() || ""

      return studentName.includes(searchTerm.toLowerCase()) || comments.includes(searchTerm.toLowerCase())
    }

    return true
  })

  if (loading) {
    return <div className="loading">Cargando calificaciones...</div>
  }

  if (error) {
    return <div className="error">{error}</div>
  }

  return (
    <div className="grade-list">
      <div className="list-header">
        <h1>Calificaciones</h1>
        <Link to="/grades/new" className="btn btn-primary">
          <i className="fas fa-plus"></i> Nueva Calificación
        </Link>
      </div>

      <div className="filters">
        <div className="search-container">
          <input
            type="text"
            placeholder="Buscar por estudiante o comentarios..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
          />
        </div>

        <div className="filter-options">
          <div className="filter-group">
            <label>Materia:</label>
            <select value={filterSubject} onChange={(e) => setFilterSubject(e.target.value)} className="filter-select">
              <option value="">Todas</option>
              {subjects.map((subject) => (
                <option key={subject} value={subject}>
                  {subject}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {filteredGrades.length === 0 ? (
        <div className="no-data">
          {searchTerm || filterSubject
            ? "No se encontraron calificaciones con los filtros aplicados"
            : "No hay calificaciones registradas"}
        </div>
      ) : (
        <div className="table-responsive">
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Estudiante</th>
                <th>Materia</th>
                <th>Calificación</th>
                <th>Fecha</th>
                <th>Comentarios</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {filteredGrades.map((grade) => {
                const studentName = (grade.student as any).firstName
                  ? `${(grade.student as any).firstName} ${(grade.student as any).lastName}`
                  : "Estudiante"

                return (
                  <tr key={grade.id}>
                    <td>{grade.id}</td>
                    <td>{studentName}</td>
                    <td>{grade.subject}</td>
                    <td className="grade-score">{grade.score}</td>
                    <td>{new Date(grade.date).toLocaleDateString()}</td>
                    <td>{grade.comments || "-"}</td>
                    <td className="actions">
                      <Link to={`/grades/edit/${grade.id}`} className="btn-action edit">
                        <i className="fas fa-edit"></i>
                      </Link>
                      <button className="btn-action delete" onClick={() => grade.id && handleDelete(grade.id)}>
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

export default GradeList

