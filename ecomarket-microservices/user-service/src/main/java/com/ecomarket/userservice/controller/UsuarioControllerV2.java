package com.ecomarket.userservice.controller;

import com.ecomarket.userservice.assemblers.UsuarioModelAssembler;
import com.ecomarket.userservice.model.Usuario;
import com.ecomarket.userservice.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v2/usuarios")
@Tag(name = "Usuarios V2", description = "API con HATEOAS para usuarios de EcoMarket")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler assembler;

    @Operation(summary = "Obtener todos los usuarios con HATEOAS")
    @GetMapping
    public CollectionModel<EntityModel<Usuario>> obtenerTodos() {
        List<EntityModel<Usuario>> usuarios = usuarioService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(usuarios)
                .add(linkTo(methodOn(UsuarioControllerV2.class).obtenerTodos()).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).totalUsuarios()).withRel("total"));
    }

    @Operation(summary = "Obtener usuario por ID con HATEOAS")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> obtenerPorId(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario != null) {
            return ResponseEntity.ok(assembler.toModel(usuario));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Buscar usuario por correo con HATEOAS")
    @GetMapping("/correo/{correo}")
    public ResponseEntity<EntityModel<Usuario>> obtenerPorCorreo(@PathVariable String correo) {
        Usuario usuario = usuarioService.buscarPorCorreo(correo);
        if (usuario != null) {
            return ResponseEntity.ok(assembler.toModel(usuario));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear nuevo usuario con HATEOAS")
    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> crear(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.guardar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(nuevoUsuario));
    }

    @Operation(summary = "Actualizar usuario con HATEOAS")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> actualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = usuarioService.actualizar(id, usuario);
        if (usuarioActualizado != null) {
            return ResponseEntity.ok(assembler.toModel(usuarioActualizado));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        boolean eliminado = usuarioService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Total de usuarios")
    @GetMapping("/total")
    public ResponseEntity<EntityModel<Integer>> totalUsuarios() {
        int total = usuarioService.totalUsuarios();
        return ResponseEntity.ok(
                EntityModel.of(total)
                        .add(linkTo(methodOn(UsuarioControllerV2.class).totalUsuarios()).withSelfRel())
                        .add(linkTo(methodOn(UsuarioControllerV2.class).obtenerTodos()).withRel("usuarios"))
        );
    }

    @Operation(summary = "Obtener usuarios por rol con HATEOAS")
    @GetMapping("/rol/{rolId}")
    public CollectionModel<EntityModel<Usuario>> obtenerPorRol(@PathVariable Integer rolId) {
        List<EntityModel<Usuario>> usuarios = usuarioService.obtenerPorRol(rolId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(usuarios)
                .add(linkTo(methodOn(UsuarioControllerV2.class).obtenerPorRol(rolId)).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).obtenerTodos()).withRel("usuarios"));
    }

    @Operation(summary = "Obtener usuarios por tienda con HATEOAS")
    @GetMapping("/tienda/{tiendaId}")
    public CollectionModel<EntityModel<Usuario>> obtenerPorTienda(@PathVariable Integer tiendaId) {
        List<EntityModel<Usuario>> usuarios = usuarioService.obtenerPorTienda(tiendaId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(usuarios)
                .add(linkTo(methodOn(UsuarioControllerV2.class).obtenerPorTienda(tiendaId)).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).obtenerTodos()).withRel("usuarios"));
    }

    @Operation(summary = "Obtener usuarios activos con HATEOAS")
    @GetMapping("/activos")
    public CollectionModel<EntityModel<Usuario>> obtenerUsuariosActivos() {
        List<EntityModel<Usuario>> usuarios = usuarioService.obtenerUsuariosActivos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(usuarios)
                .add(linkTo(methodOn(UsuarioControllerV2.class).obtenerUsuariosActivos()).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).obtenerTodos()).withRel("usuarios"));
    }

    @Operation(summary = "Buscar usuarios por nombre con HATEOAS")
    @GetMapping("/buscar/{nombre}")
    public CollectionModel<EntityModel<Usuario>> buscarPorNombre(@PathVariable String nombre) {
        List<EntityModel<Usuario>> usuarios = usuarioService.buscarPorNombre(nombre).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(usuarios)
                .add(linkTo(methodOn(UsuarioControllerV2.class).buscarPorNombre(nombre)).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).obtenerTodos()).withRel("usuarios"));
    }
}
