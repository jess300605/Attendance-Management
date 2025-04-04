package com.attendance.service;

import com.attendance.model.Attendance;
import com.attendance.model.Student;
import com.attendance.model.Teacher;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.StudentRepository;
import com.attendance.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }
    
    public Optional<Attendance> getAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }
    
    public List<Attendance> getAttendanceByStudent(Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        return student.map(attendanceRepository::findByStudent).orElse(List.of());
    }
    
    public List<Attendance> getAttendanceByTeacher(Long teacherId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        return teacher.map(attendanceRepository::findByTeacher).orElse(List.of());
    }
    
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }
    
    public List<Attendance> getStudentAttendanceBetweenDates(Long studentId, LocalDate startDate, LocalDate endDate) {
        Optional<Student> student = studentRepository.findById(studentId);
        return student.map(s -> attendanceRepository.findByStudentAndDateBetween(s, startDate, endDate))
                .orElse(List.of());
    }
    
    public List<Attendance> getTeacherAttendanceBetweenDates(Long teacherId, LocalDate startDate, LocalDate endDate) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        return teacher.map(t -> attendanceRepository.findByTeacherAndDateBetween(t, startDate, endDate))
                .orElse(List.of());
    }
    
    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }
    
    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }
}

