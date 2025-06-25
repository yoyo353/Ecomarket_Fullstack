package com.ecomarket.inventoryservice.repository;

import com.ecomarket.inventoryservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
