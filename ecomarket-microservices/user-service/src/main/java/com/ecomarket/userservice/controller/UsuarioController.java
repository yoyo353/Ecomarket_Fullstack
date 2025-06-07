package com.ecomarket.userservice.controller;

import com.ecomarket.userservice.model.Usuario;
import com.ecomarket.userservice.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok(usuarios);
    }
    
    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Buscar usuario por correo
    @GetMapping("/correo/{correo}")
    public ResponseEntity<Usuario> obtenerPorCorreo(@PathVariable String correo) {
        Usuario usuario = usuarioService.buscarPorCorreo(correo);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Crear nuevo usuario
    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.guardar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }
    
    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = usuarioService.actualizar(id, usuario);
        if (usuarioActualizado != null) {
            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        boolean eliminado = usuarioService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Endpoints adicionales
    
    // Total de usuarios
    @GetMapping("/total")
    public ResponseEntity<Integer> totalUsuarios() {
        int total = usuarioService.totalUsuarios();
        return ResponseEntity.ok(total);
    }
    
    // Usuarios por rol
    @GetMapping("/rol/{rolId}")
    public ResponseEntity<List<Usuario>> obtenerPorRol(@PathVariable Integer rolId) {
        List<Usuario> usuarios = usuarioService.obtenerPorRol(rolId);
        return ResponseEntity.ok(usuarios);
    }
    
    // Usuarios por tienda
    @GetMapping("/tienda/{tiendaId}")
    public ResponseEntity<List<Usuario>> obtenerPorTienda(@PathVariable Integer tiendaId) {
        List<Usuario> usuarios = usuarioService.obtenerPorTienda(tiendaId);
        return ResponseEntity.ok(usuarios);
    }
    
    // Usuarios activos
    @GetMapping("/activos")
    public ResponseEntity<List<Usuario>> obtenerUsuariosActivos() {
        List<Usuario> usuarios = usuarioService.obtenerUsuariosActivos();
        return ResponseEntity.ok(usuarios);
    }
    
    // Buscar usuarios por nombre
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<Usuario>> buscarPorNombre(@PathVariable String nombre) {
        List<Usuario> usuarios = usuarioService.buscarPorNombre(nombre);
        return ResponseEntity.ok(usuarios);
    }
}
