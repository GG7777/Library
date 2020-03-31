package net.onlinelibrary.rest;

import net.onlinelibrary.dto.*;
import net.onlinelibrary.exception.*;
import net.onlinelibrary.mapper.BookMapper;
import net.onlinelibrary.mapper.CommentMapper;
import net.onlinelibrary.mapper.UserMapper;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.Role;
import net.onlinelibrary.model.User;
import net.onlinelibrary.security.jwt.JwtUserDetails;
import net.onlinelibrary.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/comments")
public class CommentController {
    private final CommentService commentService;

    private final CommentMapper commentMapper;
    private final BookMapper bookMapper;
    private final UserMapper userMapper;

    public CommentController(CommentService commentService, CommentMapper commentMapper, BookMapper bookMapper, UserMapper userMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.bookMapper = bookMapper;
        this.userMapper = userMapper;
    }

    @GetMapping("")
    public Stream<CommentDto> getCommentsInRange(@RequestParam Integer begin, @RequestParam Integer count) {
        return commentService
                .getByRange(begin, count)
                .stream()
                .map(comment -> commentMapper.toDto(comment));
    }

    @GetMapping("{id}")
    public CommentDto getCommentById(@PathVariable("id") Long commentId) {
        try {
            return commentMapper.toDto(commentService.getById(commentId));
        } catch (CommentException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/book")
    public BookDto getBookOfComment(@PathVariable("id") Long commentId) {
        try {
            return bookMapper.toDto(commentService.getBookOfComment(commentId));
        } catch (CommentException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/user")
    public UserDto getUserOfComment(@PathVariable("id") Long commentId) {
        try {
            return userMapper.toDto(commentService.getUserOfComment(commentId));
        } catch (CommentException e) {
            throw new NotFoundException(e.getMessage());
        }
    }



    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("")
    public CommentDto saveComment(@RequestBody CommentDto dto) {
        Comment comment = commentMapper.toEntity(dto);

        comment.setId(null);
        comment.setCreatedDate(new Date());
        comment.setLastModifiedDate(new Date());

        comment.setRating(0l);

        try {
            return commentMapper.toDto(commentService.saveComment(comment));
        } catch (ValidationException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("{id}")
    public CommentDto fullUpdateComment(@PathVariable("id") Long commentId, @RequestBody CommentDto dto) {
        try {
            Comment comment = commentService.getById(commentId);
            User authorizedUser = ((JwtUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            if (!authorizedUser.getId().equals(comment.getUser().getId()) && !authorizedUser.getRoles().contains(Role.KOSTYAN))
                throw new ForbiddenException("You do not have access to modify comment");

            Comment commentByDto = commentMapper.toEntity(dto);

            commentByDto.setId(comment.getId());
            commentByDto.setCreatedDate(comment.getCreatedDate());
            commentByDto.setLastModifiedDate(new Date());

            BeanUtils.copyProperties(commentByDto, comment);

            return commentMapper.toDto(commentService.saveComment(comment));
        } catch (CommentException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PatchMapping("{id}")
    public CommentDto partUpdateComment(@PathVariable("id") Long commentId, @RequestBody CommentDto dto) {
        try {
            Comment comment = commentService.getById(commentId);
            User authorizedUser = ((JwtUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            if (!authorizedUser.getId().equals(comment.getUser().getId()) && !authorizedUser.getRoles().contains(Role.KOSTYAN))
                throw new ForbiddenException("You do not have access to modify comment");

            Comment commentByDto = commentMapper.toEntity(dto);

            if (commentByDto.getBook() != null)
                comment.setBook(commentByDto.getBook());
            if (commentByDto.getUser() != null)
                comment.setUser(commentByDto.getUser());
            if (commentByDto.getRating() != null)
                comment.setRating(commentByDto.getRating());
            if (commentByDto.getText() != null)
                comment.setText(commentByDto.getText());

            comment.setLastModifiedDate(new Date());

            return commentMapper.toDto(commentService.saveComment(comment));
        } catch (CommentException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException(e.getMessage());
        }
    }



    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("{id}")
    public void deleteComment(@PathVariable("id") Long commentId) {
        try {
            Comment comment = commentService.getById(commentId);
            User authorizedUser = ((JwtUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            if (!authorizedUser.getId().equals(comment.getUser().getId()) && !authorizedUser.getRoles().contains(Role.KOSTYAN))
                throw new ForbiddenException("You do not have access to modify comment");

            commentService.deleteById(commentId);
        } catch (CommentException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
