package com.test.hft.application.service.impl;

import com.test.hft.application.dto.request.CreateOrderRequest;
import com.test.hft.application.dto.response.OrderResponse;
import com.test.hft.application.service.OrderService;
import com.test.hft.domain.entity.order.Order;
import com.test.hft.domain.entity.order.StatusOrder;
import com.test.hft.domain.repository.OrderRepository;
import com.test.hft.presentation.exception.BusinessException;
import com.test.hft.presentation.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final Random random = new Random();

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating new order: symbol={}, price={}, quantity={}, sideOrder={}",
                request.getSymbol(), request.getPrice(), request.getQuantity(), request.getSideOrder());

        Order order = new Order(
                request.getSymbol().toUpperCase(),
                request.getPrice(),
                request.getQuantity(),
                request.getSideOrder()
        );

        Order saved = orderRepository.save(order);
        log.info("Order created successfully: id={}, symbol={}, status={}",
                saved.getId(), saved.getSymbol(), saved.getStatusOrder());

        return new OrderResponse(saved);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        log.debug("Fetching order by id={}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND, id));

        return new OrderResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        log.debug("Fetching all orders");
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersByStatus(String status) {
        log.debug("Fetching orders by status={}", status);

        StatusOrder orderStatus;
        try {
            orderStatus = StatusOrder.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS, status);
        }

        return orderRepository.findByStatusOrder(orderStatus)
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersBySymbol(String symbol) {
        log.debug("Fetching orders by symbol={}", symbol);

        return orderRepository.findBySymbol(symbol.toUpperCase())
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse cancelOrder(Long id) {
        log.info("Attempting to cancel order id={}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND, id));

        if (!order.canBeCancelled()) {
            log.warn("Cannot cancel order id={} with status={}", id, order.getStatusOrder());
            throw new BusinessException(ErrorCode.CANNOT_CANCEL_ORDER, order.getStatusOrder());
        }

        order.cancel();
        Order cancelled = orderRepository.save(order);
        log.info("Order cancelled successfully: id={}, symbol={}", cancelled.getId(), cancelled.getSymbol());

        return new OrderResponse(cancelled);
    }

    @Override
    public int simulateExecution() {
        log.info("Executing random orders...");

        List<Order> pendingOrders = orderRepository.findByStatusOrder(StatusOrder.PENDING);
        if (pendingOrders.isEmpty()) {
            log.info("No pending orders found for execution");
            return 0;
        }

        int executedCount = 0;
        for (Order order : pendingOrders) {
            if (random.nextDouble() < 0.6) {
                order.execute();
                orderRepository.save(order);
                executedCount++;
                log.info("Order executed: id={}, symbol={}, price={}, quantity={}",
                        order.getId(), order.getSymbol(), order.getPrice(), order.getQuantity());
            }
        }
        log.info("Random execution completed: {} orders executed out of {}", executedCount, pendingOrders.size());
        return executedCount;
    }
}
