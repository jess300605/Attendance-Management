package com.attendance.repository;

import com.attendance.model.Attendance;
import com.attendance.model.AttendanceSession;
import com.attendance.model.Student;
import com.attendance.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudent(Student student);
    List<Attendance> findByTeacher(Teacher teacher);

    // Reemplazamos el método findByDate con una consulta JPQL
    @Query("SELECT a FROM Attendance a WHERE a.attendanceSession.date = :date")
    List<Attendance> findByDate(@Param("date") LocalDate date);

    List<Attendance> findByStudentAndAttendanceSession_DateBetween(Student student, LocalDate startDate, LocalDate endDate);
    List<Attendance> findByTeacherAndAttendanceSession_DateBetween(Teacher teacher, LocalDate startDate, LocalDate endDate);
}

