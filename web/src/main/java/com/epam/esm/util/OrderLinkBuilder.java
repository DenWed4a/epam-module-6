package com.epam.esm.util;

import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.OrderDto;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Class Order link builder
 * @author Dzianis Savastsiuk
 */
@Component
public class OrderLinkBuilder {
    /**
     * Creates self link for OrderDto
     * @param orderDto the OrderDto
     * @return Link
     */
    public Link createOrderSelfLink(OrderDto orderDto){
        return linkTo(OrderController.class).slash(orderDto.getId()).withSelfRel();
    }
}
