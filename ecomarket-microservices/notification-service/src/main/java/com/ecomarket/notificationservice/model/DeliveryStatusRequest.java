package com.ecomarket.notificationservice.model;

public class DeliveryStatusRequest {
    private String orderId;
    private String status;

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}