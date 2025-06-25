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
}


