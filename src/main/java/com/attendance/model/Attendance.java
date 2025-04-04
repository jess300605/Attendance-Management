package com.attendance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attendance")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean present;

    private LocalTime timeIn;

    private LocalTime timeOut;

    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceType type; // STUDENT o TEACHER

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnoreProperties({"attendanceRecords", "grades", "classrooms"})
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @JsonIgnoreProperties({"attendanceRecords", "classrooms", "password"})
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "attendance_session_id")
    @JsonIgnoreProperties({"attendanceRecords"})
    private AttendanceSession attendanceSession;

    public enum AttendanceType {
        STUDENT,
        TEACHER
    }
}

