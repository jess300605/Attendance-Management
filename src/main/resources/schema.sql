-- Este script se ejecutará automáticamente si spring.jpa.hibernate.ddl-auto=none
-- Para usar este script, cambia spring.jpa.hibernate.ddl-auto=none en application.properties

CREATE DATABASE IF NOT EXISTS attendance_db;
USE attendance_db;

-- Tabla de profesores
CREATE TABLE IF NOT EXISTS teacher (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    employee_id VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Tabla de estudiantes
CREATE TABLE IF NOT EXISTS student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    student_id VARCHAR(50) UNIQUE NOT NULL
);

-- Tabla de aulas/clases
CREATE TABLE IF NOT EXISTS classroom (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    course_code VARCHAR(50) NOT NULL,
    description TEXT,
    teacher_id BIGINT,
    FOREIGN KEY (teacher_id) REFERENCES teacher(id)
);

-- Tabla de relación entre aulas y estudiantes
CREATE TABLE IF NOT EXISTS classroom_student (
    classroom_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    PRIMARY KEY (classroom_id, student_id),
    FOREIGN KEY (classroom_id) REFERENCES classroom(id),
    FOREIGN KEY (student_id) REFERENCES student(id)
);

-- Tabla de sesiones de asistencia
CREATE TABLE IF NOT EXISTS attendance_session (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    classroom_id BIGINT NOT NULL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    topic VARCHAR(255),
    FOREIGN KEY (classroom_id) REFERENCES classroom(id)
);

-- Tabla de registros de asistencia
CREATE TABLE IF NOT EXISTS attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    attendance_session_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    present BOOLEAN NOT NULL,
    time_in TIME,
    time_out TIME,
    notes TEXT,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (attendance_session_id) REFERENCES attendance_session(id)
);

-- Tabla de calificaciones
CREATE TABLE IF NOT EXISTS grade (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    classroom_id BIGINT NOT NULL,
    evaluation_type VARCHAR(100) NOT NULL,
    score DOUBLE NOT NULL,
    date DATE NOT NULL,
    comments TEXT,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (classroom_id) REFERENCES classroom(id)
);
