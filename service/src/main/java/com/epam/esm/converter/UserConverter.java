package com.epam.esm.converter;

import com.epam.esm.dto.AuthenticationUserDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class User converter contains method for convert  User to UserDto
 * and vise versa
 * @author Dzianis Savastsiuk
 */
@Component
@NoArgsConstructor
public class UserConverter {

    OrderConverter orderConverter;
    @Autowired
    public UserConverter(OrderConverter orderConverter){
        this.orderConverter = orderConverter;
    }

    /**
     * Converts UserDto to User
     * @param userDto the UserDto
     * @return User
     */
    public User convertToUser(UserDto userDto){
        return User.builder()
                .id(userDto.getId())
                .login(userDto.getLogin())
                .orders(orderConverter.convertToOrderList(userDto.getOrders()))
                .roles(null)
                .build();
    }

    /**
     * Converts User to UserDto
     * @param user the User
     * @return UserDto
     */
    public UserDto convertToUserDto(User user){

        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .orders(user.getOrders() != null ?
                        orderConverter.convertToOrderDtoList(user.getOrders()) : null)
                .roles(user.getRoles().stream().map(
                        Role::getName).collect(Collectors.toList()))
                .build();
    }

    /**
     * Converts List of UserDto to List of Users
     * @param userDtoList List of UserDto
     * @return List of Users
     */
    public List<User> convertToListUsers(List<UserDto> userDtoList){
        List<User> result = new ArrayList<>();
        userDtoList.forEach(userDto -> result.add(convertToUser(userDto)));
        return result;
    }

    /**
     * Converts List of Users to List of UserDto
     * @param users List of Users
     * @return List of UserDto
     */
    public List<UserDto> convertToListUsersDto(List<User> users){
        List<UserDto> result = new ArrayList<>();
        users.forEach(user -> result.add(convertToUserDto(user)));
        return result;
    }

    public AuthenticationUserDto convertToAuthenticationUserDto(User user){
        return AuthenticationUserDto.builder().id(user.getId())
                .login(user.getLogin()).password(user.getPassword()).build();
    }
}
