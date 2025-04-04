package com.attendance.controller;

import com.attendance.model.TeacherLogin;
import com.attendance.model.TeacherLoginResponse;
import com.attendance.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AuthController {

    private final TeacherService teacherService;

    @Autowired
    public AuthController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody TeacherLogin loginRequest) {
        try {
            System.out.println("Auth login request received: " + loginRequest);

            TeacherLoginResponse response = teacherService.login(loginRequest);

            if (response != null) {
                return ResponseEntity.ok(response);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas");
        } catch (Exception e) {
            System.out.println("Error in auth login controller: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el servidor: " + e.getMessage());
        }
    }
}

