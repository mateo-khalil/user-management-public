-- Create the schema
CREATE SCHEMA IF NOT EXISTS asur_db;

-- Use the schema
SET SCHEMA asur_db;

-- Create the `espacios` table
CREATE TABLE IF NOT EXISTS espacios
(
    id        IDENTITY PRIMARY KEY,
    nombre    VARCHAR(255) NOT NULL,
    capacidad INT          NOT NULL,
    ubicacion VARCHAR(255) NOT NULL,
    activo    BOOLEAN      NOT NULL
);

-- Insert data into `espacios`
MERGE INTO espacios (nombre, capacidad, ubicacion, activo)
    KEY (nombre)
    VALUES ('Aula Magna', 200, 'Edificio Principal, Piso 2', 1),
           ('Cancha de Futbol', 500, 'Área Deportiva', 1),
           ('Auditorio', 300, 'Edificio B, Piso 1', 1),
           ('Sala de Reuniones', 20, 'Edificio Principal, Piso 1', 1),
           ('Laboratorio de Computo', 50, 'Edificio C, Piso 3', 1),
           ('Área Verde', 1000, 'Jardines Exteriores', 1),
           ('Biblioteca', 150, 'Edificio Principal, Piso 3', 1);

-- Create the `tipos_actividad` table
CREATE TABLE IF NOT EXISTS tipos_actividad
(
    id          IDENTITY PRIMARY KEY,
    nombre      VARCHAR(255) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NOT NULL,
    activo      BOOLEAN      NOT NULL
);

-- Insert data into `tipos_actividad`
MERGE INTO tipos_actividad (nombre, descripcion, activo)
    KEY (nombre)
    VALUES ('CHARLAS', 'Actividades de tipo charla, conferencias y seminarios', 1),
           ('RECREATIVAS', 'Actividades recreativas y de entretenimiento', 1),
           ('DEPORTIVAS', 'Actividades deportivas y de ejercicio físico', 1),
           ('SOCIALES', 'Actividades sociales y de integración', 1);

-- Create the `usuarios` table
CREATE TABLE IF NOT EXISTS usuarios
(
    id                      IDENTITY PRIMARY KEY,
    nombres                 VARCHAR(255) NOT NULL,
    apellidos               VARCHAR(255) NOT NULL,
    documento               BIGINT       NOT NULL,
    tipo_documento          VARCHAR(50)  NOT NULL,
    fecha_nacimiento        DATE         NOT NULL,
    domicilio               VARCHAR(255),
    telefono1               VARCHAR(50),
    telefono2               VARCHAR(50),
    email                   VARCHAR(255) NOT NULL UNIQUE,
    password                VARCHAR(255) NOT NULL,
    role                    VARCHAR(50)  NOT NULL,
    estado                  VARCHAR(50)  NOT NULL,
    categoria_socio         VARCHAR(50),
    dificultad_auditiva     BOOLEAN      NOT NULL,
    maneja_lenguaje_senias  BOOLEAN      NOT NULL,
    participa_subcomision   BOOLEAN      NOT NULL,
    descripcion_subcomision VARCHAR(255)
);

-- Insert data into `usuarios`
MERGE INTO usuarios (nombres, apellidos, documento, tipo_documento, fecha_nacimiento, domicilio, telefono1, telefono2,
                     email, password, role, estado, categoria_socio, dificultad_auditiva, maneja_lenguaje_senias,
                     participa_subcomision, descripcion_subcomision)
    KEY (email)
    VALUES ('Juan',
            'Pérez',
            12345678,
            'DNI',
            '1990-01-01',
            'Calle Falsa 123',
            '123456789',
            NULL,
            'hola@mail.com',
            'hola',
            'ADMINISTRADOR',
            'VALIDADO',
            NULL,
            0,
            0,
            0,
            NULL);
