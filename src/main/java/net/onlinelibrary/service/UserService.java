package net.onlinelibrary.service;

import net.onlinelibrary.exception.UserNotFoundException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.Role;
import net.onlinelibrary.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getByRange(Integer offset, Integer count);

    User getById(Long userId) throws UserNotFoundException;

    List<Comment> getCommentsOfUser(Long userId) throws UserNotFoundException;

    Set<Role> getRolesOfUser(Long userId) throws UserNotFoundException;

    User saveNewUser(User user) throws ValidationException;

    User updateUserWithPasswordAndUsernameAndEmailExcluding(Long userId, User user) throws UserNotFoundException, ValidationException;

    User updatePassword(Long userId, String newPassword) throws UserNotFoundException, ValidationException;

    User updateUsername(Long userId, String newUsername) throws UserNotFoundException, ValidationException;

    User updateEmail(Long userId, String newEmail) throws UserNotFoundException, ValidationException;

    void deleteById(Long userId) throws UserNotFoundException;

    User getByUsername(String username) throws UserNotFoundException;
}
