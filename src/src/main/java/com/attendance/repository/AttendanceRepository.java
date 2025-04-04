package com.attendance.repository;

import com.attendance.model.Attendance;
import com.attendance.model.Student;
import com.attendance.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudent(Student student);
    List<Attendance> findByTeacher(Teacher teacher);
    List<Attendance> findByDate(LocalDate date);
    List<Attendance> findByStudentAndDateBetween(Student student, LocalDate startDate, LocalDate endDate);
    List<Attendance> findByTeacherAndDateBetween(Teacher teacher, LocalDate startDate, LocalDate endDate);
}

