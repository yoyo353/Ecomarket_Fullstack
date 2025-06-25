package com.ecomarket.userservice.controller;

import com.ecomarket.userservice.model.Usuario;
import com.ecomarket.userservice.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios V1", description = "API REST básica para gestión de usuarios de EcoMarket")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @Operation(
        summary = "Listar todos los usuarios",
        description = "Obtiene una lista completa de todos los usuarios registrados en el sistema"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Lista de usuarios obtenida exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Usuario.class),
            examples = @ExampleObject(
                name = "Lista de usuarios",
                value = """
                [
                  {
                    \"usuarioId\": 1,
                    \"nombreUsuario\": \"Juan\",
                    \"apellido\": \"Pérez\",
                    \"correoUsuario\": \"juan@ecomarket.com\",
                    \"rolId\": 1,
                    \"tiendaId\": 1,
                    \"fechaRegistro\": \"2024-01-15 10:30:00\",
                    \"estado\": \"activo\",
                    \"ultimoAcceso\": \"2024-01-20 14:25:00\"
                  }
                ]
                """
            )
        )
    )
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.findAll();
    }

    @Operation(
        summary = "Obtener usuario por ID",
        description = "Busca y retorna un usuario específico utilizando su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Usuario encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class),
                examples = @ExampleObject(
                    name = "Usuario encontrado",
                    value = """
                    {
                      \"usuarioId\": 1,
                      \"nombreUsuario\": \"Juan\",
                      \"apellido\": \"Pérez\",
                      \"correoUsuario\": \"juan@ecomarket.com\",
                      \"rolId\": 1,
                      \"tiendaId\": 1,
                      \"fechaRegistro\": \"2024-01-15 10:30:00\",
                      \"estado\": \"activo\",
                      \"ultimoAcceso\": \"2024-01-20 14:25:00\"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Usuario no encontrado",
            content = @Content(
                examples = @ExampleObject(
                    name = "Usuario no encontrado",
                    value = "{ \"error\": \"Usuario con ID 999 no encontrado\" }"
                )
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(
            @Parameter(
                description = "Identificador único del usuario", 
                required = true,
                example = "1",
                schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable Integer id) {
        return usuarioService.findById(id)
                .map(usuario -> ResponseEntity.ok().body(usuario))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nuevo usuario",
        description = "Registra un nuevo usuario en el sistema con los datos proporcionados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Usuario creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de entrada inválidos",
            content = @Content(
                examples = @ExampleObject(
                    name = "Error de validación",
                    value = "{ \"error\": \"El correo electrónico ya está en uso\" }"
                )
            )
        )
    })
    @PostMapping
    public ResponseEntity<Usuario> createUsuario(
            @Parameter(
                description = "Datos del usuario a crear",
                required = true,
                content = @Content(
                    examples = @ExampleObject(
                        name = "Nuevo usuario",
                        value = """
                        {
                          \"nombreUsuario\": \"María\",
                          \"apellido\": \"González\",
                          \"correoUsuario\": \"maria@ecomarket.com\",
                          \"contraseña\": \"password123\",
                          \"rolId\": 2,
                          \"tiendaId\": 1,
                          \"estado\": \"activo\"
                        }
                        """
                    )
                )
            )
            @RequestBody Usuario usuario) {
        Usuario savedUsuario = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsuario);
    }

    @Operation(
        summary = "Actualizar usuario existente",
        description = "Modifica los datos de un usuario existente. Todos los campos del usuario serán actualizados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Usuario actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Usuario no encontrado"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de entrada inválidos"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(
            @Parameter(
                description = "ID del usuario a actualizar", 
                required = true,
                example = "1"
            )
            @PathVariable Integer id,
            @Parameter(
                description = "Nuevos datos del usuario",
                required = true,
                content = @Content(
                    examples = @ExampleObject(
                        name = "Usuario actualizado",
                        value = """
                        {
                          \"nombreUsuario\": \"Juan Carlos\",
                          \"apellido\": \"Pérez López\",
                          \"correoUsuario\": \"juancarlos@ecomarket.com\",
                          \"contraseña\": \"newpassword123\",
                          \"rolId\": 1,
                          \"tiendaId\": 2,
                          \"estado\": \"activo\"
                        }
                        """
                    )
                )
            )
            @RequestBody Usuario usuario) {
        return usuarioService.update(id, usuario)
                .map(updatedUsuario -> ResponseEntity.ok().body(updatedUsuario))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Eliminar usuario",
        description = "Elimina permanentemente un usuario del sistema. Esta acción no se puede deshacer"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Usuario eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Usuario no encontrado"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(
            @Parameter(
                description = "ID del usuario a eliminar", 
                required = true,
                example = "1"
            )
            @PathVariable Integer id) {
        if (usuarioService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
