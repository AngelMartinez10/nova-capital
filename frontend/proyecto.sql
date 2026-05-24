-- ======================================================
-- SCRIPT DDL - NOVA CAPITAL
-- Creación de la estructura de base de datos
-- ======================================================

CREATE DATABASE NovaCapital;
USE NovaCapital;

-- ========================
-- TABLA: CLIENTE
-- ========================
CREATE TABLE Cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    fecha_registro DATE DEFAULT (CURRENT_DATE)
);

-- ========================
-- TABLA: PROYECTO
-- ========================
CREATE TABLE Proyecto (
    id_proyecto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    categoria VARCHAR(100),
    objetivo_inversion DECIMAL(12,2),
    cantidad_actual DECIMAL(12,2) DEFAULT 0,
    fecha_creacion DATE DEFAULT (CURRENT_DATE),
    estado ENUM('Activo','Finalizado','En progreso') DEFAULT 'Activo',
    id_cliente INT,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
        ON DELETE SET NULL ON UPDATE CASCADE
);

-- ========================
-- TABLA: INVERSION
-- ========================
CREATE TABLE Inversion (
    id_inversion INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_proyecto INT NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL,
    fecha_inversion DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_proyecto) REFERENCES Proyecto(id_proyecto)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ========================
-- TABLA: TRANSACCION
-- ========================
CREATE TABLE Transaccion (
    id_transaccion INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    tipo ENUM('Compra', 'Venta', 'Recompensa', 'Retiro') NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL,
    fecha DATE DEFAULT (CURRENT_DATE),
    descripcion VARCHAR(255),
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ========================
-- TABLA: MONEDA (AURUS)
-- ========================
CREATE TABLE Aurus (
    id_aurus INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    saldo DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ========================
-- TABLA: RETO / LOGRO
-- ========================
CREATE TABLE Reto (
    id_reto INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    recompensa DECIMAL(10,2) NOT NULL
);

-- ========================
-- TABLA: CLIENTE_RETO (N:M)
-- ========================
CREATE TABLE Cliente_Reto (
    id_cliente INT,
    id_reto INT,
    completado BOOLEAN DEFAULT FALSE,
    fecha_completado DATE,
    PRIMARY KEY (id_cliente, id_reto),
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_reto) REFERENCES Reto(id_reto)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ========================
-- TABLA: PUNTUACION DE PROYECTOS
-- ========================
CREATE TABLE Puntuacion (
    id_puntuacion INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_proyecto INT NOT NULL,
    valor TINYINT CHECK (valor BETWEEN 1 AND 5),
    comentario TEXT,
    fecha DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_proyecto) REFERENCES Proyecto(id_proyecto)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ========================
-- TABLA: ADMINISTRADOR
-- ========================
CREATE TABLE Administrador (
    id_admin INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    contraseña VARCHAR(255) NOT NULL
);

-- ========================
-- TABLA: NOTIFICACION
-- ========================
CREATE TABLE Notificacion (
    id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    mensaje VARCHAR(255) NOT NULL,
    fecha DATE DEFAULT (CURRENT_DATE),
    leido BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ======================================================
-- FIN DEL SCRIPT
-- ======================================================
