package com.attendance.service;

import com.attendance.model.Attendance;
import com.attendance.model.Student;
import com.attendance.model.Teacher;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.StudentRepository;
import com.attendance.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public AttendanceService(
            AttendanceRepository attendanceRepository,
            StudentRepository studentRepository,
            TeacherRepository teacherRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Transactional(readOnly = true)
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Attendance> getAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceByStudent(Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        return student.map(attendanceRepository::findByStudent).orElse(List.of());
    }

    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceByTeacher(Long teacherId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        return teacher.map(attendanceRepository::findByTeacher).orElse(List.of());
    }

    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    @Transactional(readOnly = true)
    public List<Attendance> getStudentAttendanceBetweenDates(Long studentId, LocalDate startDate, LocalDate endDate) {
        Optional<Student> student = studentRepository.findById(studentId);
        return student.map(s -> attendanceRepository.findByStudentAndAttendanceSession_DateBetween(s, startDate, endDate))
                .orElse(List.of());
    }

    @Transactional(readOnly = true)
    public List<Attendance> getTeacherAttendanceBetweenDates(Long teacherId, LocalDate startDate, LocalDate endDate) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        return teacher.map(t -> attendanceRepository.findByTeacherAndAttendanceSession_DateBetween(t, startDate, endDate))
                .orElse(List.of());
    }

    @Transactional
    public Attendance saveAttendance(Attendance attendance) {
        // Si es asistencia de estudiante, verificar que el estudiante exista
        if (attendance.getType() == Attendance.AttendanceType.STUDENT && attendance.getStudent() != null) {
            Optional<Student> studentOpt = studentRepository.findById(attendance.getStudent().getId());
            if (studentOpt.isPresent()) {
                attendance.setStudent(studentOpt.get());
            }
        }

        // Si es asistencia de profesor, verificar que el profesor exista
        if (attendance.getType() == Attendance.AttendanceType.TEACHER && attendance.getTeacher() != null) {
            Optional<Teacher> teacherOpt = teacherRepository.findById(attendance.getTeacher().getId());
            if (teacherOpt.isPresent()) {
                attendance.setTeacher(teacherOpt.get());
            }
        }

        return attendanceRepository.save(attendance);
    }

    @Transactional
    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }
}

