package com.attendance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "classrooms")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String courseCode;

    private String description;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    @JsonIgnoreProperties({"classrooms", "attendanceRecords", "password"})
    private Teacher teacher;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "classroom_student",
            joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonIgnoreProperties({"classrooms", "attendanceRecords", "grades"})
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("classroom")
    private List<AttendanceSession> attendanceSessions = new ArrayList<>();
}

