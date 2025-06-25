package com.ecomarket.orderservice.assembler;

import com.ecomarket.orderservice.controller.PedidoControllerV2;
import com.ecomarket.orderservice.model.DetallePedido;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Assembler para convertir entidades DetallePedido en modelos HATEOAS
 * 
 * Este assembler agrega enlaces hipermedia a los detalles de pedidos,
 * facilitando la navegación entre detalles, pedidos y productos relacionados.
 */
@Component
public class DetallePedidoModelAssembler implements RepresentationModelAssembler<DetallePedido, EntityModel<DetallePedido>> {

    @Override
    public EntityModel<DetallePedido> toModel(DetallePedido detalle) {
        // Crear el modelo de entidad con enlaces HATEOAS
        EntityModel<DetallePedido> detalleModel = EntityModel.of(detalle);
        
        // 🔗 Enlace a los detalles del pedido (self, ya que no hay endpoint individual)
        detalleModel.add(linkTo(methodOn(PedidoControllerV2.class)
            .obtenerDetalles(detalle.getPedidoId())).withSelfRel());
        
        // 🔗 Enlace al pedido padre
        detalleModel.add(linkTo(methodOn(PedidoControllerV2.class)
            .obtenerPorId(detalle.getPedidoId())).withRel("pedido"));
        
        // 🔗 Enlace a todos los detalles del mismo pedido
        detalleModel.add(linkTo(methodOn(PedidoControllerV2.class)
            .obtenerDetalles(detalle.getPedidoId())).withRel("detalles-pedido"));
        
        // 🔗 Enlaces relacionados con el producto
        // Si existe un ProductoControllerV2, se recomienda crear el enlace así:
        // detalleModel.add(linkTo(methodOn(ProductoControllerV2.class)
        //     .obtenerPorId(detalle.getProductoId())).withRel("producto"));
        // Como no existe, dejamos el enlace comentado o lo omitimos.
        // Si quieres un enlace genérico, puedes usar un template o dejarlo documentado.
        // Ejemplo de enlace genérico (no funcional si no existe el endpoint):
        // detalleModel.add(linkTo(PedidoControllerV2.class).slash("productos").slash(detalle.getProductoId()).withRel("producto"));
        // Mejor: dejar comentario para implementar en el futuro.
        
        // 🔗 Enlaces a estadísticas del producto
        // Si existe un endpoint para buscar detalles por producto, crea el enlace así:
        // detalleModel.add(linkTo(methodOn(PedidoControllerV2.class)
        //     .obtenerDetallesPorProducto(detalle.getProductoId())).withRel("otros-detalles-producto"));
        // Si no existe, omitir o dejar comentario.
        
        return detalleModel;
    }
    
    /**
     * Método auxiliar para crear modelos simplificados
     * Útil cuando se retornan listas de detalles
     */
    public EntityModel<DetallePedido> toModelSimple(DetallePedido detalle) {
        EntityModel<DetallePedido> detalleModel = EntityModel.of(detalle);
        
        // Solo enlaces básicos para listas
        detalleModel.add(linkTo(methodOn(PedidoControllerV2.class)
            .obtenerPorId(detalle.getPedidoId())).withRel("pedido"));
        // Enlace al producto: solo si existe el endpoint real
        // detalleModel.add(linkTo(methodOn(ProductoControllerV2.class)
        //     .obtenerPorId(detalle.getProductoId())).withRel("producto"));
        return detalleModel;
    }
}
