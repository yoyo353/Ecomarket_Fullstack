package com.ecomarket.inventoryservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inventarioId;

    @Column(name = "producto_id")
    private Integer productoId;

    @Column(name = "cantidad_disponible")
    private Integer cantidadDisponible;

    @Column(name = "cantidad_minima")
    private Integer cantidadMinima;

    @Column(name = "bodega_id")
    private Integer bodegaId;

    @Column(name = "fecha_actualizacion")
    private String fechaActualizacion;

    private String estado;
}
