package com.ecomarket.userservice.assemblers;

import com.ecomarket.userservice.controller.UsuarioControllerV2;
import com.ecomarket.userservice.model.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public @NonNull EntityModel<Usuario> toModel(@NonNull Usuario usuario) {
        List<Link> links = new ArrayList<>();
        // Link a sí mismo (self)
        links.add(linkTo(methodOn(UsuarioControllerV2.class)
                .getUsuarioById(usuario.getUsuarioId())).withSelfRel());
        // Link a la colección de usuarios
        links.add(linkTo(UsuarioControllerV2.class).withRel("usuarios"));
        // Link para actualizar usuario
        links.add(linkTo(methodOn(UsuarioControllerV2.class)
                .updateUsuario(usuario.getUsuarioId(), null)).withRel("update"));
        // Link para eliminar usuario
        links.add(linkTo(methodOn(UsuarioControllerV2.class)
                .deleteUsuario(usuario.getUsuarioId())).withRel("delete"));
        // Links condicionales según el estado del usuario
        links.addAll(buildConditionalLinks(usuario));
        return EntityModel.of(usuario, links);
    }

    /**
     * Construye links condicionales basados en el estado del usuario
     */
    private List<Link> buildConditionalLinks(Usuario usuario) {
        List<Link> links = new ArrayList<>();
        // Si el usuario está activo, permitir desactivar
        if ("activo".equalsIgnoreCase(usuario.getEstado())) {
            links.add(linkTo(methodOn(UsuarioControllerV2.class)
                    .updateUsuario(usuario.getUsuarioId(), null))
                    .withRel("deactivate")
                    .withTitle("Desactivar usuario"));
        }
        // Si el usuario está inactivo, permitir activar
        if ("inactivo".equalsIgnoreCase(usuario.getEstado()) || "suspendido".equalsIgnoreCase(usuario.getEstado())) {
            links.add(linkTo(methodOn(UsuarioControllerV2.class)
                    .updateUsuario(usuario.getUsuarioId(), null))
                    .withRel("activate")
                    .withTitle("Activar usuario"));
        }
        // Link para ver usuarios del mismo rol
        if (usuario.getRolId() != null) {
            links.add(linkTo(UsuarioControllerV2.class)
                    .slash("rol")
                    .slash(usuario.getRolId())
                    .withRel("same-role")
                    .withTitle("Usuarios del mismo rol"));
        }
        // Link para ver usuarios de la misma tienda
        if (usuario.getTiendaId() != null) {
            links.add(linkTo(UsuarioControllerV2.class)
                    .slash("tienda")
                    .slash(usuario.getTiendaId())
                    .withRel("same-store")
                    .withTitle("Usuarios de la misma tienda"));
        }
        return links;
    }

    /**
     * Método adicional para crear EntityModel sin links (útil para listas grandes)
     */
    public EntityModel<Usuario> toSimpleModel(Usuario usuario) {
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioControllerV2.class)
                        .getUsuarioById(usuario.getUsuarioId())).withSelfRel());
    }
}
