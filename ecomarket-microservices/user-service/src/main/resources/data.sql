-- data.sql para user-service

-- Insertamos usuarios iniciales
INSERT INTO usuario (
    nombre_usuario, 
    apellido, 
    correo_usuario, 
    contraseña, 
    rol_id, 
    tienda_id, 
    fecha_registro, 
    estado, 
    ultimo_acceso
)
VALUES 
('Juan', 'Perez', 'juan.perez@ecomarket.cl', 'password123', 1, 1, CURRENT_TIMESTAMP, 'ACTIVO', CURRENT_TIMESTAMP),
('Maria', 'Garcia', 'maria.garcia@ecomarket.cl', 'password123', 2, 1, CURRENT_TIMESTAMP, 'ACTIVO', CURRENT_TIMESTAMP),
('Carlos', 'Rodriguez', 'carlos.rodriguez@ecomarket.cl', 'password123', 3, 1, CURRENT_TIMESTAMP, 'ACTIVO', CURRENT_TIMESTAMP),
('Ana', 'Martinez', 'ana.martinez@ecomarket.cl', 'password123', 2, 2, CURRENT_TIMESTAMP, 'ACTIVO', CURRENT_TIMESTAMP),
('Pedro', 'Lopez', 'pedro.lopez@ecomarket.cl', 'password123', 4, 1, CURRENT_TIMESTAMP, 'ACTIVO', CURRENT_TIMESTAMP);
