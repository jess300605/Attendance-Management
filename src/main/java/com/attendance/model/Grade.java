package com.attendance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "grades")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnoreProperties({"grades", "attendanceRecords", "classrooms"})
    private Student student;

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    @JsonIgnoreProperties({"students", "attendanceSessions", "teacher"})
    private Classroom classroom;

    @Column(nullable = false)
    private String evaluationType; // Examen, Tarea, Proyecto, etc.

    @Column(nullable = false)
    private Double score;

    @Column(nullable = false)
    private LocalDate date;

    private String comments;
}

