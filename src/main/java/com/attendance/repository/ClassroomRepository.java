package com.attendance.repository;

import com.attendance.model.Classroom;
import com.attendance.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    List<Classroom> findByTeacher(Teacher teacher);
    List<Classroom> findByTeacherId(Long teacherId);
}

