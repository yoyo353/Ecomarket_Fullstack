package com.ecomarket.userservice.config;

import com.ecomarket.userservice.model.Usuario;
import com.ecomarket.userservice.reporistory.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String[] nombres = {"Juan", "Ana", "Carlos", "Lucía", "Pedro", "Sofía", "Miguel", "Valentina", "Diego", "Camila"};
        String[] apellidos = {"Pérez", "López", "Ruiz", "García", "Torres", "Morales", "Castro", "Silva", "Rojas", "Mendoza"};
        String[] estados = {"ACTIVO", "INACTIVO", "BLOQUEADO"};
        int[] roles = {1, 2, 3}; // Ejemplo: 1=admin, 2=cliente, 3=empleado
        int[] tiendas = {1, 2, 3, 4, 5};

        System.out.println("🌱 Generando datos falsos para usuarios...");

        for (int i = 0; i < 30; i++) {
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(nombres[random.nextInt(nombres.length)]);
            usuario.setApellido(apellidos[random.nextInt(apellidos.length)]);
            usuario.setCorreoUsuario("usuario" + i + "@mail.com");
            usuario.setContraseña("pass" + (1000 + random.nextInt(9000)));
            usuario.setRolId(roles[random.nextInt(roles.length)]);
            usuario.setTiendaId(tiendas[random.nextInt(tiendas.length)]);
            usuario.setFechaRegistro(LocalDateTime.now().minusDays(random.nextInt(365)).format(formatter));
            usuario.setEstado(estados[random.nextInt(estados.length)]);
            usuario.setUltimoAcceso(LocalDateTime.now().minusDays(random.nextInt(30)).format(formatter));
            usuarioRepository.save(usuario);
        }

        System.out.println("✅ Se han generado " + usuarioRepository.count() + " usuarios en la base de datos H2");
        System.out.println("🔗 Accede a la consola H2 en: http://localhost:8091/h2-console");
        System.out.println("   JDBC URL: jdbc:h2:mem:userdb");
        System.out.println("   Usuario: sa");
        System.out.println("   Contraseña: (vacía)");
    }
}
