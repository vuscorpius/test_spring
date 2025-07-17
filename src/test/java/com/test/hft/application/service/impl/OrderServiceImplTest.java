package com.test.hft.application.service.impl;

import com.test.hft.application.dto.request.CreateOrderRequest;
import com.test.hft.application.dto.response.OrderResponse;
import com.test.hft.domain.entity.order.Order;
import com.test.hft.domain.entity.order.SideOrder;
import com.test.hft.domain.entity.order.StatusOrder;
import com.test.hft.domain.repository.OrderRepository;
import com.test.hft.presentation.exception.BusinessException;
import com.test.hft.presentation.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_shouldReturnOrderResponse_whenValidRequest() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setSymbol("FPT");
        request.setPrice(BigDecimal.valueOf(100.5));
        request.setQuantity(10);
        request.setSideOrder(SideOrder.BUY);

        Order savedOrder = new Order("FPT", BigDecimal.valueOf(100.5), 10, SideOrder.BUY);
        savedOrder.setId(1L);

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals("FPT", response.getSymbol());
        assertEquals(StatusOrder.PENDING, response.getStatusOrder());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getOrderById_shouldThrowBusinessException_whenOrderNotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());
        BusinessException ex = assertThrows(BusinessException.class, () -> orderService.getOrderById(999L));
        assertEquals(ErrorCode.ORDER_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void getOrdersByStatus_shouldThrowBusinessException_whenInvalidStatus() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> orderService.getOrdersByStatus("WRONG_STATUS"));
        assertEquals(ErrorCode.INVALID_ORDER_STATUS, ex.getErrorCode());
    }

    @Test
    void cancelOrder_shouldReturnCancelledOrder_whenPending() {
        Order order = new Order("VCB", BigDecimal.valueOf(50), 5, SideOrder.SELL);
        order.setId(2L);

        when(orderRepository.findById(2L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.cancelOrder(2L);

        assertEquals(StatusOrder.CANCELLED, response.getStatusOrder());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void cancelOrder_shouldThrowBusinessException_whenNotPending() {
        Order executedOrder = new Order("VCB", BigDecimal.valueOf(50), 5, SideOrder.SELL);
        executedOrder.setId(3L);
        executedOrder.execute();

        when(orderRepository.findById(3L)).thenReturn(Optional.of(executedOrder));

        BusinessException ex = assertThrows(BusinessException.class, () -> orderService.cancelOrder(3L));
        assertEquals(ErrorCode.CANNOT_CANCEL_ORDER, ex.getErrorCode());
    }

    @Test
    void simulateExecution_shouldExecuteSomeOrders() {
        Order pending1 = new Order("AAA", BigDecimal.valueOf(10), 1, SideOrder.BUY);
        pending1.setId(4L);
        Order pending2 = new Order("BBB", BigDecimal.valueOf(20), 2, SideOrder.SELL);
        pending2.setId(5L);

        when(orderRepository.findByStatusOrder(StatusOrder.PENDING)).thenReturn(List.of(pending1, pending2));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        int count = orderService.simulateExecution();
        assertTrue(count >= 0 && count <= 2);
        verify(orderRepository, atLeast(0)).save(any(Order.class));
    }
}
