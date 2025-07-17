package com.test.hft.domain.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Author: Vuxie
 * Date : 17/07/2025
 */
public class Order {
    private Long id;
    private String symbol;
    private BigDecimal price;
    private Integer quantity;
    private SideOrder sideOrder;
    private StatusOrder statusOrder;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    public Order() {}

    public Order(String symbol, BigDecimal price, Integer quantity, SideOrder sideOrder) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.sideOrder = sideOrder;
        this.statusOrder = StatusOrder.PENDING;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public StatusOrder getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(StatusOrder statusOrder) {
        this.statusOrder = statusOrder;
        this.updatedTime = LocalDateTime.now();
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public boolean canBeCancelled() {
        return StatusOrder.PENDING.equals(this.statusOrder);
    }

    public void cancel() {
        if (!canBeCancelled()) {
            throw new IllegalStateException("Cannot cancel order with status: " + this.statusOrder);
        }
        this.statusOrder = StatusOrder.CANCELLED;
        this.updatedTime = LocalDateTime.now();
    }

    public void execute() {
        if (!StatusOrder.PENDING.equals(this.statusOrder)) {
            throw new IllegalStateException("Cannot execute order with status: " + this.statusOrder);
        }
        this.statusOrder = StatusOrder.EXECUTED;
        this.updatedTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("Order{id=%d, symbol='%s', price=%s, quantity=%d, sideOrder=%s, statusOrder=%s, createdTime=%s}",
                id, symbol, price, quantity, sideOrder, statusOrder, createdTime);
    }
}
