package net.onlinelibrary.rest.moderator;

import com.fasterxml.jackson.annotation.JsonView;
import net.onlinelibrary.dto.BookDto;
import net.onlinelibrary.dto.CommentDto;
import net.onlinelibrary.dto.UserDto;
import net.onlinelibrary.dto.view.Views;
import net.onlinelibrary.exception.CommentException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.exception.withResponseStatus.BadRequestException;
import net.onlinelibrary.exception.withResponseStatus.NotFoundException;
import net.onlinelibrary.mapper.CommentMapper;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.rest.CommentController;
import net.onlinelibrary.service.CommentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/moderator/comments")
@PreAuthorize("hasAuthority('MODERATOR')")
public class ModeratorCommentController {
    private final CommentService commentService;

    private final CommentMapper commentMapper;
    private final CommentController commentController;

    public ModeratorCommentController(
            CommentService commentService,
            CommentMapper commentMapper,
            CommentController commentController) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.commentController = commentController;
    }

    @GetMapping("")
    @JsonView(Views.ForModerator.class)
    public Stream<CommentDto> getCommentsInRange(@RequestParam Integer offset, @RequestParam Integer count) {
        return commentController.getCommentsInRange(offset, count);
    }

    @GetMapping("{id}")
    @JsonView(Views.ForModerator.class)
    public CommentDto getCommentById(@PathVariable("id") Long commentId) {
        return commentController.getCommentById(commentId);
    }

    @GetMapping("{id}/book")
    @JsonView(Views.ForModerator.class)
    public BookDto getBookOfComment(@PathVariable("id") Long commentId) {
        return commentController.getBookOfComment(commentId);
    }

    @GetMapping("{id}/user")
    @JsonView(Views.ForModerator.class)
    public UserDto getUserOfComment(@PathVariable("id") Long commentId) {
        return commentController.getUserOfComment(commentId);
    }

    @GetMapping("count")
    @JsonView(Views.ForModerator.class)
    public Map<Object, Object> getTotalCommentsCount() {
        return commentController.getTotalCommentsCount();
    }

    @PostMapping("")
    @JsonView(Views.ForModerator.class)
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
    @JsonView(Views.ForModerator.class)
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
    @JsonView(Views.ForModerator.class)
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
    @JsonView(Views.ForModerator.class)
    public void deleteComment(@PathVariable("id") Long commentId) {
        try {
            commentService.deleteById(commentId);
        } catch (CommentException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
