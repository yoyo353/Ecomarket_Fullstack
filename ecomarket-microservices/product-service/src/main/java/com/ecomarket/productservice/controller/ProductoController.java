package com.ecomarket.productservice.controller;

import com.ecomarket.productservice.model.Producto;
import com.ecomarket.productservice.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "API para la gestión de productos ecológicos")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    // Obtener todos los productos
    @Operation(
        summary = "Obtener todos los productos",
        description = "Retorna una lista completa de todos los productos disponibles en el catálogo"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de productos obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Producto.class)
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor"
        )
    })
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos);
    }
    
    // Obtener producto por ID
    @Operation(
        summary = "Obtener producto por ID",
        description = "Busca y retorna un producto específico usando su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Producto encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Producto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Producto no encontrado"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(
            @Parameter(description = "ID único del producto", example = "1")
            @PathVariable Integer id) {
        Producto producto = productoService.buscarPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Buscar producto por SKU
    @Operation(
        summary = "Buscar producto por SKU",
        description = "Encuentra un producto usando su código SKU único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Producto encontrado por SKU",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Producto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Producto no encontrado con ese SKU"
        )
    })
    @GetMapping("/sku/{sku}")
    public ResponseEntity<Producto> obtenerPorSKU(
            @Parameter(description = "Código SKU del producto", example = "ECO-001")
            @PathVariable String sku) {
        Producto producto = productoService.buscarPorSKU(sku);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Crear nuevo producto
    @Operation(
        summary = "Crear nuevo producto",
        description = "Registra un nuevo producto en el catálogo de EcoMarket"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Producto creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Producto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de entrada inválidos"
        )
    })
    @PostMapping
    public ResponseEntity<Producto> crear(
            @Parameter(description = "Datos del nuevo producto")
            @RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }
    
    // Actualizar producto
    @Operation(
        summary = "Actualizar producto existente",
        description = "Modifica los datos de un producto existente en el catálogo"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Producto actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Producto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Producto no encontrado para actualizar"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @Parameter(description = "ID del producto a actualizar", example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Nuevos datos del producto")
            @RequestBody Producto producto) {
        Producto productoActualizado = productoService.actualizar(id, producto);
        if (productoActualizado != null) {
            return ResponseEntity.ok(productoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Eliminar producto
    @Operation(
        summary = "Eliminar producto",
        description = "Elimina permanentemente un producto del catálogo"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Producto eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Producto no encontrado para eliminar"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del producto a eliminar", example = "1")
            @PathVariable Integer id) {
        boolean eliminado = productoService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Endpoints adicionales
    
    // Total de productos
    @Operation(
        summary = "Obtener total de productos",
        description = "Retorna la cantidad total de productos registrados en el sistema"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Total de productos obtenido exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Integer.class)
        )
    )
    @GetMapping("/total")
    public ResponseEntity<Integer> totalProductos() {
        int total = productoService.totalProductos();
        return ResponseEntity.ok(total);
    }
    
    // Productos ecológicos
    @Operation(
        summary = "Obtener productos ecológicos",
        description = "Retorna todos los productos marcados como ecológicos/sustentables"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Lista de productos ecológicos obtenida exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Producto.class)
        )
    )
    @GetMapping("/ecologicos")
    public ResponseEntity<List<Producto>> obtenerProductosEcologicos() {
        List<Producto> ecologicos = productoService.obtenerProductosEcologicos();
        return ResponseEntity.ok(ecologicos);
    }
    
    // Productos por categoría
    @Operation(
        summary = "Obtener productos por categoría",
        description = "Filtra y retorna productos pertenecientes a una categoría específica"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Productos de la categoría obtenidos exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Producto.class)
        )
    )
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> obtenerPorCategoria(
            @Parameter(description = "ID de la categoría", example = "1")
            @PathVariable Integer categoriaId) {
        List<Producto> productos = productoService.obtenerPorCategoria(categoriaId);
        return ResponseEntity.ok(productos);
    }
}
