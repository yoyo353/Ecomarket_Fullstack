package com.ecomarket.notificationservice;

import com.ecomarket.notificationservice.controller.NotificationController;
import com.ecomarket.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.springframework.http.MediaType;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService service;

    @Test
    void shouldReturnStockAlert() throws Exception {
        String requestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/request/stock-bajo.json")));

    when(service.checkStock("PROD-001", "Caja ecológica", 20))
    .thenReturn("⚠️ Alerta: Stock bajo para el producto: PROD-001 - Caja ecológica (Stock actual: 20)");


        mockMvc.perform(post("/api/v1/notifications/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("⚠️ Alerta")));
    }
}
