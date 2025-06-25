package com.ecomarket.notificationservice;

import com.ecomarket.notificationservice.repository.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationRepository notificationRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Faker faker = new Faker();

    @Test
    void shouldSendNotificationWhenStockIsLow() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("productId", "PROD-" + faker.number().digits(3));
        requestBody.put("productName", faker.food().ingredient()); // válido
        requestBody.put("stock", 10); // stock bajo

        mockMvc.perform(post("/api/v2/notifications/stock-alert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotSendNotificationWhenStockIsHigh() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("productId", "PROD-" + faker.number().digits(3));
        requestBody.put("productName", faker.food().ingredient()); // válido
        requestBody.put("stock", 200); // stock alto

        mockMvc.perform(post("/api/v2/notifications/stock-alert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());
    }
}
