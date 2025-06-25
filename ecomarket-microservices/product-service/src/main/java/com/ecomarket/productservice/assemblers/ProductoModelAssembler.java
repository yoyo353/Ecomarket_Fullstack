package com.ecomarket.productservice.assemblers;

import com.ecomarket.productservice.controller.ProductoController;
import com.ecomarket.productservice.model.Producto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {

    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto)
                .add(linkTo(methodOn(ProductoController.class).obtenerPorId(producto.getProductoId())).withSelfRel())
                .add(linkTo(ProductoController.class).withRel("productos"))
                .add(linkTo(methodOn(ProductoController.class).obtenerProductosEcologicos()).withRel("ecologicos"))
                .add(linkTo(methodOn(ProductoController.class).obtenerPorCategoria(producto.getCategoriaId())).withRel("categoria"));
    }
}
