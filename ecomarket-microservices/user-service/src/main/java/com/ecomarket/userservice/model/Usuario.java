package com.ecomarket.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer usuarioId;

    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    private String apellido;

    @Column(name = "correo_usuario", unique = true)
    private String correoUsuario;

    private String contraseña;

    @Column(name = "rol_id")
    private Integer rolId;

    @Column(name = "tienda_id")
    private Integer tiendaId;

    @Column(name = "fecha_registro")
    private String fechaRegistro;

    private String estado;

    @Column(name = "ultimo_acceso")
    private String ultimoAcceso;
}
