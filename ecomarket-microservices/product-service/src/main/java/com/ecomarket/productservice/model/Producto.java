package com.ecomarket.productservice.model;

// Modelo JPA que representa la entidad Producto en la base de datos.
// Se agregaron anotaciones de Swagger/OpenAPI para documentar cada campo y facilitar la integración.
// Se utiliza Lombok para reducir el código boilerplate (getters, setters, constructores).
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto")
@Data // Lombok: genera getters/setters/toString/equals/hashCode automáticamente
@AllArgsConstructor // Lombok: constructor con todos los campos
@NoArgsConstructor  // Lombok: constructor vacío
@Schema(description = "Modelo de datos para productos ecológicos de EcoMarket")
public class Producto {

    // Identificador único autogenerado para cada producto
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del producto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer productoId;

    // Nombre comercial del producto
    @Column(name = "nombre_producto")
    @Schema(description = "Nombre comercial del producto", example = "Jabón de lavanda orgánico", required = true)
    private String nombreProducto;

    // Código SKU único para inventario y validaciones
    @Column(name = "codigo_sku")
    @Schema(description = "Código SKU único del producto", example = "ECO-001", required = true)
    private String codigoSKU;

    // Precio de venta al público
    @Column(name = "precio_unitario")
    @Schema(description = "Precio de venta al público", example = "12.99", required = true)
    private Double precioUnitario;

    // Precio de compra o costo base
    @Column(name = "precio_compra")
    @Schema(description = "Precio de compra o costo", example = "6.50", required = true)
    private Double precioCompra;

    // Margen de ganancia (0.0 a 1.0)
    @Column(name = "margen_ganancia")
    @Schema(description = "Margen de ganancia (0.0 a 1.0)", example = "0.50", required = true)
    private Double margenGanancia;

    // Descripción detallada del producto
    @Schema(description = "Descripción detallada del producto", example = "Jabón 100% natural elaborado con aceites esenciales de lavanda.")
    private String descripcion;

    // Relación con la categoría (id de categoría)
    @Column(name = "categoria_id")
    @Schema(description = "ID de la categoría del producto", example = "1", required = true)
    private Integer categoriaId;

    // Relación con el proveedor principal (id de proveedor)
    @Column(name = "proveedor_principal_id")
    @Schema(description = "ID del proveedor principal", example = "1", required = true)
    private Integer proveedorPrincipalId;

    // Indica si el producto es ecológico/sustentable
    @Column(name = "es_ecologico")
    @Schema(description = "Indica si el producto es ecológico/sustentable", example = "true", required = true)
    private Boolean esEcologico;

    // Fecha y hora de registro del producto (formato String para simplicidad en pruebas)
    @Column(name = "fecha_registro")
    @Schema(description = "Fecha y hora de registro del producto", example = "2024-06-24 10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private String fechaRegistro;

    // Estado del producto (ej: ACTIVE, INACTIVE, DISCONTINUED)
    @Schema(description = "Estado actual del producto", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "DISCONTINUED"})
    private String estado;
}
