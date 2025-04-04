package com.attendance.repository;

import com.attendance.model.AttendanceSession;
import com.attendance.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {
    List<AttendanceSession> findByClassroom(Classroom classroom);
    List<AttendanceSession> findByClassroomId(Long classroomId);
    List<AttendanceSession> findByClassroomAndDate(Classroom classroom, LocalDate date);
    Optional<AttendanceSession> findByClassroomIdAndDate(Long classroomId, LocalDate date);
}

