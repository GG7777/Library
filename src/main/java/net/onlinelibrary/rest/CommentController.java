package net.onlinelibrary.rest;

import com.fasterxml.jackson.annotation.JsonView;
import net.onlinelibrary.dto.BookDto;
import net.onlinelibrary.dto.CommentDto;
import net.onlinelibrary.dto.UserDto;
import net.onlinelibrary.dto.view.Views;
import net.onlinelibrary.exception.CommentException;
import net.onlinelibrary.exception.withResponseStatus.NotFoundException;
import net.onlinelibrary.mapper.BookMapper;
import net.onlinelibrary.mapper.CommentMapper;
import net.onlinelibrary.mapper.UserMapper;
import net.onlinelibrary.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/comments")
public class CommentController {
    private final CommentService commentService;

    private final CommentMapper commentMapper;
    private final BookMapper bookMapper;
    private final UserMapper userMapper;

    public CommentController(
            CommentService commentService,
            CommentMapper commentMapper,
            BookMapper bookMapper,
            UserMapper userMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.bookMapper = bookMapper;
        this.userMapper = userMapper;
    }

    @GetMapping("")
    @JsonView(Views.ForEvery.class)
    public Stream<CommentDto> getCommentsInRange(@RequestParam Integer offset, @RequestParam Integer count) {
        return commentService
                .getByRange(offset, count)
                .stream()
                .map(comment -> commentMapper.toDto(comment));
    }

    @GetMapping("{id}")
    @JsonView(Views.ForEvery.class)
    public CommentDto getCommentById(@PathVariable("id") Long commentId) {
        try {
            return commentMapper.toDto(commentService.getById(commentId));
        } catch (CommentException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/book")
    @JsonView(Views.ForEvery.class)
    public BookDto getBookOfComment(@PathVariable("id") Long commentId) {
        try {
            return bookMapper.toDto(commentService.getBookOfComment(commentId));
        } catch (CommentException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/user")
    @JsonView(Views.ForEvery.class)
    public UserDto getUserOfComment(@PathVariable("id") Long commentId) {
        try {
            return userMapper.toDto(commentService.getUserOfComment(commentId));
        } catch (CommentException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("count")
    @JsonView(Views.ForEvery.class)
    public Map<Object, Object> getTotalCommentsCount() {
        Long count = commentService.getCommentsCount();

        HashMap<Object, Object> map = new HashMap<>();

        map.put("count", count);

        return map;
    }
}
