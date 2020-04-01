package net.onlinelibrary.security;

import net.onlinelibrary.exception.UserNotFoundException;
import net.onlinelibrary.model.User;
import net.onlinelibrary.security.jwt.JwtUserDetails;
import net.onlinelibrary.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userService.getByUsername(username);
            return new JwtUserDetails(user);
        } catch (UserNotFoundException e) {
            return null;
        }
    }
}
