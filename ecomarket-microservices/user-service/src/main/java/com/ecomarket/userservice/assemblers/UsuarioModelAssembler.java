package com.ecomarket.userservice.assemblers;

import com.ecomarket.userservice.model.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.ecomarket.userservice.controller.UsuarioControllerV2;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {
    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioControllerV2.class).obtenerPorId(usuario.getUsuarioId())).withSelfRel(),
                linkTo(methodOn(UsuarioControllerV2.class).obtenerTodos()).withRel("usuarios"));
    }
}
