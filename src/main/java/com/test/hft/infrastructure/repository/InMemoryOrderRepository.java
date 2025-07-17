package com.test.hft.infrastructure.repository;

import com.test.hft.domain.entity.order.Order;
import com.test.hft.domain.entity.order.StatusOrder;
import com.test.hft.domain.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryOrderRepository implements OrderRepository {

    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Order save(Order order) {
        if (order.getId() == null) {
            order.setId(idGenerator.getAndIncrement());
        }
        orders.put(order.getId(), order);
        return order;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public List<Order> findByStatusOrder(StatusOrder statusOrder) {
        return orders.values()
                .stream()
                .filter(order -> order.getStatusOrder() == statusOrder)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findBySymbol(String symbol) {
        return orders.values()
                .stream()
                .filter(order -> order.getSymbol().equalsIgnoreCase(symbol))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        orders.remove(id);
    }
}
