package com.ecomarket.productservice.controller;

import com.ecomarket.productservice.assemblers.ProductoModelAssembler;
import com.ecomarket.productservice.model.Producto;
import com.ecomarket.productservice.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/productos")
@Tag(name = "Productos V2", description = "API con HATEOAS para gestión avanzada de productos ecológicos")
public class ProductoControllerV2 {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler assembler;

    @Operation(
        summary = "Obtener todos los productos con HATEOAS",
        description = "Retorna una lista completa de productos con enlaces de navegación hipermedia"
    )
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    @GetMapping
    public CollectionModel<EntityModel<Producto>> obtenerTodos() {
        List<EntityModel<Producto>> productos = productoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(productos)
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withSelfRel())
                .add(linkTo(ProductoControllerV2.class).withRel("crear").withTitle("Crear producto"))
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerProductosEcologicos()).withRel("ecologicos"))
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerProductosActivos()).withRel("activos"))
                .add(linkTo(methodOn(ProductoControllerV2.class).totalProductos()).withRel("estadisticas"));
    }

    @Operation(
        summary = "Obtener producto por ID con HATEOAS",
        description = "Busca un producto específico y retorna todos sus enlaces relacionados"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> obtenerPorId(
            @Parameter(description = "ID único del producto", example = "1")
            @PathVariable Integer id) {
        Producto producto = productoService.buscarPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(assembler.toModel(producto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Crear nuevo producto con HATEOAS",
        description = "Crea un nuevo producto y retorna el producto creado con enlaces hipermedia"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de producto inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Producto>> crear(
            @Parameter(description = "Datos del producto a crear", required = true)
            @RequestBody Producto producto) {
        Producto savedProducto = productoService.guardar(producto);
        EntityModel<Producto> productoModel = assembler.toModel(savedProducto);
        return ResponseEntity
                .created(productoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(productoModel);
    }

    @Operation(
        summary = "Actualizar producto existente con HATEOAS",
        description = "Actualiza un producto existente y retorna el producto actualizado con enlaces"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de producto inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> actualizar(
            @Parameter(description = "ID del producto a actualizar", example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Nuevos datos del producto", required = true)
            @RequestBody Producto producto) {
        Producto updatedProducto = productoService.actualizar(id, producto);
        if (updatedProducto != null) {
            return ResponseEntity.ok(assembler.toModel(updatedProducto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Eliminar producto",
        description = "Elimina un producto del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del producto a eliminar", example = "1")
            @PathVariable Integer id) {
        if (productoService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // === MÉTODOS DE FILTROS Y BÚSQUEDAS ===

    @Operation(
        summary = "Obtener productos ecológicos",
        description = "Retorna solo los productos marcados como ecológicos/sustentables"
    )
    @GetMapping("/ecologicos")
    public CollectionModel<EntityModel<Producto>> obtenerProductosEcologicos() {
        List<EntityModel<Producto>> productosEcologicos = productoService.obtenerProductosEcologicos().stream()
                .map(assembler::toSimpleModel)
                .collect(Collectors.toList());
        return CollectionModel.of(productosEcologicos)
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerProductosEcologicos()).withSelfRel())
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("todos-productos"))
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerProductosActivos()).withRel("productos-activos"));
    }

    @Operation(
        summary = "Obtener productos activos",
        description = "Retorna solo los productos con estado 'ACTIVE'"
    )
    @GetMapping("/activos")
    public CollectionModel<EntityModel<Producto>> obtenerProductosActivos() {
        List<EntityModel<Producto>> productosActivos = productoService.obtenerTodos().stream()
                .filter(producto -> "ACTIVE".equals(producto.getEstado()))
                .map(assembler::toSimpleModel)
                .collect(Collectors.toList());
        return CollectionModel.of(productosActivos)
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerProductosActivos()).withSelfRel())
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("todos-productos"))
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerProductosEcologicos()).withRel("productos-ecologicos"));
    }

    @Operation(
        summary = "Obtener productos por categoría",
        description = "Retorna todos los productos de una categoría específica"
    )
    @GetMapping("/categoria/{categoriaId}")
    public CollectionModel<EntityModel<Producto>> obtenerPorCategoria(
            @Parameter(description = "ID de la categoría", example = "1")
            @PathVariable Integer categoriaId) {
        List<EntityModel<Producto>> productosPorCategoria = productoService.obtenerPorCategoria(categoriaId).stream()
                .map(assembler::toSimpleModel)
                .collect(Collectors.toList());
        return CollectionModel.of(productosPorCategoria)
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerPorCategoria(categoriaId)).withSelfRel())
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("todos-productos"))
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerProductosEcologicos()).withRel("productos-ecologicos"));
    }

    @Operation(
        summary = "Obtener productos por proveedor",
        description = "Retorna todos los productos de un proveedor específico"
    )
    @GetMapping("/proveedor/{proveedorId}")
    public CollectionModel<EntityModel<Producto>> obtenerPorProveedor(
            @Parameter(description = "ID del proveedor", example = "1")
            @PathVariable Integer proveedorId) {
        List<EntityModel<Producto>> productosPorProveedor = productoService.obtenerTodos().stream()
                .filter(producto -> proveedorId.equals(producto.getProveedorPrincipalId()))
                .map(assembler::toSimpleModel)
                .collect(Collectors.toList());
        return CollectionModel.of(productosPorProveedor)
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerPorProveedor(proveedorId)).withSelfRel())
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("todos-productos"));
    }

    @Operation(
        summary = "Obtener productos por rango de precio",
        description = "Retorna productos dentro de un rango de precio específico"
    )
    @GetMapping("/precio")
    public CollectionModel<EntityModel<Producto>> obtenerPorRangoPrecio(
            @Parameter(description = "Precio mínimo", example = "10.0")
            @RequestParam(name = "min", defaultValue = "0.0") Double precioMin,
            @Parameter(description = "Precio máximo", example = "50.0")
            @RequestParam(name = "max", defaultValue = "1000.0") Double precioMax) {
        List<EntityModel<Producto>> productosPorPrecio = productoService.obtenerPorRangoPrecio(precioMin, precioMax).stream()
                .map(assembler::toSimpleModel)
                .collect(Collectors.toList());
        return CollectionModel.of(productosPorPrecio)
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerPorRangoPrecio(precioMin, precioMax)).withSelfRel())
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("todos-productos"));
    }

    @Operation(
        summary = "Obtener estadísticas de productos",
        description = "Retorna información estadística sobre los productos con enlaces relacionados"
    )
    @GetMapping("/estadisticas")
    public ResponseEntity<EntityModel<EstadisticasProductos>> totalProductos() {
        int total = productoService.totalProductos();
        int ecologicos = productoService.obtenerProductosEcologicos().size();
        int activos = (int) productoService.obtenerTodos().stream()
                .filter(p -> "ACTIVE".equals(p.getEstado()))
                .count();
        EstadisticasProductos stats = new EstadisticasProductos(
                total,
                ecologicos,
                activos,
                total > 0 ? (double) ecologicos / total * 100 : 0,
                total > 0 ? (double) activos / total * 100 : 0
        );
        EntityModel<EstadisticasProductos> statsModel = EntityModel.of(stats)
                .add(linkTo(methodOn(ProductoControllerV2.class).totalProductos()).withSelfRel())
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("todos-productos"))
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerProductosEcologicos()).withRel("productos-ecologicos"))
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerProductosActivos()).withRel("productos-activos"));
        return ResponseEntity.ok(statsModel);
    }

    // Clase interna para estadísticas
    public static class EstadisticasProductos {
        public final int totalProductos;
        public final int productosEcologicos;
        public final int productosActivos;
        public final double porcentajeEcologicos;
        public final double porcentajeActivos;

        public EstadisticasProductos(int totalProductos, int productosEcologicos, int productosActivos, double porcentajeEcologicos, double porcentajeActivos) {
            this.totalProductos = totalProductos;
            this.productosEcologicos = productosEcologicos;
            this.productosActivos = productosActivos;
            this.porcentajeEcologicos = porcentajeEcologicos;
            this.porcentajeActivos = porcentajeActivos;
        }
    }
}
