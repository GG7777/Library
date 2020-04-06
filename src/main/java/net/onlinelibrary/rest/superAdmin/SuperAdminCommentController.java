package net.onlinelibrary.rest.superAdmin;

import com.fasterxml.jackson.annotation.JsonView;
import net.onlinelibrary.dto.BookDto;
import net.onlinelibrary.dto.CommentDto;
import net.onlinelibrary.dto.UserDto;
import net.onlinelibrary.dto.view.Views;
import net.onlinelibrary.exception.CommentException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.exception.withResponseStatus.BadRequestException;
import net.onlinelibrary.exception.withResponseStatus.NotFoundException;
import net.onlinelibrary.mapper.implementation.CommentMapper;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.rest.moderator.ModeratorCommentController;
import net.onlinelibrary.service.CommentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/super-admin/comments")
@PreAuthorize("hasAuthority('SUPER_ADMIN')")
public class SuperAdminCommentController {
    private final CommentService commentService;

    private final CommentMapper commentMapper;
    private final ModeratorCommentController moderatorCommentController;

    public SuperAdminCommentController(
            CommentService commentService,
            CommentMapper commentMapper,
            ModeratorCommentController moderatorCommentController) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.moderatorCommentController = moderatorCommentController;
    }

    @GetMapping("")
    @JsonView(Views.ForSuperAdmin.class)
    public Stream<CommentDto> getCommentsInRange(@RequestParam Integer offset, @RequestParam Integer count) {
        return moderatorCommentController.getCommentsInRange(offset, count);
    }

    @GetMapping("{id}")
    @JsonView(Views.ForSuperAdmin.class)
    public CommentDto getCommentById(@PathVariable("id") Long commentId) {
        return moderatorCommentController.getCommentById(commentId);
    }

    @GetMapping("{id}/book")
    @JsonView(Views.ForSuperAdmin.class)
    public BookDto getBookOfComment(@PathVariable("id") Long commentId) {
        return moderatorCommentController.getBookOfComment(commentId);
    }

    @GetMapping("{id}/user")
    @JsonView(Views.ForSuperAdmin.class)
    public UserDto getUserOfComment(@PathVariable("id") Long commentId) {
        return moderatorCommentController.getUserOfComment(commentId);
    }

    @GetMapping("count")
    @JsonView(Views.ForSuperAdmin.class)
    public Map<Object, Object> getTotalCommentsCount() {
        return moderatorCommentController.getTotalCommentsCount();
    }

    @PostMapping("")
    @JsonView(Views.ForSuperAdmin.class)
    public CommentDto saveComment(@RequestBody CommentDto dto) {
        Comment comment = commentMapper.toEntity(dto);
        try {
            Comment savedComment = commentService.saveNewComment(comment);
            return commentMapper.toDto(savedComment);
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @PutMapping("{id}")
    @JsonView(Views.ForSuperAdmin.class)
    public CommentDto fullUpdateComment(@PathVariable("id") Long commentId, @RequestBody CommentDto dto) {
        Comment comment = commentMapper.toEntity(dto);
        try {
            Comment updatedComment = commentService.updateComment(commentId, comment);
            return commentMapper.toDto(updatedComment);
        } catch (CommentException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @PatchMapping("{id}")
    @JsonView(Views.ForSuperAdmin.class)
    public CommentDto partUpdateComment(@PathVariable("id") Long commentId, @RequestBody CommentDto dto) {
        Comment comment;
        try {
            comment = commentService.getById(commentId);
        } catch (CommentException e) {
            throw new NotFoundException(e.getMessage());
        }
        Comment commentByDto = commentMapper.toEntity(dto);

        if (commentByDto.getBook() != null)
            comment.setBook(commentByDto.getBook());
        if (commentByDto.getUser() != null)
            comment.setUser(commentByDto.getUser());
        if (commentByDto.getRating() != null)
            comment.setRating(commentByDto.getRating());
        if (commentByDto.getText() != null)
            comment.setText(commentByDto.getText());

        try {
            Comment updatedComment = commentService.updateComment(comment.getId(), comment);
            return commentMapper.toDto(updatedComment);
        } catch (CommentException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @JsonView(Views.ForSuperAdmin.class)
    public void deleteComment(@PathVariable("id") Long commentId) {
        moderatorCommentController.deleteComment(commentId);
    }
}
