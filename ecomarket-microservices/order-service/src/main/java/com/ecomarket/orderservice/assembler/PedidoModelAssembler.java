package com.ecomarket.orderservice.assembler;

import com.ecomarket.orderservice.controller.PedidoControllerV2;
import com.ecomarket.orderservice.model.Pedido;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Assembler para convertir entidades Pedido en modelos HATEOAS
 * 
 * Este assembler se encarga de agregar enlaces hipermedia a los pedidos,
 * permitiendo la navegación dinámica a través de la API REST.
 */
@Component
public class PedidoModelAssembler implements RepresentationModelAssembler<Pedido, EntityModel<Pedido>> {

    @Override
    public EntityModel<Pedido> toModel(Pedido pedido) {
        // Crear el modelo de entidad con enlaces HATEOAS
        EntityModel<Pedido> pedidoModel = EntityModel.of(pedido);
        
        // 🔗 Enlace a sí mismo (self)
        pedidoModel.add(linkTo(methodOn(PedidoControllerV2.class)
            .obtenerPorId(pedido.getPedidoId())).withSelfRel());
        
        // 🔗 Enlace a la colección de todos los pedidos
        pedidoModel.add(linkTo(PedidoControllerV2.class).withRel("pedidos"));
        
        // 🔗 Enlace a los detalles del pedido
        pedidoModel.add(linkTo(methodOn(PedidoControllerV2.class)
            .obtenerDetalles(pedido.getPedidoId())).withRel("detalles"));
        
        // 🔗 Enlaces condicionales según el estado del pedido
        if ("PENDIENTE".equals(pedido.getEstado())) {
            // Solo agregar enlace de actualización si está pendiente
            pedidoModel.add(linkTo(methodOn(PedidoControllerV2.class)
                .actualizar(pedido.getPedidoId(), null)).withRel("actualizar"));
            
            // Enlace para cambiar estado
            pedidoModel.add(linkTo(methodOn(PedidoControllerV2.class)
                .actualizarEstado(pedido.getPedidoId(), null)).withRel("cambiar-estado"));
        }
        
        if ("PENDIENTE".equals(pedido.getEstado()) || "EN_PROCESO".equals(pedido.getEstado())) {
            // Solo permitir cancelar si no está completado
            pedidoModel.add(linkTo(methodOn(PedidoControllerV2.class)
                .eliminar(pedido.getPedidoId())).withRel("cancelar"));
        }
        
        // 🔗 Enlaces a recursos relacionados
        
        // Enlace a pedidos del mismo cliente
        pedidoModel.add(linkTo(methodOn(PedidoControllerV2.class)
            .obtenerPorCliente(pedido.getClienteId())).withRel("pedidos-cliente"));
        
        // Enlace a pedidos del mismo estado
        pedidoModel.add(linkTo(methodOn(PedidoControllerV2.class)
            .obtenerPorEstado(pedido.getEstado())).withRel("pedidos-mismo-estado"));
        
        // Enlace a pedidos de la misma ciudad
        if (pedido.getCiudadEnvio() != null) {
            pedidoModel.add(linkTo(methodOn(PedidoControllerV2.class)
                .obtenerPorCiudad(pedido.getCiudadEnvio())).withRel("pedidos-misma-ciudad"));
        }
        
        // 🔗 Enlaces a estadísticas relacionadas
        
        // Total de compras del cliente
        pedidoModel.add(linkTo(methodOn(PedidoControllerV2.class)
            .totalComprasPorCliente(pedido.getClienteId())).withRel("total-compras-cliente"));
        
        // Cantidad de pedidos del cliente
        pedidoModel.add(linkTo(methodOn(PedidoControllerV2.class)
            .cantidadPedidosPorCliente(pedido.getClienteId())).withRel("cantidad-pedidos-cliente"));
        
        return pedidoModel;
    }
    
    /**
     * Método auxiliar para convertir una colección de pedidos
     * Útil para endpoints que retornan listas
     */
    public EntityModel<Pedido> toModelSimple(Pedido pedido) {
        // Versión simplificada con solo enlaces básicos (para listas)
        EntityModel<Pedido> pedidoModel = EntityModel.of(pedido);
        
        pedidoModel.add(linkTo(methodOn(PedidoControllerV2.class)
            .obtenerPorId(pedido.getPedidoId())).withSelfRel());
        
        pedidoModel.add(linkTo(methodOn(PedidoControllerV2.class)
            .obtenerDetalles(pedido.getPedidoId())).withRel("detalles"));
        
        return pedidoModel;
    }
}
