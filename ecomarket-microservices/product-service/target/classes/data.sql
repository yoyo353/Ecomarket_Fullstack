-- Insertamos productos sin caracteres especiales
INSERT INTO producto (
    codigo_sku, 
    nombre_producto, 
    descripcion, 
    precio_unitario, 
    precio_compra, 
    margen_ganancia, 
    es_ecologico, 
    categoria_id, 
    proveedor_principal_id, 
    fecha_registro, 
    estado
)
VALUES 
('ECO-001', 'Jabon organico de lavanda', 'Jabon 100% natural elaborado con aceites esenciales de lavanda.', 5.99, 2.50, 0.58, true, 1, 1, CURRENT_TIMESTAMP, 'ACTIVE'),
('ECO-002', 'Champu solido de coco', 'Champu en barra sin plastico, ideal para cabello seco.', 8.50, 3.20, 0.62, true, 1, 1, CURRENT_TIMESTAMP, 'ACTIVE'),
('ECO-003', 'Cepillo de dientes de bambu', 'Cepillo de dientes biodegradable con mango de bambu.', 4.95, 1.80, 0.64, true, 2, 2, CURRENT_TIMESTAMP, 'ACTIVE'),
('ECO-004', 'Bolsa reutilizable de algodon', 'Bolsa de compras resistente hecha de algodon organico.', 3.50, 1.20, 0.66, true, 3, 3, CURRENT_TIMESTAMP, 'ACTIVE'),
('ECO-005', 'Pajitas de acero inoxidable', 'Set de 4 pajitas reutilizables con cepillo de limpieza.', 9.99, 4.00, 0.60, true, 2, 2, CURRENT_TIMESTAMP, 'ACTIVE');

