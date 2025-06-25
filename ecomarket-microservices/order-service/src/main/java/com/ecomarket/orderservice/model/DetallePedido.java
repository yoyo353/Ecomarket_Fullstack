package com.ecomarket.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "detalle_de_pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa el detalle de un producto dentro de un pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del detalle", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer detalleId;

    @Column(name = "pedido_id")
    @Schema(description = "ID del pedido al que pertenece este detalle", example = "1", required = true)
    private Integer pedidoId;

    @Column(name = "producto_id")
    @Schema(description = "Identificador del producto en el catálogo", example = "101", required = true)
    private Integer productoId;

    @Column(name = "precio_unitario")
    @Schema(
        description = "Precio unitario del producto al momento de la compra", 
        example = "25.99",
        minimum = "0",
        required = true
    )
    private Double precioUnitario;

    @Schema(
        description = "Cantidad de unidades del producto en el pedido", 
        example = "2",
        minimum = "1",
        required = true
    )
    private Integer cantidad;

    @Column(name = "sub_total")
    @Schema(
        description = "Subtotal calculado para este detalle (precio_unitario * cantidad)", 
        example = "51.98",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Double subTotal;
}
