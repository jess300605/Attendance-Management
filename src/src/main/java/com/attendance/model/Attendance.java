package com.attendance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attendance")
public class Attendance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(nullable = false)
    private LocalTime timeIn;
    
    private LocalTime timeOut;
    
    @Column(nullable = false)
    private boolean present;
    
    private String notes;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceType type; // STUDENT or TEACHER
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    
    public enum AttendanceType {
        STUDENT,
        TEACHER
    }
}

