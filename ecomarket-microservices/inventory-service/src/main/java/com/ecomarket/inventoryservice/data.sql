INSERT INTO inventario (
    producto_id, 
    cantidad_disponible, 
    cantidad_minima, 
    bodega_id, 
    fecha_actualizacion, 
    estado
) VALUES (
    1, 100, 10, 1, CURRENT_TIMESTAMP, 'ACTIVO'
);
