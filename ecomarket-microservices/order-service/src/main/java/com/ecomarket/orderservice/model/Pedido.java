package com.ecomarket.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa un pedido en EcoMarket")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del pedido", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer pedidoId;

    @Column(name = "fecha_de_pedido")
    @Schema(description = "Fecha en que se realizó el pedido", example = "2025-06-23", required = true)
    private String fechaDePedido;

    @Column(name = "cliente_id")
    @Schema(description = "Identificador del cliente que realizó el pedido", example = "1", required = true)
    private Integer clienteId;

    @Schema(
        description = "Estado actual del pedido", 
        example = "PENDIENTE",
        allowableValues = {"PENDIENTE", "EN_PROCESO", "COMPLETADO", "CANCELADO", "ENVIADO"},
        required = true
    )
    private String estado;

    @Schema(
        description = "Porcentaje de descuento aplicado al pedido", 
        example = "10.0",
        minimum = "0",
        maximum = "100"
    )
    private Double descuento;

    @Column(name = "metodo_pago_id")
    @Schema(description = "Identificador del método de pago utilizado", example = "1", required = true)
    private Integer metodoPagoId;

    @Column(name = "usuario_id")
    @Schema(description = "Identificador del usuario/vendedor que procesó el pedido", example = "1")
    private Integer usuarioId;

    @Schema(
        description = "Subtotal del pedido antes de aplicar descuentos", 
        example = "100.50",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Double subtotal;

    @Schema(
        description = "Total final del pedido después de aplicar descuentos", 
        example = "90.45",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Double total;

    @Column(name = "direccion_envio")
    @Schema(description = "Dirección completa de entrega del pedido", example = "Av. Providencia 1234, Providencia")
    private String direccionEnvio;

    @Column(name = "ciudad_envio")
    @Schema(description = "Ciudad de destino para el envío", example = "Santiago")
    private String ciudadEnvio;

    @Schema(description = "Notas adicionales o instrucciones especiales para el pedido", example = "Entrega en horario de oficina")
    private String notas;
}

