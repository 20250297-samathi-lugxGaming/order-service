package com.game.order_service.controller;

import com.game.order_service.dto.CreateOrderRequest;
import com.game.order_service.model.Order;
import com.game.order_service.model.OrderItem;
import com.game.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // Mock game service response (replace with real HTTP call if needed)
    private Map<Long, Game> fetchGameDetails(List<Long> gameIds) {
        Map<Long, Game> mockGames = new HashMap<>();
        for (Long id : gameIds) {
            mockGames.put(id, new Game(id, "Game " + id, "Shooter", "2025-01-01", 49.99));
        }
        return mockGames;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        Map<Long, Game> games = fetchGameDetails(request.getItems().stream()
                .map(CreateOrderRequest.OrderItemRequest::getGameId).toList());

        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (CreateOrderRequest.OrderItemRequest item : request.getItems()) {
            Game game = games.get(item.getGameId());

            OrderItem orderItem = new OrderItem();
            orderItem.setGameId(game.getId());
            orderItem.setGameName(game.getName());
            orderItem.setGamePrice(game.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
            total += game.getPrice() * item.getQuantity();
        }

        order.setItems(orderItems);
        order.setTotalPrice(total);
        Order saved = orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{name}")
    public List<Order> getOrdersByCustomer(@PathVariable String name) {
        return orderRepository.findByCustomerName(name);
    }

    // Inner class to simulate Game Service
    private static class Game {
        private Long id;
        private String name;
        private String category;
        private String releaseDate;
        private Double price;

        public Game(Long id, String name, String category, String releaseDate, Double price) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.releaseDate = releaseDate;
            this.price = price;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public Double getPrice() { return price; }
    }
}
