package net.onlinelibrary.service;

import net.onlinelibrary.exception.UserAlreadyExistsException;
import net.onlinelibrary.exception.UserNotFoundException;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.Role;
import net.onlinelibrary.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getByRange(Integer begin, Integer count);

    User getById(Long userId) throws UserNotFoundException;

    List<Comment> getCommentsOfUser(Long userId) throws UserNotFoundException;

    Set<Role> getRolesOfUser(Long userId) throws UserNotFoundException;

    User saveUser(User user) throws UserAlreadyExistsException;

    void deleteById(Long userId) throws UserNotFoundException;
}
