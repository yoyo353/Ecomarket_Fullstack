package com.ecomarket.notificationservice.controller;

import com.ecomarket.notificationservice.model.StockRequest;
import com.ecomarket.notificationservice.model.DeliveryStatusRequest;
import com.ecomarket.notificationservice.model.Notification;
import com.ecomarket.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/stock-alert")
    public ResponseEntity<String> handleStockUpdate(@RequestBody StockRequest request) {
        String msg = notificationService.checkStock(
            request.getProductId(),
            request.getProductName(),
            request.getStock()
        );
        return ResponseEntity.ok(msg);
    }

    @PutMapping("/stock-alert")
    public ResponseEntity<String> updateStock(@RequestBody StockRequest request) {
        String msg = notificationService.checkStock(
            request.getProductId(),
            request.getProductName(),
            request.getStock()
        );
        return ResponseEntity.ok(msg);
    }

    @PostMapping("/delivery")
    public ResponseEntity<String> notifyDeliveryStatus(@RequestBody DeliveryStatusRequest request) {
        String msg = notificationService.notifyDeliveryStatus(
            request.getProductId(),
            request.getProductName(),
            request.getStatus()
        );
        return ResponseEntity.ok(msg);
    }

    @GetMapping
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getById(@PathVariable Long id) {
        return notificationService.getNotificationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public List<Notification> getByType(@PathVariable String type) {
        return notificationService.getByType(type.toUpperCase());
    }
}
