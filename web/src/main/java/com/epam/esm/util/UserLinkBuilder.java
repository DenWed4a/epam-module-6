package com.epam.esm.util;

import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDto;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Class User link builder
 * @author  Dzianis Savastsiuk
 */
@Component
public class UserLinkBuilder {
    /**
     * Creates self link for UserDto
     * @param user the UserDto
     * @return Link
     */
    public Link createUserSelfLink(UserDto user){
        return linkTo(UserController.class).slash(user.getId()).withSelfRel();
    }

    /**
     * Creates self link for list of UserDto
     * @param page the page number
     * @param pageSize the page size
     * @return Link
     */
    public Link createUserListSelfLink(Integer page, Integer pageSize){
        return linkTo(methodOn(UserController.class).getAll(page, pageSize)).withSelfRel();
    }

    /**
     * Creates self link for user order
     * @param id the user id
     * @param orderId the order id
     * @return Link
     */
    public Link createUserOrderSelfLink(Integer id, Integer orderId){
        return linkTo(methodOn(UserController.class).getUserOrderById(id,orderId)).withSelfRel();
    }

    /**
     * Creates self link for list of user orders
     * @param id the user id
     * @return Link
     */
    public Link createUserOrdersLink(Integer id){
        return linkTo(methodOn(UserController.class).getUserOrders(id)).withRel("orders");

    }
}
