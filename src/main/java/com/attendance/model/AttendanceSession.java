package com.attendance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attendance_sessions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AttendanceSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    private LocalTime endTime;

    private String topic;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    @JsonIgnoreProperties({"attendanceSessions", "students"})
    private Classroom classroom;

    @OneToMany(mappedBy = "attendanceSession", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("attendanceSession")
    private List<Attendance> attendanceRecords = new ArrayList<>();
}

