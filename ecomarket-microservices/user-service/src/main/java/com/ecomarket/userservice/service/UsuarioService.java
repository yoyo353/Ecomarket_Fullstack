package com.ecomarket.userservice.service;

import com.ecomarket.userservice.model.Usuario;
import com.ecomarket.userservice.reporistory.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todos los usuarios
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    // Buscar usuario por ID
    public Usuario buscarPorId(Integer id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.orElse(null);
    }

    // Buscar usuario por correo
    public Usuario buscarPorCorreo(String correo) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreoUsuario(correo);
        return usuario.orElse(null);
    }

    // Guardar usuario
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Actualizar usuario
    public Usuario actualizar(Integer id, Usuario usuarioActualizado) {
        if (usuarioRepository.existsById(id)) {
            usuarioActualizado.setUsuarioId(id);
            return usuarioRepository.save(usuarioActualizado);
        }
        return null;
    }

    // Eliminar usuario
    public boolean eliminar(Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Total de usuarios
    public int totalUsuarios() {
        return (int) usuarioRepository.count();
    }

    // Usuarios por rol
    public List<Usuario> obtenerPorRol(Integer rolId) {
        return usuarioRepository.findByRolId(rolId);
    }

    // Usuarios por tienda
    public List<Usuario> obtenerPorTienda(Integer tiendaId) {
        return usuarioRepository.findByTiendaId(tiendaId);
    }

    // Usuarios activos
    public List<Usuario> obtenerUsuariosActivos() {
        return usuarioRepository.findByEstado("ACTIVO");
    }

    // Buscar usuarios por nombre
    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombreUsuarioContainingIgnoreCase(nombre);
    }
}

