### API de Asistencia Escolar - Documentación

## Descripción General

La API de Asistencia Escolar es un sistema backend desarrollado en Java que proporciona servicios para la gestión de asistencia, estudiantes, aulas y calificaciones en entornos educativos. Esta API está diseñada para trabajar con el frontend React que permite a los docentes gestionar sus clases, registrar asistencia y calificaciones, y generar reportes.

## Características Principales

- Gestión completa de aulas/salones
- Administración de estudiantes
- Registro y seguimiento de asistencia
- Sistema de calificaciones
- Autenticación y autorización mediante JWT
- Exportación de datos
- Persistencia en base de datos MySQL


## Requisitos del Sistema

- Java 11 o superior
- Maven 3.6 o superior
- MySQL 5.7 o superior (configurado en XAMPP)
- Servidor Tomcat (incluido en Spring Boot)
- XAMPP (para MySQL y phpMyAdmin)


## Configuración y Despliegue

### 1. Configuración de la Base de Datos

1. Inicie XAMPP y asegúrese de que los servicios de MySQL estén funcionando
2. Cree una base de datos llamada `attendance_db`
3. Configure el usuario y contraseña en el archivo `application.properties`


```plaintex
# Configuración de la base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/attendance_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración de JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
```

### 2. Compilación y Ejecución

```shellscript
# Clonar el repositorio
git clone https://github.com/su-usuario/attendance-api.git
cd attendance-api

# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

La API estará disponible en `http://localhost:8080`

## Estructura del Proyecto

```plaintext
attendance-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── attendance/
│   │   │           ├── config/           # Configuraciones (Spring, Security, etc.)
│   │   │           ├── controller/       # Controladores REST
│   │   │           ├── dto/              # Objetos de transferencia de datos
│   │   │           ├── exception/        # Manejo de excepciones
│   │   │           ├── model/            # Entidades JPA
│   │   │           ├── repository/       # Repositorios JPA
│   │   │           ├── security/         # Configuración de seguridad y JWT
│   │   │           ├── service/          # Servicios de negocio
│   │   │           └── util/             # Clases utilitarias
│   │   └── resources/
│   │       ├── application.properties    # Configuración principal
│   │       └── data.sql                  # Datos iniciales (opcional)
│   └── test/                             # Pruebas unitarias e integración
├── pom.xml                               # Dependencias Maven
└── README.md                             # Este archivo
```

## Endpoints de la API

### Autenticación

- `POST /api/auth/login` - Iniciar sesión y obtener token JWT
- `POST /api/auth/register` - Registrar nuevo usuario (solo administradores)


### Aulas/Salones

- `GET /api/classrooms` - Obtener todas las aulas
- `GET /api/classrooms/{id}` - Obtener aula por ID
- `GET /api/classrooms/teacher/{teacherId}` - Obtener aulas por profesor
- `POST /api/classrooms` - Crear nueva aula
- `PUT /api/classrooms/{id}` - Actualizar aula existente
- `DELETE /api/classrooms/{id}` - Eliminar aula
- `POST /api/classrooms/{id}/students` - Asignar estudiantes a un aula


### Estudiantes

- `GET /api/students` - Obtener todos los estudiantes
- `GET /api/students/{id}` - Obtener estudiante por ID
- `POST /api/students` - Crear nuevo estudiante
- `PUT /api/students/{id}` - Actualizar estudiante existente
- `DELETE /api/students/{id}` - Eliminar estudiante


### Sesiones de Asistencia

- `GET /api/attendance-sessions` - Obtener todas las sesiones
- `GET /api/attendance-sessions/{id}` - Obtener sesión por ID
- `GET /api/attendance-sessions/classroom/{classroomId}` - Obtener sesiones por aula
- `POST /api/attendance-sessions` - Crear nueva sesión
- `PUT /api/attendance-sessions/{id}` - Actualizar sesión existente
- `DELETE /api/attendance-sessions/{id}` - Eliminar sesión
- `POST /api/attendance-sessions/{id}/take-attendance` - Registrar asistencia para una sesión


### Calificaciones

- `GET /api/grades` - Obtener todas las calificaciones
- `GET /api/grades/{id}` - Obtener calificación por ID
- `GET /api/grades/classroom/{classroomId}` - Obtener calificaciones por aula
- `GET /api/grades/student/{studentId}` - Obtener calificaciones por estudiante
- `POST /api/grades` - Crear nueva calificación
- `PUT /api/grades/{id}` - Actualizar calificación existente
- `DELETE /api/grades/{id}` - Eliminar calificación

## Seguridad y Autenticación

La API utiliza JWT (JSON Web Tokens) para la autenticación y autorización:

1. El cliente envía credenciales (usuario/contraseña) al endpoint `/api/auth/login`
2. El servidor valida las credenciales y devuelve un token JWT
3. El cliente debe incluir este token en el encabezado `Authorization` de todas las solicitudes posteriores
4. El token tiene una validez de 24 horas por defecto


Ejemplo de encabezado de autorización:

```plaintext
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Inicialización de Datos

La API incluye un `DataInitializer` que crea datos de prueba al iniciar la aplicación:

- Usuarios administradores y profesores
- Estudiantes de muestra
- Aulas/salones
- Sesiones de asistencia


Credenciales de prueba:

- Admin: `admin / admin123`
- Profesor: `teacher / teacher123`


## Tecnologías Utilizadas

- **Spring Boot**: Framework principal
- **Spring Data JPA**: Persistencia y acceso a datos
- **Spring Security**: Seguridad y autenticación
- **JWT**: Tokens de autenticación
- **MySQL**: Base de datos relacional
- **Lombok**: Reducción de código boilerplate
- **ModelMapper**: Mapeo entre entidades y DTOs
- **Swagger/OpenAPI**: Documentación de la API


## Configuración CORS

La API está configurada para permitir solicitudes CORS desde el frontend:

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

## Solución de Problemas Comunes

### Error de conexión a la base de datos

1. Verifique que XAMPP esté en ejecución y el servicio MySQL activo
2. Compruebe las credenciales en `application.properties`
3. Asegúrese de que la base de datos `attendance_db` exista


### Error de autenticación

1. Verifique que está enviando el token JWT correctamente en el encabezado
2. El token puede haber expirado (validez de 24 horas)
3. Intente iniciar sesión nuevamente para obtener un nuevo token


### Error 500 al obtener datos

1. Revise los logs del servidor para identificar la causa exacta
2. Verifique que las relaciones entre entidades estén correctamente configuradas
3. Compruebe que los IDs utilizados en las solicitudes existan en la base de datos


## Desarrollo y Contribución

### Requisitos para Desarrolladores

- JDK 11 o superior
- IDE Java (IntelliJ IDEA, Eclipse, etc.)
- Maven
- Git
- Postman o herramienta similar para probar la API

---

© 2023 Sistema de Gestión de Asistencia Escolar. Todos los derechos reservados.
