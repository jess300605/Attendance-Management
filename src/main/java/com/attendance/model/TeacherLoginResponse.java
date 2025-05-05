package com.attendance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherLoginResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String employeeId;
    private String token;
    private boolean attendanceRegistered;
    private long expiresIn; // Tiempo de expiración en milisegundos
}
