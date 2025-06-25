package com.ecomarket.inventoryservice.controller;

import com.ecomarket.inventoryservice.model.Product;
import com.ecomarket.inventoryservice.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final ProductRepository repo;

    public InventoryController(ProductRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product) {
        return repo.save(product);
    }

    @GetMapping("/{id}")
    public boolean isInStock(@PathVariable String id) {
        return repo.findById(id).map(p -> p.getQuantity() > 0).orElse(false);
    }
}
