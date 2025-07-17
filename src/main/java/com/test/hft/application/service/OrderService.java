package com.test.hft.application.service;

import com.test.hft.application.dto.request.CreateOrderRequest;
import com.test.hft.application.dto.response.OrderResponse;

import java.util.List;


public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getAllOrders();

    List<OrderResponse> getOrdersByStatus(String status);

    List<OrderResponse> getOrdersBySymbol(String symbol);

    OrderResponse cancelOrder(Long id);

    int simulateExecution();
}