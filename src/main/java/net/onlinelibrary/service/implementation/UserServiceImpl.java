package net.onlinelibrary.service.implementation;

import lombok.extern.slf4j.Slf4j;
import net.onlinelibrary.exception.UserAlreadyExistsException;
import net.onlinelibrary.exception.UserNotFoundException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.Role;
import net.onlinelibrary.model.User;
import net.onlinelibrary.repository.UserRepository;
import net.onlinelibrary.service.UserService;
import net.onlinelibrary.util.NumberNormalizer;
import net.onlinelibrary.validator.Validator;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final Validator<User> userValidator;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepo, Validator<User> userValidator, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getByRange(@NotNull Integer begin, @NotNull Integer count) {
        List<User> allUsers = userRepo.findAll();

        List<User> userInRange = allUsers.subList(
                NumberNormalizer.normalize(begin, 0, allUsers.size() == 0 ? 0 : allUsers.size() - 1),
                NumberNormalizer.normalize(begin + count, 0, allUsers.size()));

        log.info("IN getByRange - found " + userInRange.size() + " users");

        return userInRange;
    }

    @Override
    public User getById(@NotNull Long userId) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findById(userId);
        if(!userOpt.isPresent()) {
            log.warn("IN getById - user with id " + userId + " has not found");
            throw new UserNotFoundException("User with id \'" + userId + "\' has not found");
        }
        User user = userOpt.get();
        log.info("IN getById - user with id " + user.getId() + " found");
        return user;
    }

    @Override
    public User getByUsername(@NotNull String username) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findByUsername(username);
        if (!userOpt.isPresent()) {
            log.warn("IN getByUsername - user with username " + username + " has not found");
            throw new UserNotFoundException("User with username \'" + username + "\' has not found");
        }
        User user = userOpt.get();
        log.info("IN getByUsername - user with id " + user.getId() + " found");
        return user;
    }

    @Override
    public List<Comment> getCommentsOfUser(@NotNull Long userId) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findById(userId);
        if(!userOpt.isPresent()) {
            log.warn("IN getCommentsOfUser - user with id " + userId + " has not found");
            throw new UserNotFoundException("User with id \'" + userId + "\' has not found");
        }
        List<Comment> comments = userOpt.get().getComments();
        log.info("IN getCommentsOfUser - found " + comments.size() + " comments of user with id " + userId);
        return comments;
    }

    @Override
    public Set<Role> getRolesOfUser(@NotNull Long userId) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findById(userId);
        if(!userOpt.isPresent()) {
            log.warn("IN getRolesOfUser - user with id " + userId + " has not found");
            throw new UserNotFoundException("User with id \'" + userId + "\' has not found");
        }
        Set<Role> roles = userOpt.get().getRoles();
        log.info("IN getRolesOfUser - found " + roles.size() + " roles of user with id " + userId);
        return roles;
    }

    @Override
    public User saveUser(@NotNull User user) throws UserAlreadyExistsException, ValidationException {
        userValidator.validate(user);
        if(user.getId() == null)
        {
            if(userRepo.findByUsernameOrEmail(user.getUsername(), user.getEmail()).isPresent())
                throw new UserAlreadyExistsException("User with login=\'"+user.getUsername()+"\' and email=\'"+user.getEmail()+"\' is already exist");

            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepo.save(user);
    }

    @Override
    public void deleteById(@NotNull Long userId) throws UserNotFoundException {
        try {
            userRepo.deleteById(userId);
            log.info("IN deleteById - user with id " + userId + " deleted");
        }
        catch (EmptyResultDataAccessException e)
        {
            log.warn("IN deleteById - user with id " + userId + " has not found");
            throw new UserNotFoundException("User with id \'" + userId + "\' has not found");
        }
    }
}
