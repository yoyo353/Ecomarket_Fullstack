package com.ecomarket.inventoryservice.config;

import com.ecomarket.inventoryservice.model.Product;
import com.ecomarket.inventoryservice.repository.ProductRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        for (int i = 0; i < 20; i++) {
            Product product = new Product();
            product.setId(String.valueOf(i + 1));
            product.setName(faker.commerce().productName());
            product.setQuantity(faker.number().numberBetween(0, 100));
            productRepository.save(product);
        }
    }
}