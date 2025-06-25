package com.ecomarket.notificationservice.service;

import com.ecomarket.notificationservice.model.Notification;
import com.ecomarket.notificationservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repository;

    public String checkStock(String productId, String productName, int stock) {
        String msg;
        if (stock < 100) {
            msg = "⚠️ Alerta: Stock bajo para el producto: " + productId + " - " + productName +
                  " (Stock actual: " + stock + ")";
            saveNotification("STOCK_ALERT", msg, productId);
        } else {
            msg = "✅ Stock suficiente para el producto: " + productId + " - " + productName +
                  " (Stock actual: " + stock + ")";
        }
        return msg;
    }

    public String notifyDeliveryStatus(String productId, String productName, String status) {
        String msg;
        switch (status.toLowerCase()) {
            case "preparacion":
                msg = "📦 Producto " + productId + " - " + productName + " está en preparación."; break;
            case "transito":
                msg = "🚚 Producto " + productId + " - " + productName + " está en tránsito."; break;
            case "entregado":
                msg = "✅ Producto " + productId + " - " + productName + " ha sido entregado."; break;
            default:
                msg = "⚠️ Estado desconocido para el producto " + productId + ": " + status;
        }
        saveNotification("DELIVERY_UPDATE", msg, productId);
        return msg;
    }

    public void saveNotification(String type, String message, String productId) {
        Notification n = new Notification();
        n.setType(type);
        n.setMessage(message);
        n.setTimestamp(LocalDateTime.now());
        n.setProductId(productId);
        repository.save(n);
    }

    public List<Notification> getAllNotifications() {
        return repository.findAll();
    }

    public Optional<Notification> getNotificationById(Long id) {
        return repository.findById(id);
    }

    public List<Notification> getByType(String type) {
        return repository.findByType(type);
    }
}
