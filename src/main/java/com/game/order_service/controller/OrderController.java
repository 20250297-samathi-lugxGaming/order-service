package com.game.order_service.controller;

import com.game.order_service.model.Order;
import com.game.order_service.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    private final OrderRepository repo;

    public OrderController(OrderRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return repo.findAll();
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return repo.save(order);
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order updated) {
        Order o = repo.findById(id).orElseThrow();
        o.setCustomerName(updated.getCustomerName());
        o.setItem(updated.getItem());
        o.setTotalPrice(updated.getTotalPrice());
        return repo.save(o);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
