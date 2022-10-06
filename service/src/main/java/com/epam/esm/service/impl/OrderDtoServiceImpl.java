package com.epam.esm.service.impl;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.ServiceSourceNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.OrderDtoService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class OrderDtoServiceImpl implements OrderDtoService {


    private OrderRepository orderRepository;
    private OrderConverter orderConverter;
    @Autowired
    CertificateRepository certificateRepository;
    @Autowired
    public OrderDtoServiceImpl(OrderRepository orderRepository, OrderConverter orderConverter){
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
    }

    @Override
    public List<OrderDto> getOrders(Integer page, Integer pageSize) {
        return orderConverter.convertToOrderDtoList(orderRepository.getOrders(page, pageSize));
    }

    @Override
    public OrderDto create(OrderDto order, Integer userId) {
        certificateRepository.getCertificate(order.getCertificate().getId()).orElseThrow(
                () -> new ServiceSourceNotFoundException(order.getCertificate().getId())
        );
        return orderConverter.convertToOrderDto(orderRepository.create(orderConverter.convertToOrder(order), userId));
    }

    @Override
    public OrderDto getOrderById(Integer id) {
        return orderConverter.convertToOrderDto(orderRepository.getOrderById(id)
                .orElseThrow(() -> new ServiceSourceNotFoundException(id)));
    }

    @Override
    public void deleteOrder(Integer id) {
        orderRepository.deleteOrder(id);
    }


}
