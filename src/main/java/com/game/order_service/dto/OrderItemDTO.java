package com.game.order_service.dto;

public class OrderItemDTO {
    private Long gameId;
    private int quantity;

    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
