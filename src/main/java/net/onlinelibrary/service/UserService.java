package net.onlinelibrary.service;

import net.onlinelibrary.exception.UserAlreadyExistsException;
import net.onlinelibrary.exception.UserNotFoundException;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.Role;
import net.onlinelibrary.model.User;

import java.util.List;

public interface UserService {
    List<User> getByRange(Integer begin, Integer count);

    User getById(Long userId) throws UserNotFoundException;

    List<Comment> getCommentsOfUser(Long userId) throws UserNotFoundException;

    List<Role> getRolesOfUser(Long userId) throws UserNotFoundException;

    User saveUser(User user) throws UserAlreadyExistsException;

    User deleteById(Long userId) throws UserNotFoundException;
}
