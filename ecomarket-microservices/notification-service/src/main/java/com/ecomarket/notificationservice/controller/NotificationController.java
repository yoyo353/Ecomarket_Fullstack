package com.ecomarket.notificationservice.controller;

import com.ecomarket.notificationservice.model.StockRequest;
import com.ecomarket.notificationservice.model.DeliveryStatusRequest;
import com.ecomarket.notificationservice.model.Notification;
import com.ecomarket.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/stock")
    public String handleStockUpdate(@RequestBody StockRequest request) {
        return notificationService.checkStock(
            request.getProductId(),
            request.getProductName(),
            request.getStock()
        );
    }

    @PutMapping("/stock")
    public String updateStock(@RequestBody StockRequest request) {
        return notificationService.checkStock(
            request.getProductId(),
            request.getProductName(),
            request.getStock()
        );
    }

    @PostMapping("/delivery")
    public String notifyDeliveryStatus(@RequestBody DeliveryStatusRequest request) {
        return notificationService.notifyDeliveryStatus(
            request.getProductId(),
            request.getProductName(),
            request.getStatus()
        );
    }

    @GetMapping
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/{id}")
    public Optional<Notification> getById(@PathVariable Long id) {
        return notificationService.getNotificationById(id);
    }

    @GetMapping("/type/{type}")
    public List<Notification> getByType(@PathVariable String type) {
        return notificationService.getByType(type.toUpperCase());
    }
}