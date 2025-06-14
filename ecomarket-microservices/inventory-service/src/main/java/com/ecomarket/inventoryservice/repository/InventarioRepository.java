package com.ecomarket.inventoryservice.repository;

import com.ecomarket.inventoryservice.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
    List<Inventario> findByProductoId(Integer productoId);
    List<Inventario> findByEstado(String estado);
}
