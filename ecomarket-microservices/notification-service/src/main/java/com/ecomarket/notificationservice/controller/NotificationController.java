package com.ecomarket.notificationservice.controller;

import com.ecomarket.notificationservice.model.StockRequest;
import com.ecomarket.notificationservice.model.DeliveryStatusRequest;
import com.ecomarket.notificationservice.model.Notification;
import com.ecomarket.notificationservice.service.NotificationService;
import com.ecomarket.notificationservice.assemblers.NotificationModelAssembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/notifications") // versión nueva para endpoints HATEOAS
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationModelAssembler assembler;

    @Autowired
    public NotificationController(NotificationService notificationService, NotificationModelAssembler assembler) {
        this.notificationService = notificationService;
        this.assembler = assembler;
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

    // HATEOAS: Obtener todas las notificaciones con links
    @GetMapping
    public CollectionModel<EntityModel<Notification>> getAllNotifications() {
        List<EntityModel<Notification>> notifications = notificationService.getAllNotifications().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(notifications,
                linkTo(methodOn(NotificationController.class).getAllNotifications()).withSelfRel());
    }

    // HATEOAS: Obtener notificación por ID con links
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Notification>> getById(@PathVariable Long id) {
        return notificationService.getNotificationById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public List<Notification> getByType(@PathVariable String type) {
        return notificationService.getByType(type.toUpperCase());
    }
}
