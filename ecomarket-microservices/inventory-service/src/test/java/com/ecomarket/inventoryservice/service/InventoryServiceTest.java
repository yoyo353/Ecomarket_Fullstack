package com.ecomarket.inventoryservice.service;

import com.ecomarket.inventoryservice.model.Product;
import com.ecomarket.inventoryservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventoryServiceTest {

    @Test
    void testAddProduct() {
        ProductRepository repo = mock(ProductRepository.class);
        InventoryService service = new InventoryService(repo);

        Product product = new Product();
        product.setId("1");
        product.setName("Test");
        product.setQuantity(10);

        when(repo.save(any(Product.class))).thenReturn(product);

        Product result = service.addProduct(product);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Test", result.getName());
        assertEquals(10, result.getQuantity());
    }

    @Test
    void testIsInStockTrue() {
        ProductRepository repo = mock(ProductRepository.class);
        InventoryService service = new InventoryService(repo);

        Product product = new Product();
        product.setId("1");
        product.setQuantity(5);

        when(repo.findById("1")).thenReturn(Optional.of(product));

        assertTrue(service.isInStock("1"));
    }

    @Test
    void testIsInStockFalse() {
        ProductRepository repo = mock(ProductRepository.class);
        InventoryService service = new InventoryService(repo);

        Product product = new Product();
        product.setId("2");
        product.setQuantity(0);

        when(repo.findById("2")).thenReturn(Optional.of(product));

        assertFalse(service.isInStock("2"));
    }

    @Test
    void testIsInStockNotFound() {
        ProductRepository repo = mock(ProductRepository.class);
        InventoryService service = new InventoryService(repo);

        when(repo.findById("3")).thenReturn(Optional.empty());

        assertFalse(service.isInStock("3"));
    }
}