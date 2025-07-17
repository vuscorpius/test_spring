package com.test.hft.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.test.hft.domain.entity.order.Order;
import com.test.hft.domain.entity.order.SideOrder;
import com.test.hft.domain.entity.order.StatusOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * Author: Vuxie
 * Date : 17/07/2025
 */
public class OrderResponse {
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

    public OrderResponse() {
    }

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.symbol = order.getSymbol();
        this.price = order.getPrice();
        this.quantity = order.getQuantity();
        this.sideOrder = order.getSideOrder();
        this.statusOrder = order.getStatusOrder();
        this.createdTime = order.getCreatedTime();
        this.updatedTime = order.getUpdatedTime();
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
}