package com.ecomarket.userservice.service;

import com.ecomarket.userservice.model.Usuario;
import com.ecomarket.userservice.reporistory.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para UsuarioService")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioTest;

    @BeforeEach
    void setUp() {
        usuarioTest = new Usuario();
        usuarioTest.setUsuarioId(1);
        usuarioTest.setNombreUsuario("Juan");
        usuarioTest.setApellido("Pérez");
        usuarioTest.setCorreoUsuario("juan@test.com");
        usuarioTest.setContraseña("password123");
        usuarioTest.setRolId(1);
        usuarioTest.setTiendaId(1);
        usuarioTest.setFechaRegistro("2024-01-01 10:00:00");
        usuarioTest.setEstado("activo");
        usuarioTest.setUltimoAcceso("2024-01-15 15:30:00");
    }

    @Test
    @DisplayName("Debería retornar todos los usuarios")
    void deberiaRetornarTodosLosUsuarios() {
        // Given
        Usuario usuario2 = new Usuario();
        usuario2.setUsuarioId(2);
        usuario2.setNombreUsuario("María");
        usuario2.setApellido("González");
        usuario2.setCorreoUsuario("maria@test.com");

        List<Usuario> usuariosEsperados = Arrays.asList(usuarioTest, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuariosEsperados);

        // When
        List<Usuario> resultado = usuarioService.findAll();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombreUsuario()).isEqualTo("Juan");
        assertThat(resultado.get(1).getNombreUsuario()).isEqualTo("María");
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería retornar usuario por ID cuando existe")
    void deberiaRetornarUsuarioPorIdCuandoExiste() {
        // Given
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioTest));

        // When
        Optional<Usuario> resultado = usuarioService.findById(1);

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombreUsuario()).isEqualTo("Juan");
        assertThat(resultado.get().getCorreoUsuario()).isEqualTo("juan@test.com");
        verify(usuarioRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Debería retornar Optional vacío cuando usuario no existe")
    void deberiaRetornarOptionalVacioCuandoUsuarioNoExiste() {
        // Given
        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<Usuario> resultado = usuarioService.findById(999);

        // Then
        assertThat(resultado).isEmpty();
        verify(usuarioRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Debería guardar usuario correctamente")
    void deberiaGuardarUsuarioCorrectamente() {
        // Given
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);

        // When
        Usuario resultado = usuarioService.save(usuarioTest);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombreUsuario()).isEqualTo("Juan");
        assertThat(resultado.getCorreoUsuario()).isEqualTo("juan@test.com");
        verify(usuarioRepository, times(1)).save(usuarioTest);
    }

    @Test
    @DisplayName("Debería actualizar usuario cuando existe")
    void deberiaActualizarUsuarioCuandoExiste() {
        // Given
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setUsuarioId(1);
        usuarioActualizado.setNombreUsuario("Juan Carlos");
        usuarioActualizado.setApellido("Pérez López");
        usuarioActualizado.setCorreoUsuario("juancarlos@test.com");

        when(usuarioRepository.existsById(1)).thenReturn(true);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        // When
        Optional<Usuario> resultado = usuarioService.update(1, usuarioActualizado);

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombreUsuario()).isEqualTo("Juan Carlos");
        assertThat(resultado.get().getCorreoUsuario()).isEqualTo("juancarlos@test.com");
        verify(usuarioRepository, times(1)).existsById(1);
        verify(usuarioRepository, times(1)).save(usuarioActualizado);
    }

    @Test
    @DisplayName("Debería retornar Optional vacío al actualizar usuario inexistente")
    void deberiaRetornarOptionalVacioAlActualizarUsuarioInexistente() {
        // Given
        when(usuarioRepository.existsById(999)).thenReturn(false);

        // When
        Optional<Usuario> resultado = usuarioService.update(999, usuarioTest);

        // Then
        assertThat(resultado).isEmpty();
        verify(usuarioRepository, times(1)).existsById(999);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debería eliminar usuario cuando existe")
    void deberiaEliminarUsuarioCuandoExiste() {
        // Given
        when(usuarioRepository.existsById(1)).thenReturn(true);

        // When
        boolean resultado = usuarioService.delete(1);

        // Then
        assertThat(resultado).isTrue();
        verify(usuarioRepository, times(1)).existsById(1);
        verify(usuarioRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Debería retornar false al eliminar usuario inexistente")
    void deberiaRetornarFalseAlEliminarUsuarioInexistente() {
        // Given
        when(usuarioRepository.existsById(999)).thenReturn(false);

        // When
        boolean resultado = usuarioService.delete(999);

        // Then
        assertThat(resultado).isFalse();
        verify(usuarioRepository, times(1)).existsById(999);
        verify(usuarioRepository, never()).deleteById(anyInt());
    }
}
