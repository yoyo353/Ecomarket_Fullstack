package com.ecomarket.userservice.controller;

import com.ecomarket.userservice.assemblers.UsuarioModelAssembler;
import com.ecomarket.userservice.model.Usuario;
import com.ecomarket.userservice.service.UsuarioService;
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
@RequestMapping("/api/v2/usuarios")
@Tag(name = "Usuarios V2", description = "API con HATEOAS para gestión de usuarios de EcoMarket")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler assembler;

    @Operation(
        summary = "Obtener todos los usuarios",
        description = "Retorna una lista completa de usuarios con enlaces HATEOAS para navegación"
    )
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    @GetMapping
    public CollectionModel<EntityModel<Usuario>> getAllUsuarios() {
        List<EntityModel<Usuario>> usuarios = usuarioService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(usuarios)
                // Link a sí mismo
                .add(linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withSelfRel())
                // Link para crear nuevo usuario
                .add(linkTo(UsuarioControllerV2.class).withRel("create").withTitle("Crear usuario"))
                // Links adicionales para filtros
                .add(linkTo(UsuarioControllerV2.class).slash("activos").withRel("active-users"))
                .add(linkTo(UsuarioControllerV2.class).slash("inactivos").withRel("inactive-users"));
    }

    @Operation(
        summary = "Obtener usuario por ID",
        description = "Retorna un usuario específico con todos sus enlaces HATEOAS disponibles"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> getUsuarioById(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Integer id) {
        return usuarioService.findById(id)
                .map(usuario -> ResponseEntity.ok(assembler.toModel(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nuevo usuario",
        description = "Crea un nuevo usuario en el sistema y retorna el usuario creado con enlaces HATEOAS"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> createUsuario(
            @Parameter(description = "Datos del usuario a crear", required = true)
            @RequestBody Usuario usuario) {
        Usuario savedUsuario = usuarioService.save(usuario);
        EntityModel<Usuario> usuarioModel = assembler.toModel(savedUsuario);
        return ResponseEntity
                .created(usuarioModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(usuarioModel);
    }

    @Operation(
        summary = "Actualizar usuario existente",
        description = "Actualiza un usuario existente y retorna el usuario actualizado con enlaces HATEOAS"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> updateUsuario(
            @Parameter(description = "ID del usuario a actualizar", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Nuevos datos del usuario", required = true)
            @RequestBody Usuario usuario) {
        return usuarioService.update(id, usuario)
                .map(updatedUsuario -> ResponseEntity.ok(assembler.toModel(updatedUsuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Eliminar usuario",
        description = "Elimina un usuario del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(
            @Parameter(description = "ID del usuario a eliminar", required = true, example = "1")
            @PathVariable Integer id) {
        if (usuarioService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Métodos adicionales para HATEOAS y compatibilidad con el assembler

    // Alias para compatibilidad con el assembler y HATEOAS avanzado
    @GetMapping("/alias/{id}")
    public ResponseEntity<EntityModel<Usuario>> obtenerPorId(@PathVariable Integer id) {
        return getUsuarioById(id);
    }

    @PutMapping("/alias/{id}")
    public ResponseEntity<EntityModel<Usuario>> actualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        return updateUsuario(id, usuario);
    }

    @DeleteMapping("/alias/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return deleteUsuario(id);
    }

    // Métodos para usuarios activos/inactivos y estadísticas
    @Operation(
        summary = "Obtener usuarios activos",
        description = "Retorna solo los usuarios con estado 'activo'"
    )
    @GetMapping("/activos")
    public CollectionModel<EntityModel<Usuario>> getUsuariosActivos() {
        List<EntityModel<Usuario>> usuariosActivos = usuarioService.findAll().stream()
                .filter(usuario -> "activo".equalsIgnoreCase(usuario.getEstado()))
                .map(assembler::toSimpleModel)
                .collect(Collectors.toList());
        return CollectionModel.of(usuariosActivos)
                .add(linkTo(methodOn(UsuarioControllerV2.class).getUsuariosActivos()).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withRel("all-users"))
                .add(linkTo(methodOn(UsuarioControllerV2.class).getUsuariosInactivos()).withRel("inactive-users"));
    }

    @Operation(
        summary = "Obtener usuarios inactivos",
        description = "Retorna solo los usuarios con estado 'inactivo' o 'suspendido'"
    )
    @GetMapping("/inactivos")
    public CollectionModel<EntityModel<Usuario>> getUsuariosInactivos() {
        List<EntityModel<Usuario>> usuariosInactivos = usuarioService.findAll().stream()
                .filter(usuario -> "inactivo".equalsIgnoreCase(usuario.getEstado()) || 
                                  "suspendido".equalsIgnoreCase(usuario.getEstado()))
                .map(assembler::toSimpleModel)
                .collect(Collectors.toList());
        return CollectionModel.of(usuariosInactivos)
                .add(linkTo(methodOn(UsuarioControllerV2.class).getUsuariosInactivos()).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withRel("all-users"))
                .add(linkTo(methodOn(UsuarioControllerV2.class).getUsuariosActivos()).withRel("active-users"));
    }

    @Operation(
        summary = "Obtener estadísticas de usuarios",
        description = "Retorna información estadística sobre los usuarios con enlaces relacionados"
    )
    @GetMapping("/stats")
    public ResponseEntity<EntityModel<UsuarioStats>> getUsuarioStats() {
        long totalUsuarios = usuarioService.findAll().size();
        long usuariosActivos = usuarioService.findAll().stream()
                .filter(u -> "activo".equalsIgnoreCase(u.getEstado()))
                .count();
        long usuariosInactivos = totalUsuarios - usuariosActivos;
        UsuarioStats stats = new UsuarioStats(totalUsuarios, usuariosActivos, usuariosInactivos);
        EntityModel<UsuarioStats> statsModel = EntityModel.of(stats)
                .add(linkTo(methodOn(UsuarioControllerV2.class).getUsuarioStats()).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withRel("all-users"))
                .add(linkTo(methodOn(UsuarioControllerV2.class).getUsuariosActivos()).withRel("active-users"))
                .add(linkTo(methodOn(UsuarioControllerV2.class).getUsuariosInactivos()).withRel("inactive-users"));
        return ResponseEntity.ok(statsModel);
    }

    // Clase interna para estadísticas de usuario
    public static class UsuarioStats {
        public final long total;
        public final long activos;
        public final long inactivos;
        public final double porcentajeActivos;
        public UsuarioStats(long total, long activos, long inactivos) {
            this.total = total;
            this.activos = activos;
            this.inactivos = inactivos;
            this.porcentajeActivos = total > 0 ? ((double) activos / total) * 100 : 0;
        }
    }
}
