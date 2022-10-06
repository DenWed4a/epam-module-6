package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.service.OrderDtoService;
import com.epam.esm.util.OrderLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * The type Order controller
 * @author Dzianis Savastsiuk
 */
@RestController
@RequestMapping("/rest/orders")
public class OrderController {

    @Autowired
    private OrderDtoService orderDtoService;
    @Autowired
    private OrderLinkBuilder orderLinkBuilder;

    /**
     * Gets collection of OrderDto with pagination
     * @param page the page number
     * @param pageSize the page size
     * @return CollectionModel of OrderDto
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public CollectionModel<OrderDto> getOrders(@RequestParam(value = "page", defaultValue = "1", required = false)
                                                   @Min(value = 1, message = "local.positive.field") Integer page,
                                               @RequestParam(value = "pageSize", defaultValue = "5", required = false)
                                               @Min(value = 1, message = "local.positive.field") Integer pageSize){
        List<OrderDto> orders = orderDtoService.getOrders(page, pageSize);
        orders.forEach(orderDto -> orderDto.add(orderLinkBuilder.createOrderSelfLink(orderDto)));
        Link selfLink = linkTo(OrderController.class).withSelfRel();

        return CollectionModel.of(orders, selfLink);
    }

    /**
     * Gets OrderDto by id
     * @param id the id
     * @return OrderDto
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public OrderDto getOrderById(@PathVariable @Min(value = 1, message = "local.positive.field") Integer id){
        OrderDto orderDto = orderDtoService.getOrderById(id);
        orderDto.add(orderLinkBuilder.createOrderSelfLink(orderDto));
        return orderDto;
    }

    /**
     * Deletes OrderDto by id
     * @param id the id
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable @Min(value = 1, message = "local.positive.field") Integer id){
        orderDtoService.deleteOrder(id);
    }
}
