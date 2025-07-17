package com.test.hft.domain.repository;

 import com.test.hft.domain.entity.order.Order;
 import com.test.hft.domain.entity.order.StatusOrder;
 import org.springframework.stereotype.Repository;

 import java.util.List;
 import java.util.Optional;

/**
 * Author: Vuxie
 * Date : 17/07/2025
 */
@Repository
public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    List<Order> findByStatusOrder(StatusOrder statusOrder);

    List<Order> findBySymbol(String symbol);

    void deleteById(Long id);
}
