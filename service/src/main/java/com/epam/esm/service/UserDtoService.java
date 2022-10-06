package com.epam.esm.service;

import com.epam.esm.dto.AuthenticationUserDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;

import java.util.List;

/**
 * The interface UserDto service contains methods for business logic with users.
 * @author Dzianis Savastsiuk
 */
public interface UserDtoService {
    /**
     * Gets UserDto by id
     * @param id the id
     * @return UserDto
     */
    UserDto getUserById(Integer id);

    UserDto getUserByName(String name);
    /**
     * Gets UsersDto with pagination
     * @param page page number
     * @param pageSize page size
     * @return List of UsersDto
     */
    List<UserDto> getAllUsers(Integer page, Integer pageSize);
    /**
     * Gets users OrdersDto by id
     * @param id the id
     * @return List of OrdersDto
     */
    List<OrderDto> getUserOrders(Integer id);

    UserDto register(AuthenticationUserDto userDto);
    AuthenticationUserDto getAuthenticationUserByName(String name);
    AuthenticationUserDto getAuthenticationUserById(Integer id);


}
