package com.ecomarket.notificationservice.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public String checkStock(String productName, int stock) {
        if (stock < 100) {
            return "⚠️ Alerta: Stock bajo para el producto: " + productName + " (Stock actual: " + stock + ")";
        } else {
            return "✅ Stock suficiente para el producto: " + productName + " (Stock actual: " + stock + ")";
        }
    }

    public String notifyDeliveryStatus(String orderId, String status) {
        switch (status.toLowerCase()) {
            case "preparacion":
                return "🧑‍🍳 Pedido " + orderId + " está en preparación.";
            case "transito":
                return "🚚 Pedido " + orderId + " está en tránsito.";
            case "entregado":
                return "📦 Pedido " + orderId + " ha sido entregado.";
            default:
                return "⚠️ Estado desconocido para el pedido " + orderId + ": " + status;
        }
    }
}