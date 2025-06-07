package com.ecomarket.productservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productoId;

    @Column(name = "nombre_producto")
    private String nombreProducto;

    @Column(name = "codigo_sku")
    private String codigoSKU;

    @Column(name = "precio_unitario")
    private Double precioUnitario;

    @Column(name = "precio_compra")
    private Double precioCompra;

    @Column(name = "margen_ganancia")
    private Double margenGanancia;

    private String descripcion;

    @Column(name = "categoria_id")
    private Integer categoriaId;

    @Column(name = "proveedor_principal_id")
    private Integer proveedorPrincipalId;

    @Column(name = "es_ecologico")
    private Boolean esEcologico;

    @Column(name = "fecha_registro")
    private String fechaRegistro;

    private String estado;
    

}
