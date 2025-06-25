package com.ecomarket.productservice.repository;

// Repositorio Spring Data JPA para la entidad Producto.
// Permite realizar operaciones CRUD y consultas personalizadas sobre la tabla producto.
// Se agregaron métodos específicos para cubrir los filtros y búsquedas requeridos por la lógica de negocio y la API.
import com.ecomarket.productservice.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Producto, Integer> {
    // Busca un producto por su código SKU único.
    // Útil para validaciones de unicidad y búsquedas rápidas.
    Optional<Producto> findByCodigoSKU(String codigoSKU);

    // Devuelve todos los productos que son o no ecológicos.
    // Permite filtrar por el atributo booleano esEcologico.
    List<Producto> findByEsEcologico(Boolean esEcologico);

    // Devuelve productos por categoría (id de categoría).
    List<Producto> findByCategoriaId(Integer categoriaId);

    // Devuelve productos por estado (ej: ACTIVE, INACTIVE, DISCONTINUED).
    List<Producto> findByEstado(String estado);

    // Devuelve productos por proveedor principal (id de proveedor).
    List<Producto> findByProveedorPrincipalId(Integer proveedorPrincipalId);

    // Devuelve productos cuyo precio unitario está entre dos valores.
    // Útil para filtros de rango de precio en la API.
    List<Producto> findByPrecioUnitarioBetween(Double precioMin, Double precioMax);

    // Verifica si existe un producto con un SKU dado (para validaciones de unicidad).
    boolean existsByCodigoSKU(String codigoSKU);

    // ================== MÉTODOS PERSONALIZADOS PARA FILTROS AVANZADOS ==================

    // Devuelve productos de un proveedor que además sean (o no) ecológicos.
    List<Producto> findByProveedorPrincipalIdAndEsEcologico(Integer proveedorId, Boolean esEcologico);

    // Devuelve productos cuyo precio unitario es mayor que un valor dado.
    List<Producto> findByPrecioUnitarioGreaterThan(Double precio);

    // Devuelve productos cuyo precio unitario es menor que un valor dado.
    List<Producto> findByPrecioUnitarioLessThan(Double precio);

    // Busca productos cuyo nombre contenga una cadena (ignorando mayúsculas/minúsculas).
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombre);

    // Cuenta cuántos productos son (o no) ecológicos.
    Long countByEsEcologico(Boolean esEcologico);
}
