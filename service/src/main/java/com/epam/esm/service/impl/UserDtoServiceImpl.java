package com.epam.esm.service.impl;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.converter.UserConverter;
import com.epam.esm.dto.AuthenticationUserDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.InvalidUsernameException;
import com.epam.esm.exception.ServiceSourceNotFoundException;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserDtoService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@NoArgsConstructor
public class UserDtoServiceImpl implements UserDtoService {


    private UserRepository userRepository;
    private UserConverter userConverter;
    private OrderConverter orderConverter;
    @Autowired
    private RoleRepository roleRepository;
    private  PasswordEncoder passwordEncoder;

    @Autowired
    public UserDtoServiceImpl(UserRepository userRepository,
                              UserConverter userConverter,
                              OrderConverter orderConverter,
                              PasswordEncoder passwordEncoder
    ){
        this.userRepository = userRepository;
        this.orderConverter = orderConverter;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto getUserById(Integer id) {
        return userConverter.convertToUserDto(userRepository.getUserById(id)
                .orElseThrow(() -> new ServiceSourceNotFoundException(id)));
    }

    @Override
    public UserDto getUserByName(String name) {
        User user = userRepository.getUserByName(name).orElseThrow(() -> new ServiceSourceNotFoundException(1));
        return userConverter.convertToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers(Integer page, Integer pageSize) {
        return userConverter.convertToListUsersDto(userRepository.getAllUsers(page, pageSize));
    }

    @Override
    public List<OrderDto> getUserOrders(Integer id) {
        return orderConverter.convertToOrderDtoList(userRepository.getUserOrders(id));
    }

    @Override

    public UserDto register(AuthenticationUserDto userDto) {

        Role role = roleRepository.getRoleByName("USER").orElseThrow(
                () -> new ServiceSourceNotFoundException(1));
        User user = User.builder()
                .login(userDto.getLogin())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(List.of(role))
                .build();
        User registeredUser = userRepository.register(user).orElseThrow(
                () -> new InvalidUsernameException("Username already exist"));
        return userConverter.convertToUserDto(registeredUser);
    }

    @Override
    public AuthenticationUserDto getAuthenticationUserByName(String name) {
        User user = userRepository.getUserByName(name).orElseThrow(() -> new ServiceSourceNotFoundException(1));
        return userConverter.convertToAuthenticationUserDto(user);
    }

    @Override
    public AuthenticationUserDto getAuthenticationUserById(Integer id) {
        User user = userRepository.getUserById(id).orElseThrow(() -> new ServiceSourceNotFoundException(id));
        return userConverter.convertToAuthenticationUserDto(user);
    }


}
