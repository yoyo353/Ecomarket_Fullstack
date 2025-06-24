package com.ecomarket.notificationservice.controller;

import com.ecomarket.notificationservice.model.StockRequest;
import com.ecomarket.notificationservice.model.DeliveryStatusRequest;
import com.ecomarket.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/stock")
    public String checkStock(@RequestBody StockRequest request) {
        return notificationService.checkStock(request.getProductName(), request.getStock());
    }

    @PostMapping("/delivery")
    public String notifyDeliveryStatus(@RequestBody DeliveryStatusRequest request) {
        return notificationService.notifyDeliveryStatus(request.getOrderId(), request.getStatus());
    }
}