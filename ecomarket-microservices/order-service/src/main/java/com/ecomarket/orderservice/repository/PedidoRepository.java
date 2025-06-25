package com.ecomarket.orderservice.repository;

import com.ecomarket.orderservice.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    
    
    List<Pedido> findByClienteId(Integer clienteId);
    List<Pedido> findByUsuarioId(Integer usuarioId);
    List<Pedido> findByEstado(String estado);
    List<Pedido> findByMetodoPagoId(Integer metodoPagoId);
    List<Pedido> findByCiudadEnvio(String ciudadEnvio);
    List<Pedido> findByTotalBetween(Double totalMin, Double totalMax);
    
    // ✅ NUEVO: Método para contar por estado (necesario para DataLoader)
    Long countByEstado(String estado);
    
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.clienteId = :clienteId")
    Long countByClienteId(@Param("clienteId") Integer clienteId);
    
    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.clienteId = :clienteId AND p.estado = 'COMPLETADO'")
    Double sumTotalByClienteId(@Param("clienteId") Integer clienteId);

    // ✅ NUEVAS CONSULTAS ÚTILES PARA ESTADÍSTICAS
    @Query("SELECT AVG(p.total) FROM Pedido p WHERE p.estado = 'COMPLETADO'")
    Double promedioTotalPedidosCompletados();
    
    @Query("SELECT p.ciudadEnvio, COUNT(p) FROM Pedido p GROUP BY p.ciudadEnvio ORDER BY COUNT(p) DESC")
    List<Object[]> estadisticasPorCiudad();
    
    @Query("SELECT p.estado, COUNT(p) FROM Pedido p GROUP BY p.estado")
    List<Object[]> estadisticasPorEstado();
}

