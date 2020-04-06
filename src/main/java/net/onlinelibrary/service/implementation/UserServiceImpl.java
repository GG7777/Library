package net.onlinelibrary.service.implementation;

import lombok.extern.slf4j.Slf4j;
import net.onlinelibrary.exception.UserNotFoundException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.Role;
import net.onlinelibrary.model.User;
import net.onlinelibrary.repository.UserRepository;
import net.onlinelibrary.service.UserService;
import net.onlinelibrary.substitute.implementation.UserPropertiesSubstitute;
import net.onlinelibrary.util.NumberNormalizer;
import net.onlinelibrary.validator.implementation.UserValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final UserPropertiesSubstitute userPropsSubstitute;

    public UserServiceImpl(
            UserRepository userRepo,
            UserValidator userValidator,
            @Lazy PasswordEncoder passwordEncoder,
            UserPropertiesSubstitute userPropsSubstitute) {
        this.userRepo = userRepo;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.userPropsSubstitute = userPropsSubstitute;
    }

    @Override
    public List<User> getByRange(@NotNull Integer offset, @NotNull Integer count) {
        List<User> allUsers = userRepo.findAll();

        List<User> userInRange = allUsers.subList(
                NumberNormalizer.normalize(offset, 0, allUsers.size() == 0 ? 0 : allUsers.size() - 1),
                NumberNormalizer.normalize(offset + count, 0, allUsers.size()));

        log.info("IN getByRange - found " + userInRange.size() + " users");

        return userInRange;
    }

    @Override
    public User getById(@NotNull Long userId) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findById(userId);
        if(!userOpt.isPresent()) {
            UserNotFoundException userNotFoundException = new UserNotFoundException("User with id \'" + userId + "\' has not found");
            log.warn("IN getById - " + userNotFoundException.getMessage());
            throw userNotFoundException;
        }
        User user = userOpt.get();
        log.info("IN getById - user with id " + user.getId() + " found");
        return user;
    }

    @Override
    public User getByUsername(@NotNull String username) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findByUsernameIgnoreCase(username);
        if (!userOpt.isPresent()) {
            UserNotFoundException userNotFoundException = new UserNotFoundException("User with username \'" + username + "\' has not found");
            log.warn("IN getByUsername - " + userNotFoundException.getMessage());
            throw userNotFoundException;
        }
        User user = userOpt.get();
        log.info("IN getByUsername - user with id " + user.getId() + " found");
        return user;
    }

    @Override
    public Long getUsersCount() {
        long count = userRepo.count();
        log.info("IN getUsersCount - total users count = " + count);
        return count;
    }

    @Override
    public List<Comment> getCommentsOfUser(@NotNull Long userId) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findById(userId);
        if(!userOpt.isPresent()) {
            UserNotFoundException userNotFoundException = new UserNotFoundException("User with id \'" + userId + "\' has not found");
            log.warn("IN getCommentsOfUser - " + userNotFoundException.getMessage());
            throw userNotFoundException;
        }
        List<Comment> comments = userOpt.get().getComments();
        log.info("IN getCommentsOfUser - found " + comments.size() + " comments of user with id " + userId);
        return comments;
    }

    @Override
    public Set<Role> getRolesOfUser(@NotNull Long userId) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findById(userId);
        if(!userOpt.isPresent()) {
            UserNotFoundException userNotFoundException = new UserNotFoundException("User with id \'" + userId + "\' has not found");
            log.warn("IN getRolesOfUser - " + userNotFoundException.getMessage());
            throw userNotFoundException;
        }
        Set<Role> roles = userOpt.get().getRoles();
        log.info("IN getRolesOfUser - found " + roles.size() + " roles of user with id " + userId);
        return roles;
    }

    @Override
    public User saveNewUser(@NotNull User user) throws ValidationException {
        user.setId(null);
        user.setCreatedDate(new Date());
        user.setLastModifiedDate(new Date());

        user = userPropsSubstitute.substitute(user);

        try {
            userValidator.validate(user);
        } catch (ValidationException e) {
            log.warn("IN saveNewUser - validation failure - " + e.getMessage());
            throw e;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepo.save(user);
        log.info("IN saveNewUser - user with id " + savedUser.getId() + " saved");
        return savedUser;
    }

    @Override
    public User updateUserWithPasswordAndUsernameAndEmailExcluding(@NotNull Long userId, @NotNull User user) throws UserNotFoundException, ValidationException {
        User userFromRepo;
        try {
            userFromRepo = getById(userId);
        } catch (UserNotFoundException e) {
            log.warn("IN updateUser - " + e.getMessage());
            throw e;
        }

        user.setId(userFromRepo.getId());
        user.setCreatedDate(userFromRepo.getCreatedDate());
        user.setLastModifiedDate(new Date());

        user.setPassword(userFromRepo.getPassword());
        user.setUsername(userFromRepo.getUsername());
        user.setEmail(userFromRepo.getEmail());

        try {
            userValidator.validate(user, false, false);
        } catch (ValidationException e) {
            log.warn("IN updateUser - validation failure - " + e.getMessage());
            throw e;
        }

        User savedUser = userRepo.save(user);
        log.info("IN updateUser - user with id " + savedUser.getId() + " updated");
        return savedUser;
    }

    @Override
    public User updatePassword(@NotNull Long userId, String newPassword) throws UserNotFoundException, ValidationException {
        User userFromRepo;
        try {
            userFromRepo = getById(userId);
        } catch (UserNotFoundException e) {
            log.warn("IN updatePassword - " + e.getMessage());
            throw e;
        }

        userFromRepo.setPassword(newPassword);
        userFromRepo.setLastModifiedDate(new Date());

        try {
            userValidator.validate(userFromRepo, true, false);
        } catch (ValidationException e) {
            log.warn("IN updatePassword - validation failure - " + e.getMessage());
            throw e;
        }

        userFromRepo.setPassword(passwordEncoder.encode(userFromRepo.getPassword()));

        User savedUser = userRepo.save(userFromRepo);
        log.info("IN updatePassword - password of user with id " + savedUser.getId() + " updated");
        return savedUser;
    }

    @Override
    public User updateUsername(@NotNull Long userId, String newUsername) throws UserNotFoundException, ValidationException {
        User userFromRepo;
        try {
            userFromRepo = getById(userId);
        } catch (UserNotFoundException e) {
            log.warn("IN updateUsername - " + e.getMessage());
            throw e;
        }

        userFromRepo.setUsername(newUsername);
        userFromRepo.setLastModifiedDate(new Date());

        try {
            userValidator.validate(userFromRepo, false, true);
        } catch (ValidationException e) {
            log.warn("IN updateUsername - " + e.getMessage());
            throw e;
        }

        User savedUser = userRepo.save(userFromRepo);
        log.info("IN updateUsername - user with id " + savedUser.getId() + " updated username");
        return savedUser;
    }

    @Override
    public User updateEmail(@NotNull Long userId, String newEmail) throws UserNotFoundException, ValidationException {
        User userFromRepo;
        try {
            userFromRepo = getById(userId);
        } catch (UserNotFoundException e) {
            log.warn("IN updateUsername - " + e.getMessage());
            throw e;
        }

        userFromRepo.setEmail(newEmail);
        userFromRepo.setLastModifiedDate(new Date());

        try {
            userValidator.validate(userFromRepo, false, true);
        } catch (ValidationException e) {
            log.warn("IN updateUsername - " + e.getMessage());
            throw e;
        }

        User savedUser = userRepo.save(userFromRepo);
        log.info("IN updateUsername - user with id " + savedUser.getId() + " updated username");
        return savedUser;
    }

    @Override
    public void deleteById(@NotNull Long userId) throws UserNotFoundException {
        try {
            userRepo.deleteById(userId);
            log.info("IN deleteById - user with id " + userId + " deleted");
        }
        catch (EmptyResultDataAccessException e)
        {
            UserNotFoundException userNotFoundException = new UserNotFoundException("User with id \'" + userId + "\' has not found");
            log.warn("IN deleteById - " + userNotFoundException.getMessage());
            throw userNotFoundException;
        }
    }
}
