package com.ecomarket.orderservice.service;

import com.ecomarket.orderservice.model.Pedido;
import com.ecomarket.orderservice.model.DetallePedido;
import com.ecomarket.orderservice.repository.PedidoRepository;
import com.ecomarket.orderservice.repository.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    // Obtener todos los pedidos
    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    // Buscar pedido por ID
    public Pedido buscarPorId(Integer id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        return pedido.orElse(null);
    }

    // Crear nuevo pedido
    @Transactional
    public Pedido crearPedido(Pedido pedido, List<DetallePedido> detalles) {
        // Calcular subtotal y total
        double subtotal = detalles.stream()
                .mapToDouble(detalle -> detalle.getPrecioUnitario() * detalle.getCantidad())
                .sum();
        
        double descuento = pedido.getDescuento() != null ? pedido.getDescuento() : 0.0;
        double total = subtotal - (subtotal * descuento / 100);
        
        pedido.setSubtotal(subtotal);
        pedido.setTotal(total);
        
        // Guardar pedido
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        
        // Guardar detalles
        detalles.forEach(detalle -> {
            detalle.setPedidoId(pedidoGuardado.getPedidoId());
            detalle.setSubTotal(detalle.getPrecioUnitario() * detalle.getCantidad());
            detallePedidoRepository.save(detalle);
        });
        
        return pedidoGuardado;
    }

    // Actualizar pedido
    public Pedido actualizar(Integer id, Pedido pedidoActualizado) {
        if (pedidoRepository.existsById(id)) {
            pedidoActualizado.setPedidoId(id);
            return pedidoRepository.save(pedidoActualizado);
        }
        return null;
    }

    // Actualizar estado del pedido
    public Pedido actualizarEstado(Integer id, String nuevoEstado) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            pedido.setEstado(nuevoEstado);
            return pedidoRepository.save(pedido);
        }
        return null;
    }

    // Eliminar pedido
    @Transactional
    public boolean eliminar(Integer id) {
        if (pedidoRepository.existsById(id)) {
            // Primero eliminar detalles
            List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(id);
            detallePedidoRepository.deleteAll(detalles);
            
            // Luego eliminar pedido
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Obtener pedidos por cliente
    public List<Pedido> obtenerPorCliente(Integer clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    // Obtener pedidos por estado
    public List<Pedido> obtenerPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }

    // Obtener pedidos por usuario
    public List<Pedido> obtenerPorUsuario(Integer usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    // Obtener pedidos por ciudad
    public List<Pedido> obtenerPorCiudad(String ciudad) {
        return pedidoRepository.findByCiudadEnvio(ciudad);
    }

    // Obtener pedidos por rango de total
    public List<Pedido> obtenerPorRangoTotal(Double totalMin, Double totalMax) {
        return pedidoRepository.findByTotalBetween(totalMin, totalMax);
    }

    // Total de pedidos
    public int totalPedidos() {
        return (int) pedidoRepository.count();
    }

    // Total de compras por cliente
    public Double totalComprasPorCliente(Integer clienteId) {
        Double total = pedidoRepository.sumTotalByClienteId(clienteId);
        return total != null ? total : 0.0;
    }

    // Cantidad de pedidos por cliente
    public Long cantidadPedidosPorCliente(Integer clienteId) {
        return pedidoRepository.countByClienteId(clienteId);
    }

    // Obtener detalles de un pedido
    public List<DetallePedido> obtenerDetallesPedido(Integer pedidoId) {
        return detallePedidoRepository.findByPedidoId(pedidoId);
    }

    // Productos más vendidos
    public List<Object[]> obtenerProductosMasVendidos() {
        return detallePedidoRepository.getProductosMasVendidos();
    }
}
