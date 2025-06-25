package com.ecomarket.productservice.service;

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

    // Obtener todos los productos
    public List<Producto> obtenerTodos() {
        return productRepository.findAll();
    }

    // Buscar producto por ID
    public Producto buscarPorId(Integer id) {
        Optional<Producto> producto = productRepository.findById(id);
        return producto.orElse(null);
    }

    // Buscar producto por SKU
    public Producto buscarPorSKU(String sku) {
        Optional<Producto> producto = productRepository.findByCodigoSKU(sku);
        return producto.orElse(null);
    }

    // Guardar producto
    public Producto guardar(Producto producto) {
        return productRepository.save(producto);
    }

    // Actualizar producto
    public Producto actualizar(Integer id, Producto productoActualizado) {
        if (productRepository.existsById(id)) {
            productoActualizado.setProductoId(id); // Aseguramos que se actualice el existente
            return productRepository.save(productoActualizado);
        }
        return null;
    }

    // Eliminar producto
    public boolean eliminar(Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Total de productos
    public int totalProductos() {
        return (int) productRepository.count();
    }

    // Productos ecológicos
    public List<Producto> obtenerProductosEcologicos() {
        return productRepository.findByEsEcologico(true);
    }

    // Productos por categoría
    public List<Producto> obtenerPorCategoria(Integer categoriaId) {
        return productRepository.findByCategoriaId(categoriaId);
    }

    // Productos por proveedor y tipo ecológico
    public List<Producto> obtenerPorProveedorYEcologico(Integer proveedorId, Boolean esEcologico) {
        return productRepository.findByProveedorPrincipalIdAndEsEcologico(proveedorId, esEcologico);
    }

    // Productos por rango de precio
    public List<Producto> obtenerPorRangoPrecio(Double precioMin, Double precioMax) {
        return productRepository.findByPrecioUnitarioBetween(precioMin, precioMax);
    }

    // Productos más caros que un precio
    public List<Producto> obtenerPorPrecioMayorA(Double precio) {
        return productRepository.findByPrecioUnitarioGreaterThan(precio);
    }

    // Productos más baratos que un precio
    public List<Producto> obtenerPorPrecioMenorA(Double precio) {
        return productRepository.findByPrecioUnitarioLessThan(precio);
    }

    // Buscar productos por nombre
    public List<Producto> buscarPorNombre(String nombre) {
        return productRepository.findByNombreProductoContainingIgnoreCase(nombre);
    }

    // Contar productos ecológicos
    public Long contarProductosEcologicos() {
        return productRepository.countByEsEcologico(true);
    }

    // Contar productos no ecológicos
    public Long contarProductosNoEcologicos() {
        return productRepository.countByEsEcologico(false);
    }
}


