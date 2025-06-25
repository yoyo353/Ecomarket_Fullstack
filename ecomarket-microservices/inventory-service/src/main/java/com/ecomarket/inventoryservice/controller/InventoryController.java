package com.ecomarket.inventoryservice.controller;

import com.ecomarket.inventoryservice.model.Product;
import com.ecomarket.inventoryservice.repository.ProductRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final ProductRepository repo;

    public InventoryController(ProductRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/add")
    public EntityModel<Product> addProduct(@RequestBody Product product) {
        Product saved = repo.save(product);
        return EntityModel.of(saved,
                linkTo(methodOn(InventoryController.class).getProduct(saved.getId())).withSelfRel(),
                linkTo(methodOn(InventoryController.class).isInStock(saved.getId())).withRel("isInStock")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> getProduct(@PathVariable String id) {
        Optional<Product> productOpt = repo.findById(id);
        if (productOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Product product = productOpt.get();
        EntityModel<Product> resource = EntityModel.of(product,
                linkTo(methodOn(InventoryController.class).getProduct(id)).withSelfRel(),
                linkTo(methodOn(InventoryController.class).isInStock(id)).withRel("isInStock")
        );
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/{id}/isInStock")
    public ResponseEntity<Boolean> isInStock(@PathVariable String id) {
        boolean inStock = repo.findById(id).map(p -> p.getQuantity() > 0).orElse(false);
        return ResponseEntity.ok(inStock);
    }
}