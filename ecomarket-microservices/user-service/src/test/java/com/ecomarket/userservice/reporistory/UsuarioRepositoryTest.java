package com.ecomarket.userservice.reporistory;

import com.ecomarket.userservice.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Tests para UsuarioRepository")
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        // Usuario 1
        usuario1 = new Usuario();
        usuario1.setNombreUsuario("Ana");
        usuario1.setApellido("García");
        usuario1.setCorreoUsuario("ana@test.com");
        usuario1.setContraseña("password123");
        usuario1.setRolId(1);
        usuario1.setTiendaId(1);
        usuario1.setFechaRegistro("2024-01-01 10:00:00");
        usuario1.setEstado("activo");

        // Usuario 2
        usuario2 = new Usuario();
        usuario2.setNombreUsuario("Carlos");
        usuario2.setApellido("López");
        usuario2.setCorreoUsuario("carlos@test.com");
        usuario2.setContraseña("password456");
        usuario2.setRolId(2);
        usuario2.setTiendaId(1);
        usuario2.setFechaRegistro("2024-01-02 11:00:00");
        usuario2.setEstado("inactivo");

        // Guardar en la base de datos de prueba
        entityManager.persistAndFlush(usuario1);
        entityManager.persistAndFlush(usuario2);
    }

    @Test
    @DisplayName("Debería encontrar usuario por correo electrónico")
    void deberiaEncontrarUsuarioPorCorreo() {
        // When
        Optional<Usuario> resultado = usuarioRepository.findByCorreoUsuario("ana@test.com");

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombreUsuario()).isEqualTo("Ana");
        assertThat(resultado.get().getApellido()).isEqualTo("García");
    }

    @Test
    @DisplayName("Debería retornar Optional vacío para correo inexistente")
    void deberiaRetornarOptionalVacioParaCorreoInexistente() {
        // When
        Optional<Usuario> resultado = usuarioRepository.findByCorreoUsuario("noexiste@test.com");

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debería encontrar usuarios por rol ID")
    void deberiaEncontrarUsuariosPorRolId() {
        // Given - Agregar otro usuario con rol 1
        Usuario usuario3 = new Usuario();
        usuario3.setNombreUsuario("Pedro");
        usuario3.setApellido("Martínez");
        usuario3.setCorreoUsuario("pedro@test.com");
        usuario3.setContraseña("password789");
        usuario3.setRolId(1); // Mismo rol que usuario1
        usuario3.setTiendaId(2);
        usuario3.setEstado("activo");
        entityManager.persistAndFlush(usuario3);

        // When
        List<Usuario> resultado = usuarioRepository.findByRolId(1);

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(Usuario::getNombreUsuario)
                .containsExactlyInAnyOrder("Ana", "Pedro");
    }

    @Test
    @DisplayName("Debería encontrar usuarios por tienda ID")
    void deberiaEncontrarUsuariosPorTiendaId() {
        // When
        List<Usuario> resultado = usuarioRepository.findByTiendaId(1);

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(Usuario::getNombreUsuario)
                .containsExactlyInAnyOrder("Ana", "Carlos");
    }

    @Test
    @DisplayName("Debería encontrar usuarios por estado")
    void deberiaEncontrarUsuariosPorEstado() {
        // When
        List<Usuario> usuariosActivos = usuarioRepository.findByEstado("activo");
        List<Usuario> usuariosInactivos = usuarioRepository.findByEstado("inactivo");

        // Then
        assertThat(usuariosActivos).hasSize(1);
        assertThat(usuariosActivos.get(0).getNombreUsuario()).isEqualTo("Ana");

        assertThat(usuariosInactivos).hasSize(1);
        assertThat(usuariosInactivos.get(0).getNombreUsuario()).isEqualTo("Carlos");
    }

    @Test
    @DisplayName("Debería verificar si existe usuario por correo")
    void deberiaVerificarSiExisteUsuarioPorCorreo() {
        // When
        boolean existeAna = usuarioRepository.existsByCorreoUsuario("ana@test.com");
        boolean existeInexistente = usuarioRepository.existsByCorreoUsuario("noexiste@test.com");

        // Then
        assertThat(existeAna).isTrue();
        assertThat(existeInexistente).isFalse();
    }

    @Test
    @DisplayName("Debería encontrar usuarios por nombre que contenga texto (ignorando mayúsculas)")
    void deberiaEncontrarUsuariosPorNombreQueContengaTexto() {
        // When
        List<Usuario> resultado = usuarioRepository.findByNombreUsuarioContainingIgnoreCase("an");

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombreUsuario()).isEqualTo("Ana");
    }

    @Test
    @DisplayName("Debería encontrar usuarios por nombre parcial en minúsculas")
    void deberiaEncontrarUsuariosPorNombreParcialEnMinusculas() {
        // When
        List<Usuario> resultado = usuarioRepository.findByNombreUsuarioContainingIgnoreCase("car");

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombreUsuario()).isEqualTo("Carlos");
    }
}
