package com.epam.esm.service.impl;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.OrderConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.converter.UserConverter;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserDtoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserDtoService.class)
class UserDtoServiceImplTest {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private UserDtoService userDtoService;
    private UserConverter userConverter = Mockito.mock(UserConverter.class);
    private OrderConverter orderConverter = Mockito.mock(OrderConverter.class);
    private CertificateConverter certificateConverter;
    private TagConverter tagConverter;


    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        this.tagConverter = new TagConverter();
        this.certificateConverter = new CertificateConverter(tagConverter);
        this.userDtoService = new UserDtoServiceImpl(userRepository, userConverter, orderConverter, null);

    }

    @Test
    void Should_CallMethodGetUserByIdFromRepository_When_GettingUserDtoById() {

        Set<Tag> tags = new HashSet<>();
        tags.add(Tag.builder().id(1).name("some tag").build());

        Certificate certificate = Certificate.builder().id(1).name("certificateName")
                .lastUpdateDate(LocalDateTime.now())
                .createDate(LocalDateTime.now())
                .duration(10).price(25.5)
                .description("some description").tags(tags).build();
        List<Order> orders = new ArrayList<>();
        orders.add(Order.builder().certificate(certificate).id(1)
                        .user(User.builder().build()).dateOfPurchase(LocalDateTime.now())
                .total(certificate.getPrice()).build());


        Mockito.when(userRepository.getUserById(Mockito.anyInt())).thenReturn(
                Optional.of(User.builder().id(2)
                        .login("user").orders(orders).build()));
        userDtoService.getUserById(1);
        Mockito.verify(userRepository).getUserById(1);
    }

    @Test
    void Should_CallMethodGetAllUsers_When_GettingAllUsers() {
        Mockito.when(userRepository.getAllUsers(1, 5))
                .thenReturn(List.of(new User()));
        userDtoService.getAllUsers(1,5);
        Mockito.when(userConverter.convertToUserDto(
                Mockito.any(User.class))).thenReturn(new UserDto());
        Mockito.verify(userRepository).getAllUsers(1,5);
    }

    @Test
    void Should_CallMethodGetUserOrdersFromRepository_When_GettingUserOrders() {
        List<Order> orders = List.of(new Order());
        Mockito.when(orderConverter.convertToOrderDtoList(orders))
                .thenReturn(List.of(new OrderDto()));
        Mockito.when(userRepository.getUserOrders(1))
                .thenReturn(orders);
        userDtoService.getUserOrders(1);
        Mockito.verify(userRepository).getUserOrders(1);
        Mockito.verify(orderConverter, Mockito.times(1))
                .convertToOrderDtoList(orders);
    }
}