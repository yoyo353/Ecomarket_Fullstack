package com.ecomarket.notificationservice.model;

public class StockRequest {
    private String productName;
    private int stock;

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
}