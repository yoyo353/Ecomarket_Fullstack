package com.ecomarket.inventoryservice.service;

import com.ecomarket.inventoryservice.model.Inventario;
import com.ecomarket.inventoryservice.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    public List<Inventario> obtenerTodos() {
        return inventarioRepository.findAll();
    }

    public Inventario buscarPorId(Integer id) {
        return inventarioRepository.findById(id).orElse(null);
    }

    public Inventario guardar(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    public Inventario actualizar(Integer id, Inventario actualizado) {
        if (inventarioRepository.existsById(id)) {
            actualizado.setInventarioId(id);
            return inventarioRepository.save(actualizado);
        }
        return null;
    }

    public boolean eliminar(Integer id) {
        if (inventarioRepository.existsById(id)) {
            inventarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Inventario> obtenerPorProducto(Integer productoId) {
        return inventarioRepository.findByProductoId(productoId);
    }
}
