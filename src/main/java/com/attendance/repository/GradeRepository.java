package com.attendance.repository;

import com.attendance.model.Classroom;
import com.attendance.model.Grade;
import com.attendance.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudent(Student student);
    List<Grade> findByClassroom(Classroom classroom);
    List<Grade> findByStudentAndClassroom(Student student, Classroom classroom);

    // Reemplazamos el método findByStudentAndSubject con una consulta que use evaluationType
    @Query("SELECT g FROM Grade g WHERE g.student = :student AND g.evaluationType = :evaluationType")
    List<Grade> findByStudentAndSubject(@Param("student") Student student, @Param("evaluationType") String evaluationType);
}

