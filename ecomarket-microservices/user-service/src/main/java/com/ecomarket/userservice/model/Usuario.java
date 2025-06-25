package com.ecomarket.userservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    name = "Usuario",
    description = "Entidad que representa un usuario en el sistema EcoMarket"
)
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "Identificador único del usuario",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer usuarioId;
    
    @Column(name = "nombre_usuario")
    @Schema(
        description = "Nombre del usuario",
        example = "Juan",
        required = true,
        maxLength = 100
    )
    private String nombreUsuario;
    
    @Schema(
        description = "Apellido del usuario",
        example = "Pérez",
        required = true,
        maxLength = 100
    )
    private String apellido;
    
    @Column(name = "correo_usuario", unique = true)
    @Schema(
        description = "Correo electrónico único del usuario",
        example = "juan.perez@ecomarket.com",
        required = true,
        format = "email"
    )
    private String correoUsuario;
    
    @Schema(
        description = "Contraseña del usuario (será encriptada)",
        example = "password123",
        required = true,
        minLength = 8,
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private String contraseña;
    
    @Column(name = "rol_id")
    @Schema(
        description = "Identificador del rol del usuario",
        example = "1",
        allowableValues = {"1", "2", "3", "4"},
        implementation = Integer.class
    )
    private Integer rolId;
    
    @Column(name = "tienda_id")
    @Schema(
        description = "Identificador de la tienda asociada al usuario",
        example = "1",
        implementation = Integer.class
    )
    private Integer tiendaId;
    
    @Column(name = "fecha_registro")
    @Schema(
        description = "Fecha y hora de registro del usuario",
        example = "2024-01-15 10:30:00",
        format = "date-time",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private String fechaRegistro;
    
    @Schema(
        description = "Estado actual del usuario",
        example = "activo",
        allowableValues = {"activo", "inactivo", "suspendido", "pendiente"},
        defaultValue = "pendiente"
    )
    private String estado;
    
    @Column(name = "ultimo_acceso")
    @Schema(
        description = "Fecha y hora del último acceso del usuario",
        example = "2024-01-20 14:25:00",
        format = "date-time",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private String ultimoAcceso;
}
