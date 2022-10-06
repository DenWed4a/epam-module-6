package com.epam.esm.repository;

import com.epam.esm.entity.Order;

import java.util.List;
import java.util.Optional;

/**
 * The Interface order repository contains methods for orders
 *
 * @author Dzianis Savastsiuk
 */
public interface OrderRepository {
    /**
     * Gets orders with pagination from database
     * @param page page number
     * @param pageSize size of page
     * @return List of orders
     */
    List<Order> getOrders(Integer page, Integer pageSize);

    /**
     * Creates order
     * @param order the order
     * @param userId users id
     * @return The order
     */
    Order create(Order order, Integer userId);

    /**
     * Gets order by id
     * @param id the id
     * @return Optional of order
     */
    Optional<Order> getOrderById(Integer id);

    /**
     * Deletes order by id
     * @param id the id
     */
    void deleteOrder(Integer id);
}
