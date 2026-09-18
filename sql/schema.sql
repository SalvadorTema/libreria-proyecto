-- Base de Datos para el proyecto de Librería (Variante B)
CREATE DATABASE IF NOT EXISTS libreria_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE libreria_db;

-- Tabla: libros
-- Decisión de diseño y justificación de tipos de dato:
-- 1. id: INT AUTO_INCREMENT PRIMARY KEY para asegurar identificación única automática.
-- 2. titulo: VARCHAR(150) NOT NULL. Se usan 150 caracteres para soportar títulos largos y subtítulos. No puede ser NULL por regla de negocio.
-- 3. autor: VARCHAR(100) NOT NULL. 100 caracteres son suficientes para nombres completos de autores. No puede ser NULL por regla de negocio.
-- 4. categoria: VARCHAR(50) NOT NULL. Texto libre para clasificaciones como Novela, Técnico, Infantil, etc.
-- 5. precio: DECIMAL(10,2) NOT NULL. Se utiliza DECIMAL para evitar errores de redondeo numérico flotante en montos monetarios (ej. Quetzales Q145.00).
-- 6. existencias: INT NOT NULL DEFAULT 0. Cantidad entera de inventario, por defecto 0 (agotado).
-- 7. anio_publicacion: INT NOT NULL. Guarda el año de publicación (ej. 1967).

CREATE TABLE IF NOT EXISTS libros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    existencias INT NOT NULL DEFAULT 0,
    anio_publicacion INT NOT NULL,
    CONSTRAINT chk_precio_positivo CHECK (precio > 0),
    CONSTRAINT chk_existencias_no_negativas CHECK (existencias >= 0)
) ENGINE=InnoDB;

-- Datos de prueba para verificación inicial
INSERT INTO libros (titulo, autor, categoria, precio, existencias, anio_publicacion) VALUES
('Cien años de soledad', 'Gabriel Garcia Marquez', 'Novela', 145.00, 12, 1967),
('Clean Code', 'Robert C. Martin', 'Tecnico', 220.50, 5, 2008),
('El principito', 'Antoine de Saint-Exupéry', 'Infantil', 85.00, 0, 1943);