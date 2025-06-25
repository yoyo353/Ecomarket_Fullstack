package com.ecomarket.productservice.service;

// Servicio de negocio para la gestión de productos.
// Centraliza la lógica de negocio, validaciones y operaciones sobre productos.
// Separa la lógica de los controladores para mantener el código limpio y reutilizable.
import com.ecomarket.productservice.model.Producto;
import com.ecomarket.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductRepository productRepository;

    // Devuelve todos los productos registrados en la base de datos.
    // Se usa en endpoints de listado general y para estadísticas.
    public List<Producto> obtenerTodos() {
        return productRepository.findAll();
    }

    // Busca un producto por su ID único.
    // Devuelve null si no existe (para control de errores en el controlador).
    public Producto buscarPorId(Integer id) {
        Optional<Producto> producto = productRepository.findById(id);
        return producto.orElse(null);
    }

    // Busca un producto por su código SKU único.
    // Útil para validaciones de unicidad y búsquedas rápidas.
    public Producto buscarPorSKU(String sku) {
        Optional<Producto> producto = productRepository.findByCodigoSKU(sku);
        return producto.orElse(null);
    }

    // Guarda un nuevo producto o actualiza uno existente.
    // Se usa tanto para crear como para actualizar productos.
    public Producto guardar(Producto producto) {
        return productRepository.save(producto);
    }

    // Actualiza un producto existente por su ID.
    // Si el producto no existe, retorna null (control de errores en el controlador).
    public Producto actualizar(Integer id, Producto productoActualizado) {
        if (productRepository.existsById(id)) {
            productoActualizado.setProductoId(id); // Garantiza que se actualice el registro correcto
            return productRepository.save(productoActualizado);
        }
        return null;
    }

    // Elimina un producto por su ID.
    // Retorna true si se eliminó, false si no existía (para respuesta HTTP adecuada).
    public boolean eliminar(Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Devuelve el total de productos registrados.
    // Útil para estadísticas y reportes.
    public int totalProductos() {
        return (int) productRepository.count();
    }

    // Devuelve todos los productos marcados como ecológicos.
    // Se usa en filtros y estadísticas.
    public List<Producto> obtenerProductosEcologicos() {
        return productRepository.findByEsEcologico(true);
    }

    // Devuelve productos de una categoría específica.
    public List<Producto> obtenerPorCategoria(Integer categoriaId) {
        return productRepository.findByCategoriaId(categoriaId);
    }

    // Devuelve productos de un proveedor y tipo ecológico específico.
    public List<Producto> obtenerPorProveedorYEcologico(Integer proveedorId, Boolean esEcologico) {
        return productRepository.findByProveedorPrincipalIdAndEsEcologico(proveedorId, esEcologico);
    }

    // Devuelve productos dentro de un rango de precio.
    public List<Producto> obtenerPorRangoPrecio(Double precioMin, Double precioMax) {
        return productRepository.findByPrecioUnitarioBetween(precioMin, precioMax);
    }

    // Devuelve productos cuyo precio es mayor a un valor dado.
    public List<Producto> obtenerPorPrecioMayorA(Double precio) {
        return productRepository.findByPrecioUnitarioGreaterThan(precio);
    }

    // Devuelve productos cuyo precio es menor a un valor dado.
    public List<Producto> obtenerPorPrecioMenorA(Double precio) {
        return productRepository.findByPrecioUnitarioLessThan(precio);
    }

    // Busca productos cuyo nombre contenga una cadena (ignorando mayúsculas/minúsculas).
    public List<Producto> buscarPorNombre(String nombre) {
        return productRepository.findByNombreProductoContainingIgnoreCase(nombre);
    }

    // Cuenta cuántos productos son ecológicos.
    public Long contarProductosEcologicos() {
        return productRepository.countByEsEcologico(true);
    }

    // Contar productos no ecológicos
    public Long contarProductosNoEcologicos() {
        return productRepository.countByEsEcologico(false);
    }
}


