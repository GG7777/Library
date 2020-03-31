package net.onlinelibrary.service.implementation;

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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final Validator<User> userValidator;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepo, Validator<User> userValidator, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getByRange(Integer begin, Integer count) {
        List<User> users = userRepo.findAll();
        return users.subList(
                NumberNormalizer.normalize(begin, 0, users.size() == 0 ? 0 : users.size() - 1),
                NumberNormalizer.normalize(begin + count - 1, 0, users.size()));
    }

    @Override
    public User getById(Long userId) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findById(userId);
        if(!userOpt.isPresent())
            throw new UserNotFoundException("User with id \'" + userId + "\' has not found");
        return userOpt.get();
    }

    @Override
    public User getByUsername(String username) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findByUsername(username);
        if (!userOpt.isPresent())
            throw new UserNotFoundException("User with username \'" + username + "\' has not found");
        return userOpt.get();
    }

    @Override
    public List<Comment> getCommentsOfUser(Long userId) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findById(userId);
        if(!userOpt.isPresent())
            throw new UserNotFoundException("User with id \'" + userId + "\' has not found");
        return userOpt.get().getComments();
    }

    @Override
    public Set<Role> getRolesOfUser(Long userId) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findById(userId);
        if(!userOpt.isPresent())
            throw new UserNotFoundException("User with id \'" + userId + "\' has not found");
        return userOpt.get().getRoles();
    }

    @Override
    public User saveUser(User user) throws UserAlreadyExistsException, ValidationException {
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
    public void deleteById(Long userId) throws UserNotFoundException {
        try {
            userRepo.deleteById(userId);
        }
        catch (EmptyResultDataAccessException e)
        {
            throw new UserNotFoundException("User with id \'" + userId + "\' has not found");
        }
    }
}
