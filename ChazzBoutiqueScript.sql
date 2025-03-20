use chazzboutique;
INSERT INTO tblUsuario (nombreUsuario, contraseña, rol, activo, fechaCreacion) 
VALUES ('admin', 'admin123', 'Administrador', TRUE, '2025-03-20');

INSERT INTO tblProveedor (nombre, telefono, correo, direccion, fechaCreacion) 
VALUES 
('Textiles Luna', '555-1234', 'contacto@textilesluna.com', 'Av. Principal #123', '2025-03-19'),
('Denim Pro', '555-5678', 'ventas@denimpro.com', 'Calle Secundaria #456', '2025-03-19'),
('SportMax', '555-9876', 'info@sportmax.com', 'Boulevard Deportivo #789', '2025-03-19');

INSERT INTO tblProducto (nombreProducto, descripcionProducto, fechaCreacion, proveedor_id) 
VALUES 
('Camiseta Básica', 'Camiseta de algodón 100%, color blanco', '2025-03-19', 1),
('Jeans Clásicos', 'Pantalón de mezclilla azul, ajuste regular', '2025-03-19', 2),
('Zapatillas Deportivas', 'Calzado ligero para correr, color negro', '2025-03-19', 3);

INSERT INTO tblVarianteProducto (codigoBarra, stock, precioCompra, talla, precioVenta, producto_id) 
VALUES 
('7501000123463', 40, 120.00, 'S', 250.00, 1), -- Camiseta Negra (RGB: 0,0,0)
('7501000789118', 25, 260.00, '34', 520.00, 2), -- Jeans Grises (RGB: 128,128,128)
('7501000456796', 15, 420.00, '40', 850.00, 3), -- Zapatillas Blancas (RGB: 255,255,255)
('7501000123470', 60, 130.00, 'L', 270.00, 1), -- Camiseta Roja (RGB: 255,0,0)
('7501000789125', 35, 240.00, '30', 480.00, 2), -- Jeans Negros (RGB: 0,0,0)
('7501000456802', 10, 450.00, '41', 900.00, 3), -- Zapatillas Azules (RGB: 0,0,255)
('7501000123487', 55, 140.00, 'XL', 290.00, 1), -- Camiseta Verde (RGB: 0,255,0)
('7501000789132', 20, 270.00, '36', 540.00, 2), -- Jeans Rotos (RGB: 255,255,0)
('7501000456819', 5, 460.00, '39', 920.00, 3); -- Zapatillas Rojas (RGB: 255,0,0)



