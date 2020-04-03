package net.onlinelibrary.rest;

import net.onlinelibrary.dto.CommentDto;
import net.onlinelibrary.dto.UserDto;
import net.onlinelibrary.exception.*;
import net.onlinelibrary.exception.withResponseStatus.AlreadyExistsException;
import net.onlinelibrary.exception.withResponseStatus.BadRequestException;
import net.onlinelibrary.exception.withResponseStatus.ForbiddenException;
import net.onlinelibrary.exception.withResponseStatus.NotFoundException;
import net.onlinelibrary.mapper.CommentMapper;
import net.onlinelibrary.mapper.UserMapper;
import net.onlinelibrary.model.Role;
import net.onlinelibrary.model.User;
import net.onlinelibrary.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    private final UserMapper userMapper;
    private final CommentMapper commentMapper;

    public UserController(UserService userService, UserMapper userMapper, CommentMapper commentMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.commentMapper = commentMapper;
    }

    @GetMapping("")
    public Stream<UserDto> getUsersInRange(@RequestParam Integer begin, @RequestParam Integer count) {
        return userService
                .getByRange(begin, count)
                .stream()
                .map(user -> userMapper.toDto(user));
    }

    @GetMapping("{id}")
    public UserDto getUserById(@PathVariable("id") Long userId) {
        try {
            return userMapper.toDto(userService.getById(userId));
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/comments")
    public Stream<CommentDto> getCommentsOfUser(@PathVariable("id") Long userId) {
        try {
            return userService
                    .getCommentsOfUser(userId)
                    .stream()
                    .map(comment -> commentMapper.toDto(comment));
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/roles")
    public Set<Role> getRolesOfUser(@PathVariable("id") Long userId) {
        try {
            return userService.getRolesOfUser(userId);
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }



    @PostMapping("")
    public UserDto saveUser(@RequestBody UserDto dto) {
        User user = userMapper.toEntity(dto);

        user.setId(null);
        user.setCreatedDate(new Date());
        user.setLastModifiedDate(new Date());

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));

        try {
            return userMapper.toDto(userService.saveUser(user));
        } catch (UserAlreadyExistsException e) {
            throw new AlreadyExistsException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("{id}")
    public UserDto fullUpdateUser(@PathVariable("id") Long userId, @RequestBody UserDto dto) {
        try {
            checkAccessRights(userId, dto.getRoles() != null);

            User user = userService.getById(userId);
            User userByDto = userMapper.toEntity(dto);

            userByDto.setId(user.getId());
            userByDto.setCreatedDate(user.getCreatedDate());
            userByDto.setLastModifiedDate(new Date());

            userByDto.setActive(user.isActive());

            BeanUtils.copyProperties(userByDto, user);

            return userMapper.toDto(userService.saveUser(user));
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (UserAlreadyExistsException e) {
            throw new AlreadyExistsException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PatchMapping("{id}")
    public UserDto partUpdateUser(@PathVariable("id") Long userId, @RequestBody UserDto dto) {
        try {
            checkAccessRights(userId, dto.getRoles() != null);

            User user = userService.getById(userId);
            User userByDto = userMapper.toEntity(dto);

            if (userByDto.getComments() != null)
                user.setComments(userByDto.getComments());
            if (userByDto.getRoles() != null)
                user.setRoles(userByDto.getRoles());
            if (userByDto.getEmail() != null)
                user.setEmail(userByDto.getEmail());
            if (userByDto.getUsername() != null)
                user.setUsername(userByDto.getUsername());
            if (userByDto.getPassword() != null)
                user.setPassword(userByDto.getPassword());

            user.setLastModifiedDate(new Date());

            return userMapper.toDto(userService.saveUser(user));
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (UserAlreadyExistsException e) {
            throw new AlreadyExistsException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException(e.getMessage());
        }
    }


    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") Long userId) {
        try {
            checkAccessRights(userId, false);

            userService.deleteById(userId);
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    private void checkAccessRights(Long userId, boolean checkAccessToChangeRoles) {
        String authorizedUsername = ((UserDetails)SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal())
                .getUsername();

        ForbiddenException exception = new ForbiddenException("You do not have access to change user");

        User user = null;
        try {
            user = userService.getByUsername(authorizedUsername);
        } catch (UserNotFoundException e) {
            throw exception;
        }

        if (!user.getId().equals(userId) && !user.getRoles().contains(Role.KOSTYAN))
            throw exception;

        if (checkAccessToChangeRoles && !user.getRoles().contains(Role.KOSTYAN))
            throw exception;
    }
}
