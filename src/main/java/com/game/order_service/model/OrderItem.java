package com.game.order_service.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long gameId;
    private String gameName;
    private int quantity;
    private BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // Getters and Setters
    public Long getId() { return id; }
    public Long getGameId() { return gameId; }
    public String getGameName() { return gameName; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public Order getOrder() { return order; }

    public void setId(Long id) { this.id = id; }
    public void setGameId(Long gameId) { this.gameId = gameId; }
    public void setGameName(String gameName) { this.gameName = gameName; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public void setOrder(Order order) { this.order = order; }
}
