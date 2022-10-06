package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;

import java.util.List;
import java.util.Optional;
/**
 * The Interface user repository contains methods for users
 *
 * @author Dzianis Savastsiuk
 */
public interface UserRepository {
    /**
     * Gets user by id
     * @param id the id
     * @return Optional of user
     */
    Optional<User> getUserById(Integer id);
    Optional<User> getUserByName(String name);
    Optional<User> register(User user);

    /**
     * Gets users with pagination
     * @param page page number
     * @param pageSize page size
     * @return List of users
     */
    List<User> getAllUsers(Integer page, Integer pageSize);

    /**
     * Gets users orders by id
     * @param id the id
     * @return List of orders
     */
    List<Order> getUserOrders(Integer id);




}
