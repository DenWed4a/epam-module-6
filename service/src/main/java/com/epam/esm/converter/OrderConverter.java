package com.epam.esm.converter;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Order converter contains method for convert  Order to OrderDto
 * and vise versa
 * @author Dzianis Savastsiuk
 */

@Component
@NoArgsConstructor
public class OrderConverter {


    private CertificateConverter certificateConverter;
    @Autowired
    public OrderConverter(CertificateConverter certificateConverter){
        this.certificateConverter = certificateConverter;
    }
    /**
     * Converts OrderDto to Order
     * @param orderDto the OrderDto
     * @return The Order
     */
    public Order convertToOrder(OrderDto orderDto){
        return Order.builder()
                .id(orderDto.getId())
                .total(orderDto.getTotal())
                .dateOfPurchase(orderDto.getDateOfPurchase())
                //.user(userConverter.convertToUser(orderDto.getUser()))
                .certificate(certificateConverter.convertToCertificate(orderDto.getCertificate()))
                .build();
    }

    /**
     * Converts Order to OrderDto
     * @param order the Order
     * @return OrderDto
     */

    public OrderDto convertToOrderDto(Order order){
        return OrderDto.builder()
                .id(order.getId())
                .total(order.getTotal())
                .dateOfPurchase(order.getDateOfPurchase())
                .certificate(certificateConverter.convertToCertificateDto(order.getCertificate()))
                .userId(order.getUser().getId())
                .build();
    }

    /**
     * Converts List of Orders to List of OrdersDto
     * @param orders List of Orders
     * @return List of OrdersDto
     */
    public List<OrderDto> convertToOrderDtoList(List<Order> orders){
        List<OrderDto> result = new ArrayList<>();
        orders.forEach(order -> result.add(convertToOrderDto(order)));
        return result;
    }

    /**
     * Converts List of OrdersDto to List of Orders
     * @param orders List of OrdersDto
     * @return List of Orders
     */
    public List<Order> convertToOrderList(List<OrderDto> orders){
        List<Order> result = new ArrayList<>();
        orders.forEach(orderDto -> result.add(convertToOrder(orderDto)));
        return result;
    }



}
