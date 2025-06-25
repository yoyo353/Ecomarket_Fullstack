package com.ecomarket.orderservice.repository;

import com.ecomarket.orderservice.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
    
   
    List<DetallePedido> findByPedidoId(Integer pedidoId);
    List<DetallePedido> findByProductoId(Integer productoId);
    
    
    @Query("SELECT SUM(d.cantidad) FROM DetallePedido d WHERE d.productoId = :productoId")
    Long getTotalCantidadByProducto(@Param("productoId") Integer productoId);
    
    @Query("SELECT d.productoId, SUM(d.cantidad) FROM DetallePedido d GROUP BY d.productoId ORDER BY SUM(d.cantidad) DESC")
    List<Object[]> getProductosMasVendidos();
}