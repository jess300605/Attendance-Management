package com.attendance.config;

import com.attendance.model.*;
import com.attendance.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private AttendanceSessionRepository attendanceSessionRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Checking if data initialization is needed...");

        // Verificar si ya existen datos
        if (teacherRepository.count() > 0) {
            System.out.println("Data already exists. Skipping initialization.");
            return;
        }

        System.out.println("Initializing data...");

        try {
            // Crear profesores con credenciales claras
            Teacher teacher1 = new Teacher();
            teacher1.setFirstName("Juan");
            teacher1.setLastName("Pérez");
            teacher1.setEmail("profesor1@escuela.edu");
            teacher1.setEmployeeId("T001");
            teacher1.setPassword("profesor123");

            Teacher teacher2 = new Teacher();
            teacher2.setFirstName("María");
            teacher2.setLastName("González");
            teacher2.setEmail("profesor2@escuela.edu");
            teacher2.setEmployeeId("T002");
            teacher2.setPassword("profesor123");

            List<Teacher> savedTeachers = teacherRepository.saveAll(Arrays.asList(teacher1, teacher2));
            System.out.println("Teachers saved: " + savedTeachers.size());

            // Crear estudiantes
            List<Student> students = new ArrayList<>();

            for (int i = 1; i <= 20; i++) {
                Student student = new Student();
                student.setFirstName("Estudiante" + i);
                student.setLastName("Apellido" + i);
                student.setEmail("estudiante" + i + "@escuela.edu");
                student.setStudentId("S" + String.format("%03d", i));
                students.add(student);
            }

            List<Student> savedStudents = studentRepository.saveAll(students);
            System.out.println("Students saved: " + savedStudents.size());

            // Crear aulas
            Classroom classroom1 = new Classroom();
            classroom1.setName("Matemáticas 101");
            classroom1.setCourseCode("MAT101");
            classroom1.setDescription("Curso básico de matemáticas");
            classroom1.setTeacher(teacher1);
            classroom1.setStudents(new ArrayList<>(savedStudents.subList(0, 10)));

            Classroom classroom2 = new Classroom();
            classroom2.setName("Ciencias 101");
            classroom2.setCourseCode("CIE101");
            classroom2.setDescription("Curso básico de ciencias");
            classroom2.setTeacher(teacher1);
            classroom2.setStudents(new ArrayList<>(savedStudents.subList(5, 15)));

            Classroom classroom3 = new Classroom();
            classroom3.setName("Historia 101");
            classroom3.setCourseCode("HIS101");
            classroom3.setDescription("Curso básico de historia");
            classroom3.setTeacher(teacher2);
            classroom3.setStudents(new ArrayList<>(savedStudents.subList(10, 20)));

            List<Classroom> savedClassrooms = classroomRepository.saveAll(Arrays.asList(classroom1, classroom2, classroom3));
            System.out.println("Classrooms saved: " + savedClassrooms.size());

            // Verificar que los estudiantes se hayan asignado correctamente
            for (Classroom classroom : savedClassrooms) {
                System.out.println("Classroom: " + classroom.getName() + " has " +
                        (classroom.getStudents() != null ? classroom.getStudents().size() : 0) +
                        " students");
            }

            // Crear sesiones de asistencia
            AttendanceSession session1 = new AttendanceSession();
            session1.setClassroom(classroom1);
            session1.setDate(LocalDate.now());
            session1.setStartTime(LocalTime.of(8, 0));
            session1.setEndTime(LocalTime.of(10, 0));
            session1.setTopic("Introducción a álgebra");

            AttendanceSession session2 = new AttendanceSession();
            session2.setClassroom(classroom2);
            session2.setDate(LocalDate.now());
            session2.setStartTime(LocalTime.of(10, 30));
            session2.setEndTime(LocalTime.of(12, 30));
            session2.setTopic("Introducción a biología");

            List<AttendanceSession> savedSessions = attendanceSessionRepository.saveAll(Arrays.asList(session1, session2));
            System.out.println("Attendance sessions saved: " + savedSessions.size());

            // Registrar asistencia para algunos estudiantes
            List<Attendance> attendanceRecords = new ArrayList<>();

            for (Student student : classroom1.getStudents().subList(0, 8)) {
                Attendance attendance = new Attendance();
                attendance.setStudent(student);
                attendance.setAttendanceSession(session1);
                attendance.setType(Attendance.AttendanceType.STUDENT);
                attendance.setPresent(true);
                attendance.setTimeIn(LocalTime.of(8, 0));
                attendance.setTimeOut(LocalTime.of(10, 0));
                attendanceRecords.add(attendance);
            }

            // Dos estudiantes ausentes
            for (Student student : classroom1.getStudents().subList(8, 10)) {
                Attendance attendance = new Attendance();
                attendance.setStudent(student);
                attendance.setAttendanceSession(session1);
                attendance.setType(Attendance.AttendanceType.STUDENT);
                attendance.setPresent(false);
                attendance.setNotes("Ausente sin justificación");
                attendanceRecords.add(attendance);
            }

            List<Attendance> savedAttendance = attendanceRepository.saveAll(attendanceRecords);
            System.out.println("Attendance records saved: " + savedAttendance.size());

            // Registrar algunas calificaciones
            List<Grade> grades = new ArrayList<>();

            for (Student student : classroom1.getStudents()) {
                Grade grade = new Grade();
                grade.setStudent(student);
                grade.setClassroom(classroom1);
                grade.setEvaluationType("Examen parcial");
                grade.setScore(Math.random() * 10);
                grade.setDate(LocalDate.now().minusDays(7));
                grade.setComments("Primer examen parcial");
                grades.add(grade);
            }

            List<Grade> savedGrades = gradeRepository.saveAll(grades);
            System.out.println("Grades saved: " + savedGrades.size());

            System.out.println("Data initialization completed successfully!");

            // Imprimir credenciales para facilitar el acceso
            System.out.println("\n=== CREDENCIALES DE ACCESO ===");
            System.out.println("Profesor 1: profesor1@escuela.edu / profesor123");
            System.out.println("Profesor 2: profesor2@escuela.edu / profesor123");
            System.out.println("============================\n");

        } catch (Exception e) {
            System.out.println("Error initializing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
