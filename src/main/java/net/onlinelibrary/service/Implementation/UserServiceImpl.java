package net.onlinelibrary.service.Implementation;

import net.onlinelibrary.exception.UserAlreadyExistsException;
import net.onlinelibrary.exception.UserNotFoundException;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.Role;
import net.onlinelibrary.model.User;
import net.onlinelibrary.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<User> getByRange(Integer begin, Integer count) {
        return null;
    }

    @Override
    public User getById(Long userId) throws UserNotFoundException {
        return null;
    }

    @Override
    public List<Comment> getCommentsOfUser(Long userId) throws UserNotFoundException {
        return null;
    }

    @Override
    public List<Role> getRolesOfUser(Long userId) throws UserNotFoundException {
        return null;
    }

    @Override
    public User saveUser(User user) throws UserAlreadyExistsException {
        return null;
    }

    @Override
    public User deleteById(Long userId) throws UserNotFoundException {
        return null;
    }
}
