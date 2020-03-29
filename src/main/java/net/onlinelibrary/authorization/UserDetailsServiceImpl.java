package net.onlinelibrary.authorization;

import net.onlinelibrary.exception.NotFoundException;
import net.onlinelibrary.model.User;
import net.onlinelibrary.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            throw new NotFoundException("User with username \'" + username + "\' has not found");
        }
        return new UserPrincipalImpl(userOpt.get());
    }
}
