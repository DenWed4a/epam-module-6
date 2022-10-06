package com.epam.esm.security;

import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
/**
 * Class UserDetailServiceImpl
 *
 * @author Dzianis Savastsiuk
 */
@Component("UserDetailServiceImpl")
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        User user = userRepository.getUserByName(login).orElseThrow(
                () -> new UsernameNotFoundException("username  not found")
        );
        return SecurityUser.fromUser(user);
    }
}
