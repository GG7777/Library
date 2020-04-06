package net.onlinelibrary.rest.superAdmin;

import com.fasterxml.jackson.annotation.JsonView;
import net.onlinelibrary.dto.CommentDto;
import net.onlinelibrary.dto.UserDto;
import net.onlinelibrary.dto.view.Views;
import net.onlinelibrary.exception.UserNotFoundException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.exception.withResponseStatus.BadRequestException;
import net.onlinelibrary.exception.withResponseStatus.NotFoundException;
import net.onlinelibrary.mapper.implementation.UserMapper;
import net.onlinelibrary.model.Role;
import net.onlinelibrary.model.User;
import net.onlinelibrary.rest.UserController;
import net.onlinelibrary.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/super-admin/users")
@PreAuthorize("hasAuthority('SUPER_ADMIN')")
public class SuperAdminUserController {
    private final UserService userService;

    private final UserMapper userMapper;
    private final UserController userController;

    public SuperAdminUserController(
            UserService userService,
            UserMapper userMapper,
            UserController userController) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userController = userController;
    }

    @GetMapping("")
    @JsonView(Views.ForSuperAdmin.class)
    public Stream<UserDto> getUsersInRange(@RequestParam Integer offset, @RequestParam Integer count) {
        return userController.getUsersInRange(offset, count);
    }

    @GetMapping("{id}")
    @JsonView(Views.ForSuperAdmin.class)
    public UserDto getUserById(@PathVariable("id") Long userId) {
        return userController.getUserById(userId);
    }

    @GetMapping("{id}/comments")
    @JsonView(Views.ForSuperAdmin.class)
    public Stream<CommentDto> getCommentsOfUser(@PathVariable("id") Long userId) {
        return userController.getCommentsOfUser(userId);
    }

    @GetMapping("{id}/roles")
    @JsonView(Views.ForSuperAdmin.class)
    public Set<Role> getRolesOfUser(@PathVariable("id") Long userId) {
        try {
            return userService.getRolesOfUser(userId);
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("count")
    @JsonView(Views.ForSuperAdmin.class)
    public Map<Object, Object> getTotalUsersCount() {
        return userController.getTotalUsersCount();
    }

    @PostMapping("")
    @JsonView(Views.ForSuperAdmin.class)
    public UserDto saveUser(@RequestBody UserDto dto) {
        User user = userMapper.toEntity(dto);
        try {
            User savedUser = userService.saveNewUser(user);
            return userMapper.toDto(savedUser);
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @PutMapping("{id}")
    @JsonView(Views.ForSuperAdmin.class)
    public UserDto fullUpdateUser(@PathVariable("id") Long userId, @RequestBody UserDto dto) {
        User user = userMapper.toEntity(dto);
        try {
            User updatedUser = userService.updateUserWithPasswordAndUsernameAndEmailExcluding(userId, user);
            return userMapper.toDto(updatedUser);
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @PatchMapping("{id}")
    @JsonView(Views.ForSuperAdmin.class)
    public UserDto partUpdateUser(@PathVariable("id") Long userId, @RequestBody UserDto dto) {
        User user;
        try {
            user = userService.getById(userId);
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
        User userByDto = userMapper.toEntity(dto);

        if (userByDto.getComments() != null)
            user.setComments(userByDto.getComments());
        if (userByDto.getRoles() != null)
            user.setRoles(userByDto.getRoles());
        if (userByDto.getActive() != null)
            user.setActive(userByDto.getActive());

        try {
            User updatedUser = userService.updateUserWithPasswordAndUsernameAndEmailExcluding(user.getId(), user);
            return userMapper.toDto(updatedUser);
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @PatchMapping("{id}/password")
    @JsonView(Views.ForSuperAdmin.class)
    public UserDto updatePassword(@PathVariable("id") Long userId, @RequestBody UserDto dto) {
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
    @JsonView(Views.ForSuperAdmin.class)
    public UserDto updateUsername(@PathVariable("id") Long userId, @RequestBody UserDto dto) {
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
    @JsonView(Views.ForSuperAdmin.class)
    public UserDto updateEmail(@PathVariable("id") Long userId, @RequestBody UserDto dto) {
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
    @JsonView(Views.ForSuperAdmin.class)
    public void deleteUser(@PathVariable("id") Long userId) {
        try {
            userService.deleteById(userId);
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
