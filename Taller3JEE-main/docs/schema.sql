-- Schema SQL para MySQL (Producción)
-- Taller3JEE - Sistema de Gestión de Curso de Estructuras de Datos

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS taller3jee;
USE taller3jee;

-- Tabla de Temas
CREATE TABLE tema (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    descripcion LONGTEXT,
    tipo VARCHAR(50) NOT NULL,  -- 'TEORICA' o 'PRACTICA'
    numero_orden INT NOT NULL UNIQUE,
    objetivos LONGTEXT,
    duracion_estimada INT,  -- en minutos
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_numero_orden (numero_orden),
    INDEX idx_tipo (tipo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Estudiantes
CREATE TABLE estudiante (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    matricula VARCHAR(50) UNIQUE,
    estado VARCHAR(50) NOT NULL DEFAULT 'ACTIVO',  -- 'ACTIVO', 'INACTIVO', 'SUSPENDIDO'
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_email (email),
    INDEX idx_matricula (matricula),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Progreso del Estudiante
CREATE TABLE progreso_estudiante (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    estudiante_id BIGINT NOT NULL,
    tema_id BIGINT NOT NULL,
    nivel_comprension INT NOT NULL DEFAULT 0,  -- 0-100
    intentos INT NOT NULL DEFAULT 0,
    completado BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_inicio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_completacion TIMESTAMP DEFAULT NULL,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_estudiante_tema (estudiante_id, tema_id),
    CONSTRAINT fk_estudiante FOREIGN KEY (estudiante_id) REFERENCES estudiante(id) ON DELETE CASCADE,
    CONSTRAINT fk_tema FOREIGN KEY (tema_id) REFERENCES tema(id) ON DELETE CASCADE,
    
    INDEX idx_estudiante (estudiante_id),
    INDEX idx_tema (tema_id),
    INDEX idx_completado (completado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Recomendaciones
CREATE TABLE recomendacion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    estudiante_id BIGINT NOT NULL,
    tema_actual_id BIGINT DEFAULT NULL,
    tema_recomendado_id BIGINT NOT NULL,
    tipo VARCHAR(50) NOT NULL,  -- 'REFUERZO' o 'AVANCE'
    razon VARCHAR(500),
    nivel_confianza DECIMAL(3,2) NOT NULL DEFAULT 0.50,  -- 0.0 - 1.0
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_estudiante_rec FOREIGN KEY (estudiante_id) REFERENCES estudiante(id) ON DELETE CASCADE,
    CONSTRAINT fk_tema_actual FOREIGN KEY (tema_actual_id) REFERENCES tema(id) ON DELETE SET NULL,
    CONSTRAINT fk_tema_recomendado FOREIGN KEY (tema_recomendado_id) REFERENCES tema(id) ON DELETE CASCADE,
    
    INDEX idx_estudiante_rec (estudiante_id),
    INDEX idx_fecha_creacion (fecha_creacion),
    INDEX idx_tipo (tipo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Auditoría (opcional, para tracking de acciones)
CREATE TABLE auditoria (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    entidad VARCHAR(100) NOT NULL,
    accion VARCHAR(50) NOT NULL,  -- 'CREATE', 'UPDATE', 'DELETE'
    id_entidad BIGINT,
    usuario VARCHAR(100),
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    detalles LONGTEXT,
    
    INDEX idx_entidad (entidad),
    INDEX idx_fecha (fecha)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Vistas útiles

-- Vista: Progreso resumido por estudiante
CREATE VIEW v_progreso_estudiante_resumen AS
SELECT 
    e.id,
    e.nombre,
    e.email,
    COUNT(DISTINCT p.tema_id) as temas_iniciados,
    SUM(CASE WHEN p.completado = 1 THEN 1 ELSE 0 END) as temas_completados,
    COUNT(DISTINCT t.id) - COUNT(DISTINCT p.tema_id) as temas_pendientes,
    ROUND(AVG(p.nivel_comprension), 2) as promedio_comprension,
    ROUND(SUM(CASE WHEN p.completado = 1 THEN 1 ELSE 0 END) * 100.0 / COUNT(DISTINCT t.id), 2) as porcentaje_completacion
FROM estudiante e
CROSS JOIN tema t
LEFT JOIN progreso_estudiante p ON e.id = p.estudiante_id AND t.id = p.tema_id
WHERE e.estado = 'ACTIVO'
GROUP BY e.id, e.nombre, e.email;

-- Vista: Temas por estudiante ordenados
CREATE VIEW v_temas_progreso AS
SELECT 
    e.id as estudiante_id,
    e.nombre as estudiante_nombre,
    t.id as tema_id,
    t.numero_orden,
    t.titulo,
    t.tipo,
    COALESCE(p.nivel_comprension, 0) as nivel_comprension,
    COALESCE(p.intentos, 0) as intentos,
    COALESCE(p.completado, FALSE) as completado,
    p.fecha_actualizacion
FROM estudiante e
CROSS JOIN tema t
LEFT JOIN progreso_estudiante p ON e.id = p.estudiante_id AND t.id = p.tema_id
WHERE e.estado = 'ACTIVO'
ORDER BY e.id, t.numero_orden;

-- Stored Procedures (opcional, para operaciones más complejas)

-- Procedure: Calcular recomendación automática
DELIMITER //
CREATE PROCEDURE sp_generar_recomendacion(IN p_estudiante_id BIGINT)
BEGIN
    DECLARE v_comprension_actual INT;
    DECLARE v_tema_actual_id BIGINT;
    DECLARE v_tema_recomendado_id BIGINT;
    DECLARE v_tipo VARCHAR(50);
    DECLARE v_razon VARCHAR(500);
    DECLARE v_confianza DECIMAL(3,2);
    
    -- Obtener tema actual del estudiante
    SELECT tema_id, nivel_comprension INTO v_tema_actual_id, v_comprension_actual
    FROM progreso_estudiante
    WHERE estudiante_id = p_estudiante_id AND completado = FALSE
    ORDER BY fecha_actualizacion DESC
    LIMIT 1;
    
    -- Si no hay tema actual, obtener primer tema
    IF v_tema_actual_id IS NULL THEN
        SET v_tema_actual_id = NULL;
        SELECT id INTO v_tema_recomendado_id FROM tema WHERE numero_orden = 1 LIMIT 1;
        SET v_tipo = 'AVANCE';
        SET v_razon = 'Iniciando el curso. Comienza con el primer tema.';
        SET v_confianza = 1.0;
    ELSE
        -- Generar recomendación basada en comprensión
        IF v_comprension_actual < 70 THEN
            SET v_tipo = 'REFUERZO';
            SET v_razon = CONCAT('Comprensión insuficiente (', v_comprension_actual, '%). Se recomienda refuerzo.');
            SET v_confianza = 0.8;
            SET v_tema_recomendado_id = v_tema_actual_id;
        ELSEIF v_comprension_actual < 80 THEN
            SET v_tipo = 'AVANCE';
            SET v_razon = CONCAT('Comprensión aceptable (', v_comprension_actual, '%). Puedes continuar con el siguiente tema.');
            SET v_confianza = 0.7;
            SELECT id INTO v_tema_recomendado_id FROM tema 
            WHERE numero_orden > (SELECT numero_orden FROM tema WHERE id = v_tema_actual_id)
            ORDER BY numero_orden ASC LIMIT 1;
        ELSE
            SET v_tipo = 'AVANCE';
            SET v_razon = CONCAT('Comprensión excelente (', v_comprension_actual, '%). Tema completado. Listo para el siguiente.');
            SET v_confianza = 0.95;
            SELECT id INTO v_tema_recomendado_id FROM tema 
            WHERE numero_orden > (SELECT numero_orden FROM tema WHERE id = v_tema_actual_id)
            ORDER BY numero_orden ASC LIMIT 1;
        END IF;
    END IF;
    
    -- Insertar recomendación
    INSERT INTO recomendacion (estudiante_id, tema_actual_id, tema_recomendado_id, tipo, razon, nivel_confianza)
    VALUES (p_estudiante_id, v_tema_actual_id, v_tema_recomendado_id, v_tipo, v_razon, v_confianza);
END //
DELIMITER ;

-- Índices adicionales para performance
CREATE INDEX idx_progreso_estudiante_completado ON progreso_estudiante(estudiante_id, completado);
CREATE INDEX idx_progreso_actualizacion ON progreso_estudiante(fecha_actualizacion);
CREATE INDEX idx_recomendacion_estudiante_fecha ON recomendacion(estudiante_id, fecha_creacion);

-- Estadísticas iniciales de seguridad
-- Crear usuario específico para aplicación (MySQL)
-- CREATE USER 'taller3jee'@'localhost' IDENTIFIED BY 'SecurePassword123!';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON taller3jee.* TO 'taller3jee'@'localhost';
-- FLUSH PRIVILEGES;

-- Scripts de mantenimiento
-- OPTIMIZE TABLE tema;
-- OPTIMIZE TABLE estudiante;
-- OPTIMIZE TABLE progreso_estudiante;
-- OPTIMIZE TABLE recomendacion;
