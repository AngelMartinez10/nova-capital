DROP DATABASE IF EXISTS NovaCapital;
CREATE DATABASE NovaCapital CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE NovaCapital;

CREATE TABLE Administrador (
    id_admin        INT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(100)  NOT NULL,
    email           VARCHAR(150)  UNIQUE NOT NULL,
    contrasena      VARCHAR(255)  NOT NULL,
    fecha_registro  DATETIME      DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Cliente (
    id_cliente      INT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(100)  NOT NULL,
    apellidos       VARCHAR(150)  NOT NULL,
    email           VARCHAR(150)  UNIQUE NOT NULL,
    contrasena      VARCHAR(255)  NOT NULL,
    telefono        VARCHAR(20),
    activo          BOOLEAN       DEFAULT TRUE,
    fecha_registro  DATETIME      DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Aurus (
    id_aurus    INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente  INT           NOT NULL UNIQUE,
    saldo       DECIMAL(12,2) NOT NULL DEFAULT 1000.00,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Proyecto (
    id_proyecto         INT AUTO_INCREMENT PRIMARY KEY,
    nombre              VARCHAR(150)  NOT NULL,
    descripcion         TEXT,
    categoria           VARCHAR(100),
    objetivo_inversion  DECIMAL(12,2) NOT NULL CHECK (objetivo_inversion > 0),
    cantidad_actual     DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    porcentaje          DECIMAL(5,2)  GENERATED ALWAYS AS
                            (ROUND((cantidad_actual / objetivo_inversion) * 100, 2)) STORED,
    id_cliente          INT,
    estado              ENUM('ACTIVO','EN_PROGRESO','FINANCIADO','CANCELADO') DEFAULT 'ACTIVO',
    fecha_creacion      DATETIME      DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
        ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE Inversion (
    id_inversion    INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente      INT           NOT NULL,
    id_proyecto     INT           NOT NULL,
    cantidad        DECIMAL(10,2) NOT NULL CHECK (cantidad > 0),
    fecha_inversion DATETIME      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_proyecto) REFERENCES Proyecto(id_proyecto)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Transaccion (
    id_transaccion  INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente      INT           NOT NULL,
    tipo            ENUM('INVERSION','RECOMPENSA','RETIRO','DEPOSITO') NOT NULL,
    cantidad        DECIMAL(10,2) NOT NULL,
    saldo_anterior  DECIMAL(12,2) NOT NULL,
    saldo_posterior DECIMAL(12,2) NOT NULL,
    descripcion     VARCHAR(255),
    fecha           DATETIME      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Reto (
    id_reto     INT AUTO_INCREMENT PRIMARY KEY,
    titulo      VARCHAR(150)  NOT NULL,
    descripcion TEXT,
    recompensa  DECIMAL(10,2) NOT NULL CHECK (recompensa > 0),
    activo      BOOLEAN       DEFAULT TRUE
);

CREATE TABLE Cliente_Reto (
    id_cliente        INT      NOT NULL,
    id_reto           INT      NOT NULL,
    completado        BOOLEAN  DEFAULT FALSE,
    fecha_completado  DATETIME,
    PRIMARY KEY (id_cliente, id_reto),
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_reto) REFERENCES Reto(id_reto)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Puntuacion (
    id_puntuacion  INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente     INT      NOT NULL,
    id_proyecto    INT      NOT NULL,
    valor          INT      NOT NULL CHECK (valor BETWEEN 1 AND 5),
    comentario     TEXT,
    fecha          DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_cliente_proyecto (id_cliente, id_proyecto),
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_proyecto) REFERENCES Proyecto(id_proyecto)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Notificacion (
    id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente      INT          NOT NULL,
    mensaje         VARCHAR(255) NOT NULL,
    leido           BOOLEAN      DEFAULT FALSE,
    fecha           DATETIME     DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_proyecto_estado     ON Proyecto(estado);
CREATE INDEX idx_proyecto_cliente    ON Proyecto(id_cliente);
CREATE INDEX idx_inversion_cliente   ON Inversion(id_cliente);
CREATE INDEX idx_inversion_proyecto  ON Inversion(id_proyecto);
CREATE INDEX idx_transaccion_cliente ON Transaccion(id_cliente);
CREATE INDEX idx_notif_cliente_leido ON Notificacion(id_cliente, leido);

INSERT INTO Reto (titulo, descripcion, recompensa) VALUES
('Primera inversion',      'Realiza tu primera inversion en cualquier proyecto', 50.00),
('Creador de proyectos',   'Crea tu primer proyecto de inversion',               75.00),
('Inversor diversificado', 'Invierte en 3 proyectos distintos',                  150.00),
('Proyecto financiado',    'Financia completamente un proyecto',                 200.00),
('Inversor veterano',      'Realiza 10 inversiones en total',                    300.00);