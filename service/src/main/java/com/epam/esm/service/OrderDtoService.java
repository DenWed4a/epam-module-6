package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;

import java.util.List;

/**
 * The interface OrderDto service contains methods for business logic with orders.
 * @author Dzianis Savastsiuk
 */
public interface OrderDtoService {
    /**
     * Gets orders with pagination from database
     * @param page page number
     * @param pageSize size of page
     * @return List of OrderDto
     */
    List<OrderDto> getOrders(Integer page, Integer pageSize);
    /**
     * Creates OrderDto
     * @param order the OrderDto
     * @param userId users id
     * @return The OrderDto
     */
    OrderDto create(OrderDto order, Integer userId);
    /**
     * Gets OrderDto by id
     * @param id the id
     * @return The OrderDto
     */
    OrderDto getOrderById(Integer id);

    /**
     * Deletes OrderDto by id
     * @param id the id
     */
    void deleteOrder(Integer id);

}
