package com.attendance.service;

import com.attendance.model.Classroom;
import com.attendance.model.Student;
import com.attendance.model.Teacher;
import com.attendance.repository.ClassroomRepository;
import com.attendance.repository.StudentRepository;
import com.attendance.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public ClassroomService(ClassroomRepository classroomRepository,
                            TeacherRepository teacherRepository,
                            StudentRepository studentRepository) {
        this.classroomRepository = classroomRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Classroom> getClassroomById(Long id) {
        Optional<Classroom> classroom = classroomRepository.findById(id);

        // Verificar si el aula tiene estudiantes
        if (classroom.isPresent() && classroom.get().getStudents() == null) {
            classroom.get().setStudents(new ArrayList<>());
        }

        return classroom;
    }

    @Transactional(readOnly = true)
    public List<Classroom> getClassroomsByTeacher(Long teacherId) {
        List<Classroom> classrooms = classroomRepository.findByTeacherId(teacherId);

        // Verificar si las aulas tienen estudiantes
        for (Classroom classroom : classrooms) {
            if (classroom.getStudents() == null) {
                classroom.setStudents(new ArrayList<>());
            }
        }

        return classrooms;
    }

    @Transactional
    public Classroom saveClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    @Transactional
    public void deleteClassroom(Long id) {
        classroomRepository.deleteById(id);
    }

    @Transactional
    public Classroom addStudentToClassroom(Long classroomId, Long studentId) {
        Optional<Classroom> classroomOpt = classroomRepository.findById(classroomId);
        Optional<Student> studentOpt = studentRepository.findById(studentId);

        if (classroomOpt.isPresent() && studentOpt.isPresent()) {
            Classroom classroom = classroomOpt.get();
            Student student = studentOpt.get();

            if (classroom.getStudents() == null) {
                classroom.setStudents(new ArrayList<>());
            }

            if (!classroom.getStudents().contains(student)) {
                classroom.getStudents().add(student);
                return classroomRepository.save(classroom);
            }

            return classroom;
        }

        return null;
    }

    @Transactional
    public Classroom removeStudentFromClassroom(Long classroomId, Long studentId) {
        Optional<Classroom> classroomOpt = classroomRepository.findById(classroomId);
        Optional<Student> studentOpt = studentRepository.findById(studentId);

        if (classroomOpt.isPresent() && studentOpt.isPresent()) {
            Classroom classroom = classroomOpt.get();
            Student student = studentOpt.get();

            if (classroom.getStudents() != null) {
                classroom.getStudents().remove(student);
                return classroomRepository.save(classroom);
            }

            return classroom;
        }

        return null;
    }
}

