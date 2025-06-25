package com.ecomarket.inventoryservice.service;

import com.ecomarket.inventoryservice.model.Product;
import com.ecomarket.inventoryservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService {

    private final ProductRepository repo;

    public InventoryService(ProductRepository repo) {
        this.repo = repo;
    }

    public Product addProduct(Product product) {
        return repo.save(product);
    }

    public boolean isInStock(String id) {
        Optional<Product> product = repo.findById(id);
        return product.map(p -> p.getQuantity() > 0).orElse(false);
    }
}