package com.ecomarket.productservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de datos para productos ecológicos de EcoMarket")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del producto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer productoId;

    @Column(name = "nombre_producto")
    @Schema(description = "Nombre comercial del producto", example = "Jabón de lavanda orgánico", required = true)
    private String nombreProducto;

    @Column(name = "codigo_sku")
    @Schema(description = "Código SKU único del producto", example = "ECO-001", required = true)
    private String codigoSKU;

    @Column(name = "precio_unitario")
    @Schema(description = "Precio de venta al público", example = "12.99", required = true)
    private Double precioUnitario;

    @Column(name = "precio_compra")
    @Schema(description = "Precio de compra o costo", example = "6.50", required = true)
    private Double precioCompra;

    @Column(name = "margen_ganancia")
    @Schema(description = "Margen de ganancia (0.0 a 1.0)", example = "0.50", required = true)
    private Double margenGanancia;

    @Schema(description = "Descripción detallada del producto", example = "Jabón 100% natural elaborado con aceites esenciales de lavanda.")
    private String descripcion;

    @Column(name = "categoria_id")
    @Schema(description = "ID de la categoría del producto", example = "1", required = true)
    private Integer categoriaId;

    @Column(name = "proveedor_principal_id")
    @Schema(description = "ID del proveedor principal", example = "1", required = true)
    private Integer proveedorPrincipalId;

    @Column(name = "es_ecologico")
    @Schema(description = "Indica si el producto es ecológico/sustentable", example = "true", required = true)
    private Boolean esEcologico;

    @Column(name = "fecha_registro")
    @Schema(description = "Fecha y hora de registro del producto", example = "2024-06-24 10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private String fechaRegistro;

    @Schema(description = "Estado actual del producto", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "DISCONTINUED"})
    private String estado;
    

}
