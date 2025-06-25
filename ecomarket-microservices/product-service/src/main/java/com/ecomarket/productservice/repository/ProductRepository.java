package com.ecomarket.productservice.repository;

import com.ecomarket.productservice.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Producto, Integer> {
    
    Optional<Producto> findByCodigoSKU(String codigoSKU);
    
    List<Producto> findByEsEcologico(Boolean esEcologico);
    
    List<Producto> findByCategoriaId(Integer categoriaId);
    
    List<Producto> findByEstado(String estado);
    
    List<Producto> findByProveedorPrincipalId(Integer proveedorPrincipalId);
    
    List<Producto> findByPrecioUnitarioBetween(Double precioMin, Double precioMax);
    
    boolean existsByCodigoSKU(String codigoSKU);

    // NUEVOS MÉTODOS PERSONALIZADOS
    List<Producto> findByProveedorPrincipalIdAndEsEcologico(Integer proveedorId, Boolean esEcologico);
    List<Producto> findByPrecioUnitarioGreaterThan(Double precio);
    List<Producto> findByPrecioUnitarioLessThan(Double precio);
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombre);
    Long countByEsEcologico(Boolean esEcologico);
}
