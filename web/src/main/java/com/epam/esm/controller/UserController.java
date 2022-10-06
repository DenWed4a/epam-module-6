package com.epam.esm.controller;

import com.epam.esm.dto.*;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.OrderDtoService;
import com.epam.esm.service.UserDtoService;
import com.epam.esm.util.UserLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * The type User controller
 * @author Dzianis Savastsiuk
 */
@RestController
@RequestMapping("/rest/users")
public class UserController {
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserDtoService userDtoService;
    @Autowired
    OrderDtoService orderDtoService;
    @Autowired
    UserLinkBuilder userLinkBuilder;

    public UserController() {
    }


    /**
     * Gets collection of UserDto with pagination
     * @param page the page number
     * @param pageSize the page size
     * @return CollectionModel of UserDto
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public CollectionModel<UserDto> getAll(@RequestParam(value = "page", defaultValue = "1", required = false) @Min(value = 1, message = "local.positive.field") Integer page,
                                           @RequestParam(value = "pageSize", defaultValue = "5", required = false) @Min(value = 1, message = "local.positive.field") Integer pageSize){
        List<UserDto> users = userDtoService.getAllUsers(page, pageSize);
        users.forEach(userDto -> userDto.add(userLinkBuilder.createUserSelfLink(userDto)));
        Link selfLink = userLinkBuilder.createUserListSelfLink(page, pageSize);

        return CollectionModel.of(users, selfLink);
    }

    /**
     * Gets UserDto by id
     * @param id the id
     * @return UserDto
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public UserDto getUserById(@PathVariable @Min(value = 1, message = "local.positive.field") Integer id){
        UserDto userDto = userDtoService.getUserById(id);
        userDto.add(userLinkBuilder.createUserSelfLink(userDto));
        userDto.add(userLinkBuilder.createUserOrdersLink(id));

        return userDto;
    }

    /**
     * Gets collection of user orders
     * @param id the user id
     * @return CollectionModel of OrderDto
     */
    @GetMapping("/{id}/orders")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public CollectionModel<OrderDto> getUserOrders(@PathVariable @Min(value = 1, message = "local.positive.field") Integer id){
        List<OrderDto> orders = userDtoService.getUserOrders(id);
        orders.forEach(orderDto ->  orderDto.add(userLinkBuilder.createUserOrderSelfLink(id, orderDto.getId())));
        return CollectionModel.of(orders);
    }

    /**
     * Gets user order by id
     * @param id the user id
     * @param orderId the order id
     * @return ResponseEntity of OrderDto
     */
    @GetMapping("/{id}/orders/{orderId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<OrderDto> getUserOrderById(@PathVariable @Min(value = 1, message = "local.positive.field") Integer id,
                                         @PathVariable  @Min(value = 1, message = "local.positive.field") Integer orderId){
        ResponseEntity<OrderDto> result;
        OrderDto orderDto = orderDtoService.getOrderById(orderId);
        orderDto.add(userLinkBuilder.createUserOrderSelfLink(id, orderId));

        if(Objects.equals(orderDto.getUserId(), id)){
            result = new ResponseEntity<>(orderDto, HttpStatus.OK);
        }else{
            result = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return result;
    }

    /**
     * Creates order and adds to user
     * @param id the user id
     * @param certificateDto the CertificateDto
     * @return ResponseEntity of UserDto
     */
    @PostMapping("/{id}/orders")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<UserDto> buyCertificate(@PathVariable @Min(value = 1, message = "local.positive.field") Integer id,
                                                  @Valid @RequestBody CertificateDto certificateDto, Principal principal){

        AuthenticationUserDto user = userDtoService.getAuthenticationUserById(id);
        if(!user.getLogin().equals(principal.getName())){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

         OrderDto orderDto = OrderDto.builder()
                .certificate(certificateDto)
                .userId(id)
                .dateOfPurchase(LocalDateTime.now())
                .total(certificateDto.getPrice())
                .build();
        orderDtoService.create(orderDto, id);
        UserDto userDto = userDtoService.getUserById(id);
        userDto.add(userLinkBuilder.createUserSelfLink(userDto));
        userDto.add(userLinkBuilder.createUserOrdersLink(id));

         return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public AuthenticationResponseDto authenticate(@RequestBody AuthenticationUserDto authenticationUserDto){
        String login = authenticationUserDto.getLogin();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, authenticationUserDto.getPassword()));
        AuthenticationUserDto user = userDtoService.getAuthenticationUserByName(login);
        String token = jwtTokenProvider.createToken(user.getLogin());

        return AuthenticationResponseDto.builder().login(user.getLogin()).token(token).build();
    }

    @PostMapping("/signup")
    public ResponseEntity register(@RequestBody AuthenticationUserDto userDto){

        return ResponseEntity.ok(userDtoService.register(userDto));
    }


}
