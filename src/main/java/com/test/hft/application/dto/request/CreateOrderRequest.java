package com.test.hft.application.dto.request;

import com.test.hft.domain.entity.order.SideOrder;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Author: Vuxie
 * Date : 17/07/2025
 */

public class CreateOrderRequest {
    @NotBlank(message = "Symbol is required")
    @Size(min = 1, max = 10, message = "Symbol must be between 1 and 10 characters")
    @Pattern(regexp = "^[A-Z]{1,10}$", message = "Symbol must contain only uppercase letters")
    private String symbol;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must have maximum 2 decimal places")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 1000000, message = "Quantity must not exceed 1,000,000")
    private Integer quantity;

    @NotNull(message = "Side is required")
    private SideOrder sideOrder;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(String symbol, BigDecimal price, Integer quantity, SideOrder sideOrder) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.sideOrder = sideOrder;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public SideOrder getSideOrder() {
        return sideOrder;
    }

    public void setSideOrder(SideOrder sideOrder) {
        this.sideOrder = sideOrder;
    }
}
