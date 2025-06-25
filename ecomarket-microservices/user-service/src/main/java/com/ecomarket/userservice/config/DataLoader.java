package com.ecomarket.userservice.config;

import com.ecomarket.userservice.model.Usuario;
import com.ecomarket.userservice.reporistory.UsuarioRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Profile("dev") // Solo se ejecuta en el perfil de desarrollo
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        // Solo generar datos si la tabla está vacía
        if (usuarioRepository.count() == 0) {
            generarDatosDePrueba();
        }
    }

    private void generarDatosDePrueba() {
        Faker faker = new Faker();
        Random random = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("🔄 Generando datos de prueba con DataFaker...");

        // Generar 25 usuarios de prueba
        for (int i = 1; i <= 25; i++) {
            Usuario usuario = new Usuario();
            
            // Datos básicos del usuario
            usuario.setNombreUsuario(faker.name().firstName());
            usuario.setApellido(faker.name().lastName());
            usuario.setCorreoUsuario(faker.internet().emailAddress());
            usuario.setContraseña(faker.internet().password(8, 16));
            
            // IDs simulados
            usuario.setRolId(faker.number().numberBetween(1, 4)); // Roles: 1=Admin, 2=Cliente, 3=Vendedor, 4=Moderador
            usuario.setTiendaId(faker.number().numberBetween(1, 10)); // 10 tiendas simuladas
            
            // Fechas
            LocalDateTime fechaRegistro = faker.date().birthday(1, 365).toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
            usuario.setFechaRegistro(fechaRegistro.format(formatter));
            
            // Estado aleatorio
            String[] estados = {"activo", "inactivo", "suspendido", "pendiente"};
            usuario.setEstado(estados[random.nextInt(estados.length)]);
            
            // Último acceso (puede ser null para algunos usuarios)
            if (random.nextBoolean()) {
                LocalDateTime ultimoAcceso = fechaRegistro.plusDays(faker.number().numberBetween(1, 30));
                usuario.setUltimoAcceso(ultimoAcceso.format(formatter));
            }
            
            // Guardar usuario
            usuarioRepository.save(usuario);
        }

        System.out.println("✅ Se generaron 25 usuarios de prueba exitosamente!");
        System.out.println("📊 Total de usuarios en la base de datos: " + usuarioRepository.count());
    }
}
