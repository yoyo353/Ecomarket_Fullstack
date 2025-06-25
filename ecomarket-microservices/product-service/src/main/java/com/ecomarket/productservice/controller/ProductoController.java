package com.ecomarket.productservice.controller;

import com.ecomarket.productservice.model.Producto;
import com.ecomarket.productservice.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    /**
     * GET /api/v1/productos
     * Obtiene la lista completa de productos registrados en el sistema.
     * 
     * @return 200 OK con la lista de productos, 500 si ocurre un error interno.
     * 
     * Ejemplo de respuesta:
     * [
     *   {
     *     "productoId": 1,
     *     "nombreProducto": "Jabón de lavanda orgánico",
     *     "codigoSKU": "ECO-001",
     *     "precioUnitario": 12.99,
     *     "precioCompra": 6.50,
     *     "margenGanancia": 0.5,
     *     "descripcion": "Jabón 100% natural elaborado con aceites esenciales de lavanda.",
     *     "categoriaId": 1,
     *     "proveedorPrincipalId": 1,
     *     "esEcologico": true,
     *     "fechaRegistro": "2025-06-25 10:00:00",
     *     "estado": "ACTIVE"
     *   },
     *   ...
     * ]
     */
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos);
    }
    
    /**
     * GET /api/v1/productos/{id}
     * Busca y retorna un producto específico usando su identificador único.
     * 
     * @param id ID único del producto
     * @return 200 OK con el producto, 404 si no existe
     * 
     * Ejemplo de respuesta:
     * {
     *   "productoId": 1,
     *   "nombreProducto": "Jabón de lavanda orgánico",
     *   ...
     * }
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        Producto producto = productoService.buscarPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /api/v1/productos/sku/{sku}
     * Permite a los clientes encontrar un producto usando su código SKU único.
     * 
     * @param sku Código SKU del producto
     * @return 200 OK con el producto, 404 si no existe
     * 
     * Ejemplo de respuesta:
     * {
     *   "productoId": 1,
     *   "nombreProducto": "Jabón de lavanda orgánico",
     *   ...
     * }
     */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<Producto> obtenerPorSKU(@PathVariable String sku) {
        Producto producto = productoService.buscarPorSKU(sku);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * POST /api/v1/productos
     * Registra un nuevo producto en el catálogo de EcoMarket.
     * 
     * @param producto Datos del nuevo producto
     * @return 201 Created con el producto creado
     * 
     * Ejemplo de request:
     * {
     *   "nombreProducto": "Jabón ecológico de menta",
     *   "codigoSKU": "ECO-999",
     *   "precioUnitario": 15.0,
     *   "precioCompra": 8.0,
     *   "margenGanancia": 0.47,
     *   "descripcion": "Jabón natural y refrescante",
     *   "categoriaId": 2,
     *   "proveedorPrincipalId": 1,
     *   "esEcologico": true,
     *   "fechaRegistro": "2025-06-25 10:00:00",
     *   "estado": "ACTIVE"
     * }
     * 
     * Ejemplo de respuesta:
     * {
     *   "productoId": 51,
     *   "nombreProducto": "Jabón ecológico de menta",
     *   ...
     * }
     */
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }
    
    /**
     * PUT /api/v1/productos/{id}
     * Modifica los datos de un producto existente en el catálogo.
     * 
     * @param id ID del producto a actualizar
     * @param producto Nuevos datos del producto
     * @return 200 OK con el producto actualizado, 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id, @RequestBody Producto producto) {
        Producto productoActualizado = productoService.actualizar(id, producto);
        if (productoActualizado != null) {
            return ResponseEntity.ok(productoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * DELETE /api/v1/productos/{id}
     * Elimina permanentemente un producto del catálogo.
     * 
     * @param id ID del producto a eliminar
     * @return 204 No Content si se elimina, 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        boolean eliminado = productoService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /api/v1/productos/total
     * Retorna la cantidad total de productos registrados en el sistema.
     * 
     * @return 200 OK con el total de productos
     */
    @GetMapping("/total")
    public ResponseEntity<Integer> totalProductos() {
        int total = productoService.totalProductos();
        return ResponseEntity.ok(total);
    }
    
    /**
     * GET /api/v1/productos/ecologicos
     * Retorna todos los productos marcados como ecológicos/sustentables.
     * 
     * @return 200 OK con la lista de productos ecológicos
     */
    @GetMapping("/ecologicos")
    public ResponseEntity<List<Producto>> obtenerProductosEcologicos() {
        List<Producto> ecologicos = productoService.obtenerProductosEcologicos();
        return ResponseEntity.ok(ecologicos);
    }
    
    /**
     * GET /api/v1/productos/categoria/{categoriaId}
     * Filtra y retorna productos pertenecientes a una categoría específica.
     * 
     * @param categoriaId ID de la categoría
     * @return 200 OK con la lista de productos de la categoría
     */
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> obtenerPorCategoria(@PathVariable Integer categoriaId) {
        List<Producto> productos = productoService.obtenerPorCategoria(categoriaId);
        return ResponseEntity.ok(productos);
    }
}
