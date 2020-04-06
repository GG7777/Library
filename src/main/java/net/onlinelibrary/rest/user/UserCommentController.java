package net.onlinelibrary.rest.user;

import com.fasterxml.jackson.annotation.JsonView;
import net.onlinelibrary.dto.BookDto;
import net.onlinelibrary.dto.CommentDto;
import net.onlinelibrary.dto.UserDto;
import net.onlinelibrary.dto.view.Views;
import net.onlinelibrary.exception.CommentException;
import net.onlinelibrary.exception.UserNotFoundException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.exception.withResponseStatus.BadRequestException;
import net.onlinelibrary.exception.withResponseStatus.ForbiddenException;
import net.onlinelibrary.exception.withResponseStatus.NotFoundException;
import net.onlinelibrary.mapper.CommentMapper;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.User;
import net.onlinelibrary.rest.CommentController;
import net.onlinelibrary.service.CommentService;
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
public class UserCommentController {
    private final CommentService commentService;

    private final CommentMapper commentMapper;
    private final UserService userService;
    private final CommentController commentController;

    public UserCommentController(
            CommentService commentService,
            CommentMapper commentMapper,
            UserService userService,
            CommentController commentController) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.userService = userService;
        this.commentController = commentController;
    }

    @GetMapping("")
    @JsonView(Views.ForUser.class)
    public Stream<CommentDto> getCommentsInRange(@RequestParam Integer offset, @RequestParam Integer count) {
        return commentController.getCommentsInRange(offset, count);
    }

    @GetMapping("{id}")
    @JsonView(Views.ForUser.class)
    public CommentDto getCommentById(@PathVariable("id") Long commentId) {
        return commentController.getCommentById(commentId);
    }

    @GetMapping("{id}/book")
    @JsonView(Views.ForUser.class)
    public BookDto getBookOfComment(@PathVariable("id") Long commentId) {
        return commentController.getBookOfComment(commentId);
    }

    @GetMapping("{id}/user")
    @JsonView(Views.ForUser.class)
    public UserDto getUserOfComment(@PathVariable("id") Long commentId) {
        return commentController.getUserOfComment(commentId);
    }

    @GetMapping("count")
    @JsonView(Views.ForUser.class)
    public Map<Object, Object> getTotalCommentsCount() {
        return commentController.getTotalCommentsCount();
    }

    @PostMapping("")
    @JsonView(Views.ForUser.class)
    public CommentDto saveComment(@RequestBody CommentDto dto) {
        Comment comment = commentMapper.toEntity(dto);
        comment.setRating(0l);
        comment.setUser(getAuthorizedUser());
        try {
            Comment savedComment = commentService.saveNewComment(comment);
            return commentMapper.toDto(savedComment);
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @PatchMapping("{id}/text")
    @JsonView(Views.ForUser.class)
    public CommentDto updateCommentText(@PathVariable("id") Long commentId, @RequestBody CommentDto dto) {
        if (dto.getText() == null)
            throw new BadRequestException("Text can not be null");

        checkAccessRightsToModifyComment(commentId);

        try {
            Comment updatedComment = commentService.updateCommentText(commentId, dto.getText());
            return commentMapper.toDto(updatedComment);
        } catch (CommentException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @JsonView(Views.ForUser.class)
    public void deleteComment(@PathVariable("id") Long commentId) {
        checkAccessRightsToModifyComment(commentId);
        try {
            commentService.deleteById(commentId);
        } catch (CommentException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    private void checkAccessRightsToModifyComment(Long commentId) {
        String authorizedUsername = ((UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();

        User userWithComment;
        try {
            userWithComment = commentService.getUserOfComment(commentId);
        } catch (CommentException e) {
            throw new NotFoundException(e.getMessage());
        }

        User authorizedUser = null;
        try {
            authorizedUser = userService.getByUsername(authorizedUsername);
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }

        if (!authorizedUser.getId().equals(userWithComment.getId()))
            throw new ForbiddenException("You do not have access to change authorizedUser");
    }

    private User getAuthorizedUser() {
        String authorizedUsername = ((UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
        try {
            return userService.getByUsername(authorizedUsername);
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
