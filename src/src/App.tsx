import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import { ToastContainer } from "react-toastify"
import "react-toastify/dist/ReactToastify.css"
import "./App.css"

// Layouts
import MainLayout from "./layouts/MainLayout"

// Pages
import Dashboard from "./pages/Dashboard"
import StudentList from "./pages/students/StudentList"
import StudentForm from "./pages/students/StudentForm"
import StudentDetail from "./pages/students/StudentDetail"
import TeacherList from "./pages/teachers/TeacherList"
import TeacherForm from "./pages/teachers/TeacherForm"
import TeacherDetail from "./pages/teachers/TeacherDetail"
import AttendanceList from "./pages/attendance/AttendanceList"
import AttendanceForm from "./pages/attendance/AttendanceForm"
import GradeList from "./pages/grades/GradeList"
import GradeForm from "./pages/grades/GradeForm"
import Reports from "./pages/reports/Reports"

function App() {
  return (
    <Router>
      <ToastContainer position="top-right" autoClose={3000} />
      <Routes>
        <Route path="/" element={<MainLayout />}>
          <Route index element={<Dashboard />} />

          {/* Student Routes */}
          <Route path="students" element={<StudentList />} />
          <Route path="students/new" element={<StudentForm />} />
          <Route path="students/edit/:id" element={<StudentForm />} />
          <Route path="students/:id" element={<StudentDetail />} />

          {/* Teacher Routes */}
          <Route path="teachers" element={<TeacherList />} />
          <Route path="teachers/new" element={<TeacherForm />} />
          <Route path="teachers/edit/:id" element={<TeacherForm />} />
          <Route path="teachers/:id" element={<TeacherDetail />} />

          {/* Attendance Routes */}
          <Route path="attendance" element={<AttendanceList />} />
          <Route path="attendance/new" element={<AttendanceForm />} />
          <Route path="attendance/edit/:id" element={<AttendanceForm />} />

          {/* Grade Routes */}
          <Route path="grades" element={<GradeList />} />
          <Route path="grades/new" element={<GradeForm />} />
          <Route path="grades/edit/:id" element={<GradeForm />} />

          {/* Reports */}
          <Route path="reports" element={<Reports />} />
        </Route>
      </Routes>
    </Router>
  )
}

export default App

