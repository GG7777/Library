package net.onlinelibrary.rest;

import com.fasterxml.jackson.annotation.JsonView;
import net.onlinelibrary.dto.UserDto;
import net.onlinelibrary.dto.view.Views;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.exception.withResponseStatus.BadRequestException;
import net.onlinelibrary.exception.withResponseStatus.NotFoundException;
import net.onlinelibrary.exception.UserNotFoundException;
import net.onlinelibrary.mapper.UserMapper;
import net.onlinelibrary.model.Role;
import net.onlinelibrary.model.User;
import net.onlinelibrary.security.jwt.JwtTokenProvider;
import net.onlinelibrary.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api")
public class AuthenticationController {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    public AuthenticationController(
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
    }

    @PostMapping("login")
    @JsonView(Views.ForEvery.class)
    public Map<Object, Object> login(@RequestBody UserDto dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getUsername(),
                            dto.getPassword()));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        User user = null;
        try {
            user = userService.getByUsername(dto.getUsername());
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }

        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());

        Map<Object, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("token", token);

        return response;
    }

    @PostMapping("register")
    @JsonView(Views.ForEvery.class)
    public Map<Object, Object> register(@RequestBody UserDto dto) {
        User user = userMapper.toEntity(dto);
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setComments(user.getComments() != null
                ? user.getComments()
                : new ArrayList<>());

        User savedUser;
        try {
            savedUser = userService.saveNewUser(user);
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtTokenProvider.createToken(savedUser.getUsername(), savedUser.getRoles());

        Map<Object, Object> response = new HashMap<>();
        response.put("username", savedUser.getUsername());
        response.put("token", token);

        return response;
    }
}
