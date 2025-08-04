package com.game.order_service.controller;

import com.game.order_service.dto.OrderItemDTO;
import com.game.order_service.dto.OrderRequest;
import com.game.order_service.model.Order;
import com.game.order_service.model.OrderItem;
import com.game.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository repo;

    @Autowired
    private RestTemplate restTemplate;

    // ✅ CREATE order using OrderRequest with game price lookup
    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest request) {
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDTO dto : request.getItems()) {
            // Call GameService to get game details
            String url = "http://game-service:8086/api/games/" + dto.getGameId();
            ResponseEntity<Game> gameResponse = restTemplate.getForEntity(url, Game.class);

            if (!gameResponse.getStatusCode().is2xxSuccessful() || gameResponse.getBody() == null) {
                return ResponseEntity.badRequest().body("Invalid game ID: " + dto.getGameId());
            }

            Game game = gameResponse.getBody();
            BigDecimal unitPrice = BigDecimal.valueOf(game.getPrice());
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(dto.getQuantity()));
            total = total.add(lineTotal);

            OrderItem item = new OrderItem();
            item.setGameId(game.getId());
            item.setGameName(game.getName());
            item.setQuantity(dto.getQuantity());
            item.setUnitPrice(unitPrice);
            orderItems.add(item);
        }

        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setTotalPrice(total);
        order.setItems(orderItems);
        orderItems.forEach(i -> i.setOrder(order));

        repo.save(order);

        return ResponseEntity.ok("Order created. Total = $" + total);
    }

    // ✅ GET all orders with items
    @GetMapping
    public List<Order> getAllOrders() {
        return repo.findAll();
    }

    // ✅ GET single order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ DELETE order by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.ok("Order deleted.");
    }

    // Helper DTO for game info
    private static class Game {
        private Long id;
        private String name;
        private Double price;

        public Long getId() { return id; }
        public String getName() { return name; }
        public Double getPrice() { return price; }
    }
}
