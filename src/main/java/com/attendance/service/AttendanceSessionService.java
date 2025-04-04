package com.attendance.service;

import com.attendance.model.*;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.AttendanceSessionRepository;
import com.attendance.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceSessionService {

    private final AttendanceSessionRepository attendanceSessionRepository;
    private final ClassroomRepository classroomRepository;
    private final AttendanceRepository attendanceRepository;

    @Autowired
    public AttendanceSessionService(AttendanceSessionRepository attendanceSessionRepository,
                                    ClassroomRepository classroomRepository,
                                    AttendanceRepository attendanceRepository) {
        this.attendanceSessionRepository = attendanceSessionRepository;
        this.classroomRepository = classroomRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public List<AttendanceSession> getAllAttendanceSessions() {
        return attendanceSessionRepository.findAll();
    }

    public Optional<AttendanceSession> getAttendanceSessionById(Long id) {
        return attendanceSessionRepository.findById(id);
    }

    public List<AttendanceSession> getAttendanceSessionsByClassroom(Long classroomId) {
        return attendanceSessionRepository.findByClassroomId(classroomId);
    }

    public Optional<AttendanceSession> getAttendanceSessionByClassroomAndDate(Long classroomId, LocalDate date) {
        return attendanceSessionRepository.findByClassroomIdAndDate(classroomId, date);
    }

    public AttendanceSession createAttendanceSession(Long classroomId, AttendanceSession session) {
        Optional<Classroom> classroomOpt = classroomRepository.findById(classroomId);

        if (classroomOpt.isPresent()) {
            Classroom classroom = classroomOpt.get();
            session.setClassroom(classroom);

            // Verificar si ya existe una sesión para esta fecha y aula
            Optional<AttendanceSession> existingSession =
                    attendanceSessionRepository.findByClassroomIdAndDate(classroomId, session.getDate());

            if (existingSession.isPresent()) {
                return existingSession.get();
            }

            // Guardar la nueva sesión
            AttendanceSession savedSession = attendanceSessionRepository.save(session);

            // Inicializar la lista de asistencias
            if (savedSession.getAttendanceRecords() == null) {
                savedSession.setAttendanceRecords(new ArrayList<>());
            }

            return savedSession;
        }

        return null;
    }

    public List<Attendance> takeAttendanceForSession(Long sessionId, List<Attendance> attendanceRecords) {
        Optional<AttendanceSession> sessionOpt = attendanceSessionRepository.findById(sessionId);

        if (sessionOpt.isPresent()) {
            AttendanceSession session = sessionOpt.get();
            List<Attendance> savedRecords = new ArrayList<>();

            for (Attendance record : attendanceRecords) {
                record.setAttendanceSession(session);
                record.setType(Attendance.AttendanceType.STUDENT);
                record.setTimeIn(LocalTime.now());
                savedRecords.add(attendanceRepository.save(record));
            }

            return savedRecords;
        }

        return null;
    }

    public AttendanceSession updateAttendanceSession(Long id, AttendanceSession session) {
        Optional<AttendanceSession> existingSessionOpt = attendanceSessionRepository.findById(id);

        if (existingSessionOpt.isPresent()) {
            AttendanceSession existingSession = existingSessionOpt.get();

            existingSession.setDate(session.getDate());
            existingSession.setStartTime(session.getStartTime());
            existingSession.setEndTime(session.getEndTime());
            existingSession.setTopic(session.getTopic());
            existingSession.setNotes(session.getNotes());

            return attendanceSessionRepository.save(existingSession);
        }

        return null;
    }

    public void deleteAttendanceSession(Long id) {
        attendanceSessionRepository.deleteById(id);
    }
}

