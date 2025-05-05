package com.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.attendance")
public class AttendanceManagementApplication {

    public static void main(String[] args) {
        // Establecer el perfil de MySQL como predeterminado si no se especifica otro
        System.setProperty("spring.profiles.active",
                System.getProperty("spring.profiles.active", "mysql"));

        SpringApplication.run(AttendanceApplication.class, args);
    }
}
