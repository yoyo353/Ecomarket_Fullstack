package com.ecomarket.productservice.controller;

import com.ecomarket.productservice.assemblers.ProductoModelAssembler;
import com.ecomarket.productservice.model.Producto;
import com.ecomarket.productservice.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/productos")
@Tag(name = "Productos V2", description = "API con HATEOAS para productos ecológicos")
public class ProductoControllerV2 {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler assembler;

    @Operation(summary = "Obtener todos los productos con HATEOAS")
    @GetMapping
    public CollectionModel<EntityModel<Producto>> obtenerTodos() {
        List<EntityModel<Producto>> productos = productoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(productos)
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withSelfRel())
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerProductosEcologicos()).withRel("ecologicos"))
                .add(linkTo(methodOn(ProductoControllerV2.class).totalProductos()).withRel("total"));
    }

    @Operation(summary = "Obtener producto por ID con HATEOAS")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> obtenerPorId(@PathVariable Integer id) {
        Producto producto = productoService.buscarPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(assembler.toModel(producto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Buscar producto por SKU con HATEOAS")
    @GetMapping("/sku/{sku}")
    public ResponseEntity<EntityModel<Producto>> obtenerPorSKU(@PathVariable String sku) {
        Producto producto = productoService.buscarPorSKU(sku);
        if (producto != null) {
            return ResponseEntity.ok(assembler.toModel(producto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear nuevo producto con HATEOAS")
    @PostMapping
    public ResponseEntity<EntityModel<Producto>> crear(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(nuevoProducto));
    }

    @Operation(summary = "Actualizar producto con HATEOAS")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> actualizar(@PathVariable Integer id, @RequestBody Producto producto) {
        Producto productoActualizado = productoService.actualizar(id, producto);
        if (productoActualizado != null) {
            return ResponseEntity.ok(assembler.toModel(productoActualizado));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        boolean eliminado = productoService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Total de productos")
    @GetMapping("/total")
    public ResponseEntity<EntityModel<Integer>> totalProductos() {
        int total = productoService.totalProductos();
        
        return ResponseEntity.ok(
                EntityModel.of(total)
                        .add(linkTo(methodOn(ProductoControllerV2.class).totalProductos()).withSelfRel())
                        .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("productos"))
        );
    }

    @Operation(summary = "Obtener productos ecológicos con HATEOAS")
    @GetMapping("/ecologicos")
    public CollectionModel<EntityModel<Producto>> obtenerProductosEcologicos() {
        List<EntityModel<Producto>> ecologicos = productoService.obtenerProductosEcologicos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(ecologicos)
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerProductosEcologicos()).withSelfRel())
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("todos"));
    }

    @Operation(summary = "Obtener productos por categoría con HATEOAS")
    @GetMapping("/categoria/{categoriaId}")
    public CollectionModel<EntityModel<Producto>> obtenerPorCategoria(
            @Parameter(description = "ID de la categoría")
            @PathVariable Integer categoriaId) {
        
        List<EntityModel<Producto>> productos = productoService.obtenerPorCategoria(categoriaId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(productos)
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerPorCategoria(categoriaId)).withSelfRel())
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("todos"));
    }

    @Operation(summary = "Obtener productos por proveedor y tipo ecológico")
    @GetMapping("/proveedor/{proveedorId}/ecologico/{esEcologico}")
    public CollectionModel<EntityModel<Producto>> obtenerPorProveedorYEcologico(
            @PathVariable Integer proveedorId,
            @PathVariable Boolean esEcologico) {
        List<EntityModel<Producto>> productos = productoService.obtenerPorProveedorYEcologico(proveedorId, esEcologico).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(productos)
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerPorProveedorYEcologico(proveedorId, esEcologico)).withSelfRel())
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("todos"));
    }

    @Operation(summary = "Obtener productos por rango de precio")
    @GetMapping("/precio/{precioMin}/{precioMax}")
    public CollectionModel<EntityModel<Producto>> obtenerPorRangoPrecio(
            @PathVariable Double precioMin,
            @PathVariable Double precioMax) {
        List<EntityModel<Producto>> productos = productoService.obtenerPorRangoPrecio(precioMin, precioMax).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(productos)
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerPorRangoPrecio(precioMin, precioMax)).withSelfRel())
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("todos"));
    }

    @Operation(summary = "Buscar productos por nombre")
    @GetMapping("/buscar/{nombre}")
    public CollectionModel<EntityModel<Producto>> buscarPorNombre(@PathVariable String nombre) {
        List<EntityModel<Producto>> productos = productoService.buscarPorNombre(nombre).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(productos)
                .add(linkTo(methodOn(ProductoControllerV2.class).buscarPorNombre(nombre)).withSelfRel())
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("todos"));
    }

    @Operation(summary = "Contar productos ecológicos")
    @GetMapping("/estadisticas/ecologicos")
    public ResponseEntity<EntityModel<Long>> contarProductosEcologicos() {
        Long count = productoService.contarProductosEcologicos();
        return ResponseEntity.ok(
                EntityModel.of(count)
                        .add(linkTo(methodOn(ProductoControllerV2.class).contarProductosEcologicos()).withSelfRel())
                        .add(linkTo(methodOn(ProductoControllerV2.class).obtenerProductosEcologicos()).withRel("productos-ecologicos"))
        );
    }

    @Operation(summary = "Obtener productos más caros que un precio")
    @GetMapping("/precio-mayor/{precio}")
    public CollectionModel<EntityModel<Producto>> obtenerPorPrecioMayorA(@PathVariable Double precio) {
        List<EntityModel<Producto>> productos = productoService.obtenerPorPrecioMayorA(precio).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(productos)
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerPorPrecioMayorA(precio)).withSelfRel())
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("todos"));
    }
}
