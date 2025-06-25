package com.ecomarket.productservice.assemblers;

import com.ecomarket.productservice.controller.ProductoControllerV2;
import com.ecomarket.productservice.model.Producto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {

    @Override
    public @NonNull EntityModel<Producto> toModel(@NonNull Producto producto) {
        List<Link> links = new ArrayList<>();
        // Link a sí mismo (self)
        links.add(linkTo(methodOn(ProductoControllerV2.class)
                .obtenerPorId(producto.getProductoId())).withSelfRel());
        // Link a la colección de productos
        links.add(linkTo(ProductoControllerV2.class).withRel("productos"));
        // Link para actualizar producto
        links.add(linkTo(methodOn(ProductoControllerV2.class)
                .actualizar(producto.getProductoId(), null)).withRel("update"));
        // Link para eliminar producto
        links.add(linkTo(methodOn(ProductoControllerV2.class)
                .eliminar(producto.getProductoId())).withRel("delete"));
        // Links condicionales y relacionados
        links.addAll(buildConditionalLinks(producto));
        return EntityModel.of(producto, links);
    }

    /**
     * Construye links condicionales basados en las propiedades del producto
     */
    private List<Link> buildConditionalLinks(Producto producto) {
        List<Link> links = new ArrayList<>();
        // Si es producto ecológico, link a productos ecológicos
        if (Boolean.TRUE.equals(producto.getEsEcologico())) {
            links.add(linkTo(methodOn(ProductoControllerV2.class)
                    .obtenerProductosEcologicos())
                    .withRel("productos-ecologicos")
                    .withTitle("Ver todos los productos ecológicos"));
        }
        // Link a productos de la misma categoría
        if (producto.getCategoriaId() != null) {
            links.add(linkTo(methodOn(ProductoControllerV2.class)
                    .obtenerPorCategoria(producto.getCategoriaId()))
                    .withRel("misma-categoria")
                    .withTitle("Productos de la misma categoría"));
        }
        // Link a productos del mismo proveedor
        if (producto.getProveedorPrincipalId() != null) {
            links.add(linkTo(methodOn(ProductoControllerV2.class)
                    .obtenerPorProveedor(producto.getProveedorPrincipalId()))
                    .withRel("mismo-proveedor")
                    .withTitle("Productos del mismo proveedor"));
        }
        // Links condicionales según el estado
        if ("ACTIVE".equalsIgnoreCase(producto.getEstado())) {
            links.add(linkTo(methodOn(ProductoControllerV2.class)
                    .actualizar(producto.getProductoId(), null))
                    .withRel("deactivate")
                    .withTitle("Desactivar producto"));
        }
        if ("INACTIVE".equalsIgnoreCase(producto.getEstado())) {
            links.add(linkTo(methodOn(ProductoControllerV2.class)
                    .actualizar(producto.getProductoId(), null))
                    .withRel("activate")
                    .withTitle("Activar producto"));
        }
        // Link a productos en rango de precio similar
        if (producto.getPrecioUnitario() != null) {
            double precio = producto.getPrecioUnitario();
            double rangoMin = precio * 0.8; // 20% menos
            double rangoMax = precio * 1.2; // 20% más
            links.add(linkTo(methodOn(ProductoControllerV2.class)
                    .obtenerPorRangoPrecio(rangoMin, rangoMax))
                    .withRel("precio-similar")
                    .withTitle("Productos con precio similar"));
        }
        return links;
    }

    /**
     * Método para crear EntityModel simple (útil para listas grandes)
     */
    public EntityModel<Producto> toSimpleModel(Producto producto) {
        return EntityModel.of(producto,
                linkTo(methodOn(ProductoControllerV2.class)
                        .obtenerPorId(producto.getProductoId())).withSelfRel(),
                linkTo(ProductoControllerV2.class).withRel("productos"));
    }
}
