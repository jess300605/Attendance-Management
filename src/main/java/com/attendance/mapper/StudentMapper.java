package com.attendance.mapper;

import com.attendance.dto.StudentDTO;
import com.attendance.model.Student;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentMapper {

    public StudentDTO toDTO(Student student) {
        if (student == null) {
            return null;
        }

        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setStudentId(student.getStudentId());
        dto.setEmail(student.getEmail());

        return dto;
    }

    public Student toEntity(StudentDTO dto) {
        if (dto == null) {
            return null;
        }

        Student student = new Student();
        student.setId(dto.getId());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setStudentId(dto.getStudentId());
        student.setEmail(dto.getEmail());

        return student;
    }

    public List<StudentDTO> toDTOList(List<Student> students) {
        return students.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<Student> toEntityList(List<StudentDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public void updateEntityFromDTO(StudentDTO dto, Student student) {
        if (dto.getFirstName() != null) {
            student.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            student.setLastName(dto.getLastName());
        }
        if (dto.getEmail() != null) {
            student.setEmail(dto.getEmail());
        }
        // No actualizamos studentId ya que es un identificador único
    }
}
