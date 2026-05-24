##### \# Nova Capital

##### 

##### Plataforma de coinversión y educación financiera con moneda virtual Aurus.

##### 

##### \*\*Autor:\*\* Àngel Martínez García  

##### \*\*Grado:\*\* DAM — IES La Vereda  

##### \*\*Curso:\*\* 2025-2026

##### 

##### \## Estructura del repositorio

##### 

##### \- `backend/` — API REST con Spring Boot 3.2 + Java 17

##### \- `frontend/` — App Android nativa (Android Studio)

##### \- `database/` — Script SQL con las 9 tablas y datos iniciales

##### \- `docs/` — Memoria del proyecto

##### 

##### \## Instrucciones de instalación

##### 

##### \### Backend

##### 1\. Ejecutar `database/nova-capital.sql` en MySQL Workbench

##### 2\. Abrir la carpeta `backend/` en IntelliJ IDEA

##### 3\. Configurar `application.properties` con tus credenciales MySQL

##### 4\. Ejecutar con el botón Run — el servidor arranca en el puerto 8080

##### 

##### \### Frontend

##### 1\. Abrir la carpeta `frontend/` en Android Studio

##### 2\. Verificar que en `ApiClient.java` la URL base es `http://10.0.2.2:8080/`

##### 3\. Lanzar el emulador y ejecutar la app

