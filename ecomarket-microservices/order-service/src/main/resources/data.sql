
/*comentado por ahora para que funcione el datafaker
-- Insertamos pedidos de ejemplo

INSERT INTO pedido (
    fecha_de_pedido, 
    cliente_id, 
    estado, 
    descuento, 
    metodo_pago_id, 
    usuario_id, 
    subtotal, 
    total, 
    direccion_envio, 
    ciudad_envio, 
    notas
) VALUES 
('2025-06-01', 1, 'COMPLETADO', 5.0, 1, 1, 15.49, 14.72, 'Av. Providencia 1234', 'Santiago', 'Entrega en horario de oficina'),
('2025-06-02', 2, 'PENDIENTE', 0.0, 2, 2, 8.50, 8.50, 'Calle Las Flores 567', 'Valparaiso', 'Llamar antes de entregar'),
('2025-06-03', 3, 'EN_PROCESO', 10.0, 1, 1, 13.45, 12.11, 'Pasaje Los Pinos 890', 'Concepcion', 'Dejar en conserjeria'),
('2025-06-04', 1, 'COMPLETADO', 0.0, 3, 3, 9.99, 9.99, 'Av. Providencia 1234', 'Santiago', 'Segunda compra del cliente'),
('2025-06-05', 4, 'PENDIENTE', 15.0, 2, 2, 18.44, 15.67, 'Los Robles 456', 'La Serena', 'Cliente VIP - descuento especial');

-- Insertamos detalles de pedidos

-- Detalles del pedido 1
INSERT INTO detalle_de_pedido (pedido_id, producto_id, precio_unitario, cantidad) VALUES 
(1, 1, 5.99, 2),  -- 2 jabones de lavanda
(1, 3, 4.95, 1);  -- 1 cepillo de bambu

-- Detalles del pedido 2  
INSERT INTO detalle_de_pedido (pedido_id, producto_id, precio_unitario, cantidad) VALUES 
(2, 2, 8.50, 1);  -- 1 champu solido

-- Detalles del pedido 3
INSERT INTO detalle_de_pedido (pedido_id, producto_id, precio_unitario, cantidad) VALUES 
(3, 1, 5.99, 1),  -- 1 jabon de lavanda
(3, 4, 3.50, 2);  -- 2 bolsas de algodon

-- Detalles del pedido 4
INSERT INTO detalle_de_pedido (pedido_id, producto_id, precio_unitario, cantidad) VALUES 
(4, 5, 9.99, 1);  -- 1 set de pajitas

-- Detalles del pedido 5
INSERT INTO detalle_de_pedido (pedido_id, producto_id, precio_unitario, cantidad) VALUES 
(5, 2, 8.50, 1),  -- 1 champu solido
(5, 5, 9.99, 1);  -- 1 set de pajitas

*/