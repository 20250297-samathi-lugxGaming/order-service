package com.game.orderservice.dto;

import java.util.List;

public class CreateOrderRequest {
    private String customerName;
    private List<OrderItemRequest> items;

    public static class OrderItemRequest {
        private Long gameId;
        private Integer quantity;

        public Long getGameId() { return gameId; }
        public void setGameId(Long gameId) { this.gameId = gameId; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }
}
