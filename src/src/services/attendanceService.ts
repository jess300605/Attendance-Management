import api from "./api"
import type { Attendance } from "../types"

const BASE_PATH = "/attendance"

export const getAttendance = async (): Promise<Attendance[]> => {
  const response = await api.get<Attendance[]>(BASE_PATH)
  return response.data
}

export const getAttendanceById = async (id: number): Promise<Attendance> => {
  const response = await api.get<Attendance>(`${BASE_PATH}/${id}`)
  return response.data
}

export const getAttendanceByStudent = async (studentId: number): Promise<Attendance[]> => {
  const response = await api.get<Attendance[]>(`${BASE_PATH}/students/${studentId}`)
  return response.data
}

export const getAttendanceByTeacher = async (teacherId: number): Promise<Attendance[]> => {
  const response = await api.get<Attendance[]>(`${BASE_PATH}/teachers/${teacherId}`)
  return response.data
}

export const getAttendanceByDate = async (date: string): Promise<Attendance[]> => {
  const response = await api.get<Attendance[]>(`${BASE_PATH}/date/${date}`)
  return response.data
}

export const getStudentAttendanceBetweenDates = async (
  studentId: number,
  startDate: string,
  endDate: string,
): Promise<Attendance[]> => {
  const response = await api.get<Attendance[]>(
    `${BASE_PATH}/students/${studentId}/between?startDate=${startDate}&endDate=${endDate}`,
  )
  return response.data
}

export const getTeacherAttendanceBetweenDates = async (
  teacherId: number,
  startDate: string,
  endDate: string,
): Promise<Attendance[]> => {
  const response = await api.get<Attendance[]>(
    `${BASE_PATH}/teachers/${teacherId}/between?startDate=${startDate}&endDate=${endDate}`,
  )
  return response.data
}

export const createAttendance = async (attendance: Attendance): Promise<Attendance> => {
  const response = await api.post<Attendance>(BASE_PATH, attendance)
  return response.data
}

export const updateAttendance = async (id: number, attendance: Attendance): Promise<Attendance> => {
  const response = await api.put<Attendance>(`${BASE_PATH}/${id}`, attendance)
  return response.data
}

export const deleteAttendance = async (id: number): Promise<void> => {
  await api.delete(`${BASE_PATH}/${id}`)
}

