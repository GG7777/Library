package net.onlinelibrary.rest.user;

import com.fasterxml.jackson.annotation.JsonView;
import net.onlinelibrary.dto.CommentDto;
import net.onlinelibrary.dto.UserDto;
import net.onlinelibrary.dto.view.Views;
import net.onlinelibrary.exception.UserNotFoundException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.exception.withResponseStatus.BadRequestException;
import net.onlinelibrary.exception.withResponseStatus.ForbiddenException;
import net.onlinelibrary.exception.withResponseStatus.NotFoundException;
import net.onlinelibrary.mapper.implementation.UserMapper;
import net.onlinelibrary.model.User;
import net.onlinelibrary.rest.UserController;
import net.onlinelibrary.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/user/users")
@PreAuthorize("hasAuthority('USER')")
public class UserUserController {
    private final UserService userService;

    private final UserMapper userMapper;
    private final UserController userController;

    public UserUserController(
            UserService userService,
            UserMapper userMapper,
            UserController userController) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userController = userController;
    }

    @GetMapping("")
    @JsonView(Views.ForUser.class)
    public Stream<UserDto> getUsersInRange(@RequestParam Integer offset, @RequestParam Integer count) {
        return userController.getUsersInRange(offset, count);
    }

    @GetMapping("{id}")
    @JsonView(Views.ForUser.class)
    public UserDto getUserById(@PathVariable("id") Long userId) {
        return userController.getUserById(userId);
    }

    @GetMapping("{id}/comments")
    @JsonView(Views.ForUser.class)
    public Stream<CommentDto> getCommentsOfUser(@PathVariable("id") Long userId) {
        return userController.getCommentsOfUser(userId);
    }

    @GetMapping("count")
    @JsonView(Views.ForUser.class)
    public Map<Object, Object> getTotalUsersCount() {
        return userController.getTotalUsersCount();
    }

    @PatchMapping("{id}/password")
    @JsonView(Views.ForUser.class)
    public UserDto updatePassword(@PathVariable("id") Long userId, @RequestBody UserDto dto) {
        checkAccessRightsToModifyUserBy(userId);
        try {
            User updatedUser = userService.updatePassword(userId, dto.getPassword());
            return userMapper.toDto(updatedUser);
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @PatchMapping("{id}/username")
    @JsonView(Views.ForUser.class)
    public UserDto updateUsername(@PathVariable("id") Long userId, @RequestBody UserDto dto) {
        checkAccessRightsToModifyUserBy(userId);
        try {
            User updatedUser = userService.updateUsername(userId, dto.getUsername());
            return userMapper.toDto(updatedUser);
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @PatchMapping("{id}/email")
    @JsonView(Views.ForUser.class)
    public UserDto updateEmail(@PathVariable("id") Long userId, @RequestBody UserDto dto) {
        checkAccessRightsToModifyUserBy(userId);
        try {
            User updatedUser = userService.updateEmail(userId, dto.getEmail());
            return userMapper.toDto(updatedUser);
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @JsonView(Views.ForUser.class)
    public void deleteUser(@PathVariable("id") Long userId) {
        checkAccessRightsToModifyUserBy(userId);
        try {
            userService.deleteById(userId);
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    private void checkAccessRightsToModifyUserBy(Long userId) {
        String authorizedUsername = ((UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();

        User user = null;
        try {
            user = userService.getByUsername(authorizedUsername);
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }

        if (!user.getId().equals(userId))
            throw new ForbiddenException("You do not have access to modify user");
    }
}
