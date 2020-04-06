package net.onlinelibrary.rest;

import com.fasterxml.jackson.annotation.JsonView;
import net.onlinelibrary.dto.CommentDto;
import net.onlinelibrary.dto.UserDto;
import net.onlinelibrary.dto.view.Views;
import net.onlinelibrary.exception.UserNotFoundException;
import net.onlinelibrary.exception.withResponseStatus.NotFoundException;
import net.onlinelibrary.mapper.CommentMapper;
import net.onlinelibrary.mapper.UserMapper;
import net.onlinelibrary.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
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
    @JsonView(Views.ForEvery.class)
    public Stream<UserDto> getUsersInRange(@RequestParam Integer offset, @RequestParam Integer count) {
        return userService
                .getByRange(offset, count)
                .stream()
                .map(user -> userMapper.toDto(user));
    }

    @GetMapping("{id}")
    @JsonView(Views.ForEvery.class)
    public UserDto getUserById(@PathVariable("id") Long userId) {
        try {
            return userMapper.toDto(userService.getById(userId));
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/comments")
    @JsonView(Views.ForEvery.class)
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

    @GetMapping("count")
    @JsonView(Views.ForEvery.class)
    public Map<Object, Object> getTotalUsersCount() {
        Long count = userService.getUsersCount();

        HashMap<Object, Object> map = new HashMap<>();

        map.put("count", count);

        return map;
    }
}
