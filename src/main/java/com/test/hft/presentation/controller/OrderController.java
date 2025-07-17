package com.test.hft.presentation.controller;

import com.test.hft.application.dto.request.CreateOrderRequest;
import com.test.hft.application.dto.response.BaseResponse;
import com.test.hft.application.dto.response.OrderResponse;
import com.test.hft.application.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@Validated
@Tag(name = "Order Management", description = "APIs for managing trading orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final MessageSource messageSource;

    public OrderController(OrderService orderService, MessageSource messageSource) {
        this.orderService = orderService;
        this.messageSource = messageSource;
    }

    @PostMapping
    @Operation(summary = "Create a new order")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<BaseResponse<OrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            Locale locale) {
        logger.info("Create order: {}", request.getSymbol());
        OrderResponse response = orderService.createOrder(request);
        String message = messageSource.getMessage("order.create.success", null, locale);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.ok(response, message));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<BaseResponse<OrderResponse>> getOrderById(
            @Parameter(description = "Order ID") @PathVariable @NotNull Long id,
            Locale locale) {
        logger.debug("Get order by id: {}", id);
        OrderResponse response = orderService.getOrderById(id);
        String message = messageSource.getMessage("order.get.success", null, locale);
        return ResponseEntity.ok(BaseResponse.ok(response, message));
    }

    @GetMapping
    @Operation(summary = "Get list of orders")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    })
    public ResponseEntity<BaseResponse<List<OrderResponse>>> getAllOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String symbol,
            Locale locale) {
        List<OrderResponse> response;
        if (status != null && !status.isEmpty()) {
            response = orderService.getOrdersByStatus(status);
        } else if (symbol != null && !symbol.isEmpty()) {
            response = orderService.getOrdersBySymbol(symbol);
        } else {
            response = orderService.getAllOrders();
        }
        String message = messageSource.getMessage("order.list.success", null, locale);
        return ResponseEntity.ok(BaseResponse.ok(response, message));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel an order (if PENDING)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Order cannot be cancelled"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<BaseResponse<OrderResponse>> cancelOrder(
            @Parameter(description = "Order ID") @PathVariable @NotNull Long id,
            Locale locale) {
        logger.info("Cancel order: {}", id);
        OrderResponse response = orderService.cancelOrder(id);
        String message = messageSource.getMessage("order.cancel.success", null, locale);
        return ResponseEntity.ok(BaseResponse.ok(response, message));
    }

    @PostMapping("/simulate-execution")
    @Operation(summary = "Simulate random execution for PENDING orders")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Simulation executed successfully")
    })
    public ResponseEntity<BaseResponse<Map<String, Object>>> simulateExecution(Locale locale) {
        logger.info("Simulate execution");
        int executedCount = orderService.simulateExecution();
        Map<String, Object> data = Map.of("executedOrdersCount", executedCount);
        String message = messageSource.getMessage("order.execute.random.success", null, locale);
        return ResponseEntity.ok(BaseResponse.ok(data, message));
    }
}
