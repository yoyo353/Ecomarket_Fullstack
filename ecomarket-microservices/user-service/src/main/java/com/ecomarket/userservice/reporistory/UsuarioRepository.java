package com.ecomarket.userservice.reporistory;

import com.ecomarket.userservice.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    Optional<Usuario> findByCorreoUsuario(String correoUsuario);
    
    List<Usuario> findByRolId(Integer rolId);
    
    List<Usuario> findByTiendaId(Integer tiendaId);
    
    List<Usuario> findByEstado(String estado);
    
    boolean existsByCorreoUsuario(String correoUsuario);
    
    List<Usuario> findByNombreUsuarioContainingIgnoreCase(String nombreUsuario);
}

