// Student types
export interface Student {
  id?: number
  firstName: string
  lastName: string
  studentId: string
  email: string
  attendanceRecords?: Attendance[]
  grades?: Grade[]
}

// Teacher types
export interface Teacher {
  id?: number
  firstName: string
  lastName: string
  employeeId: string
  email: string
  attendanceRecords?: Attendance[]
}

// Attendance types
export interface Attendance {
  id?: number
  date: string
  timeIn: string
  timeOut?: string
  present: boolean
  notes?: string
  type: "STUDENT" | "TEACHER"
  student?: Student | { id: number }
  teacher?: Teacher | { id: number }
}

// Grade types
export interface Grade {
  id?: number
  student: Student | { id: number }
  subject: string
  score: number
  date: string
  comments?: string
}

// API Response types
export interface ApiResponse<T> {
  data: T
  message?: string
  status: number
}

export interface PaginatedResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

