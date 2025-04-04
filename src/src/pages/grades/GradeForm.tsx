"use client"

import type React from "react"
import { useState, useEffect } from "react"
import { useParams, useNavigate, useLocation } from "react-router-dom"
import { toast } from "react-toastify"
import { getGradeById, createGrade, updateGrade } from "../../services/gradeService"
import { getStudents } from "../../services/studentService"
import type { Grade, Student } from "../../types"
import "./GradeForm.css"

const GradeForm: React.FC = () => {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const location = useLocation()
  const queryParams = new URLSearchParams(location.search)
  const studentIdParam = queryParams.get("studentId")

  const isEditMode = !!id

  const [formData, setFormData] = useState<Grade>({
    student: { id: studentIdParam ? Number.parseInt(studentIdParam) : 0 },
    subject: "",
    score: 0,
    date: new Date().toISOString().split("T")[0],
    comments: "",
  })

  const [students, setStudents] = useState<Student[]>([])
  const [loading, setLoading] = useState<boolean>(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    fetchData()
  }, [id])

  const fetchData = async () => {
    try {
      setLoading(true)

      // Fetch students
      const studentsData = await getStudents()
      setStudents(studentsData)

      // If editing, fetch grade
      if (isEditMode) {
        const gradeData = await getGradeById(Number.parseInt(id as string))

        // Format date for form input
        const formattedData = {
          ...gradeData,
          date: new Date(gradeData.date).toISOString().split("T")[0],
        }

        setFormData(formattedData)
      }

      setLoading(false)
    } catch (err) {
      setError("Error al cargar los datos")
      setLoading(false)
      console.error("Error fetching data:", err)
    }
  }

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target

    if (name === "studentId") {
      setFormData((prev) => ({
        ...prev,
        student: { id: Number.parseInt(value) },
      }))
    } else if (name === "score") {
      setFormData((prev) => ({
        ...prev,
        [name]: Number.parseFloat(value),
      }))
    } else {
      setFormData((prev) => ({
        ...prev,
        [name]: value,
      }))
    }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    // Validation
    if (!formData.subject || formData.score < 0 || !formData.date) {
      toast.error("Por favor completa los campos requeridos correctamente")
      return
    }

    if (!formData.student || (formData.student as any).id === 0) {
      toast.error("Por favor selecciona un estudiante")
      return
    }

    try {
      setLoading(true)

      if (isEditMode) {
        await updateGrade(Number.parseInt(id as string), formData)
        toast.success("Calificación actualizada correctamente")
      } else {
        await createGrade(formData)
        toast.success("Calificación creada correctamente")
      }

      navigate("/grades")
    } catch (err) {
      setError(`Error al ${isEditMode ? "actualizar" : "crear"} la calificación`)
      toast.error(`Error al ${isEditMode ? "actualizar" : "crear"} la calificación`)
      setLoading(false)
      console.error("Error saving grade:", err)
    }
  }

  if (loading && isEditMode) {
    return <div className="loading">Cargando datos...</div>
  }

  return (
    <div className="grade-form">
      <h1>{isEditMode ? "Editar Calificación" : "Nueva Calificación"}</h1>

      {error && <div className="alert alert-danger">{error}</div>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="studentId">Estudiante</label>
          <select
            id="studentId"
            name="studentId"
            value={(formData.student as any)?.id || ""}
            onChange={handleChange}
            className="form-control"
            required
            disabled={isEditMode} // Can't change student in edit mode
          >
            <option value="">Selecciona un estudiante</option>
            {students.map((student) => (
              <option key={student.id} value={student.id}>
                {student.firstName} {student.lastName} ({student.studentId})
              </option>
            ))}
          </select>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="subject">Materia</label>
            <input
              type="text"
              id="subject"
              name="subject"
              value={formData.subject}
              onChange={handleChange}
              className="form-control"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="score">Calificación</label>
            <input
              type="number"
              id="score"
              name="score"
              value={formData.score}
              onChange={handleChange}
              className="form-control"
              min="0"
              max="10"
              step="0.1"
              required
            />
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="date">Fecha</label>
          <input
            type="date"
            id="date"
            name="date"
            value={formData.date}
            onChange={handleChange}
            className="form-control"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="comments">Comentarios</label>
          <textarea
            id="comments"
            name="comments"
            value={formData.comments || ""}
            onChange={handleChange}
            className="form-control"
            rows={3}
          ></textarea>
        </div>

        <div className="form-actions">
          <button type="button" className="btn btn-secondary" onClick={() => navigate("/grades")}>
            Cancelar
          </button>
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? "Guardando..." : "Guardar"}
          </button>
        </div>
      </form>
    </div>
  )
}

export default GradeForm

