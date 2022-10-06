package com.epam.esm.service.impl;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.ServiceSourceNotFoundException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.OrderDtoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OrderDtoService.class)
class OrderDtoServiceImplTest {

    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private OrderDtoService orderDtoService;
    private OrderConverter orderConverter = Mockito.mock(OrderConverter.class);
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderDtoService = new OrderDtoServiceImpl(orderRepository, orderConverter);
    }

    @Test
    void Should_CallMethodGetOrdersFromRepository_When_GettingOrders() {
        Mockito.when(orderRepository.getOrders(1,5)).thenReturn(List.of(
                Order.builder().id(1).total(20.5).build()));
        Mockito.when(orderConverter.convertToOrderDtoList(Mockito.anyList())).thenReturn(
                List.of(OrderDto.builder().id(1).total(20.5).build()));
        orderDtoService.getOrders(1,5);
        Mockito.verify(orderRepository).getOrders(1,5);
    }
    @Disabled
    @Test
    void Should_CallMethodCreateFromRepository_When_Creating() {
        Order order = Order.builder().id(1).total(20.5).build();
        OrderDto orderDto = OrderDto.builder().id(1).total(20.5).build();
        Mockito.when(orderRepository.create(order, 1))
                        .thenReturn(order);
        Mockito.when(orderConverter.convertToOrderDto(order)).thenReturn(orderDto);
        Mockito.when(orderConverter.convertToOrder(orderDto)).thenReturn(order);
        orderDtoService.create(orderDto, 1);
        Mockito.verify(orderRepository).create(order, 1);
    }

    @Test
    void Should_ThrowServiceSourceNotFoundException_When_GettingById() throws ServiceSourceNotFoundException{
        Mockito.when(orderRepository.getOrderById(1)).thenReturn(Optional.empty());
        try{
            orderDtoService.getOrderById(1);
        }catch (ServiceSourceNotFoundException e){
            assertEquals(1, e.getSourceId());
        }
    }


}