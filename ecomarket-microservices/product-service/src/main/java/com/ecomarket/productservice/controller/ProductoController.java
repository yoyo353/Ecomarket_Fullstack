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
    
    // Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos);
    }
    
    // Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        Producto producto = productoService.buscarPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Buscar producto por SKU
    @GetMapping("/sku/{sku}")
    public ResponseEntity<Producto> obtenerPorSKU(@PathVariable String sku) {
        Producto producto = productoService.buscarPorSKU(sku);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Crear nuevo producto
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }
    
    // Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id, @RequestBody Producto producto) {
        Producto productoActualizado = productoService.actualizar(id, producto);
        if (productoActualizado != null) {
            return ResponseEntity.ok(productoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        boolean eliminado = productoService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Endpoints adicionales
    
    // Total de productos
    @GetMapping("/total")
    public ResponseEntity<Integer> totalProductos() {
        int total = productoService.totalProductos();
        return ResponseEntity.ok(total);
    }
    
    // Productos ecológicos
    @GetMapping("/ecologicos")
    public ResponseEntity<List<Producto>> obtenerProductosEcologicos() {
        List<Producto> ecologicos = productoService.obtenerProductosEcologicos();
        return ResponseEntity.ok(ecologicos);
    }
    
    // Productos por categoría
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> obtenerPorCategoria(@PathVariable Integer categoriaId) {
        List<Producto> productos = productoService.obtenerPorCategoria(categoriaId);
        return ResponseEntity.ok(productos);
    }
}
