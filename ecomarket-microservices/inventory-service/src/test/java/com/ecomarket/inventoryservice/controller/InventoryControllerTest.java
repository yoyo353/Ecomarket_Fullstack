package com.ecomarket.inventoryservice.controller;

import com.ecomarket.inventoryservice.model.Product;
import com.ecomarket.inventoryservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(InventoryController.class)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository repo;

    @Test
    void testAddProduct() throws Exception {
        Product product = new Product();
        product.setId("1");
        product.setName("Test");
        product.setQuantity(10);

        when(repo.save(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/inventory/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"1\",\"name\":\"Test\",\"quantity\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void testIsInStockTrue() throws Exception {
        Product product = new Product();
        product.setId("1");
        product.setQuantity(5);

        when(repo.findById("1")).thenReturn(Optional.of(product));

        mockMvc.perform(get("/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testIsInStockFalse() throws Exception {
        Product product = new Product();
        product.setId("2");
        product.setQuantity(0);

        when(repo.findById("2")).thenReturn(Optional.of(product));

        mockMvc.perform(get("/inventory/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testIsInStockNotFound() throws Exception {
        when(repo.findById("3")).thenReturn(Optional.empty());

        mockMvc.perform(get("/inventory/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}