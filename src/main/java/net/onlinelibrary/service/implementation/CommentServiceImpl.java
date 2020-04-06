package net.onlinelibrary.service.implementation;

import lombok.extern.slf4j.Slf4j;
import net.onlinelibrary.exception.CommentException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.User;
import net.onlinelibrary.repository.CommentRepository;
import net.onlinelibrary.service.CommentService;
import net.onlinelibrary.util.NumberNormalizer;
import net.onlinelibrary.validator.Validator;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepo;
    private final Validator<Comment> commentValidator;

    public CommentServiceImpl(CommentRepository commentRepo, Validator<Comment> commentValidator) {
        this.commentRepo = commentRepo;
        this.commentValidator = commentValidator;
    }

    @Override
    public List<Comment> getByRange(@NotNull Integer offset, @NotNull Integer count) {
        List<Comment> allComments = commentRepo.findAll();

        List<Comment> commentsInRange = allComments.subList(
                NumberNormalizer.normalize(offset, 0, allComments.size() == 0 ? 0 : allComments.size() - 1),
                NumberNormalizer.normalize(offset + count, 0, allComments.size()));

        log.info("IN getByRange - found " + commentsInRange.size() + " comments");

        return commentsInRange;
    }

    @Override
    public Comment getById(@NotNull Long commentId) throws CommentException {
        Optional<Comment> commentOpt = commentRepo.findById(commentId);
        if (!commentOpt.isPresent()) {
            CommentException commentException = new CommentException("Comment with id \'" + commentId + "\' has not found");
            log.warn("IN getById - " + commentException.getMessage());
            throw commentException;
        }
        Comment comment = commentOpt.get();
        log.info("IN getById - comment with id " + comment.getId() + " found");
        return comment;
    }

    @Override
    public Book getBookOfComment(@NotNull Long commentId) throws CommentException {
        Optional<Comment> commentOpt = commentRepo.findById(commentId);
        if (!commentOpt.isPresent()) {
            CommentException commentException = new CommentException("Comment with id \'" + commentId + "\' has not found");
            log.warn("IN getBookOfComment - " + commentException.getMessage());
            throw commentException;
        }
        Book book = commentOpt.get().getBook();
        log.info("IN getBookOfComment - book with id " + book.getId() + " found in comment with id " + commentId);
        return book;
    }

    @Override
    public User getUserOfComment(@NotNull Long commentId) throws CommentException {
        Optional<Comment> commentOpt = commentRepo.findById(commentId);
        if (!commentOpt.isPresent()) {
            CommentException commentException = new CommentException("Comment with id \'" + commentId + "\' has not found");
            log.warn("IN getUserOfComment - " + commentException.getMessage());
            throw commentException;
        }
        User user = commentOpt.get().getUser();
        log.info("IN getUserOfComment - user with id " + user.getId() + " found in comment with id " + commentId);
        return user;
    }

    @Override
    public Comment saveNewComment(@NotNull Comment comment) throws ValidationException {
        comment.setId(null);
        comment.setCreatedDate(new Date());
        comment.setLastModifiedDate(new Date());

        try {
            commentValidator.validate(comment);
        } catch (ValidationException e) {
            log.warn("IN saveNewComment - validation failure - " + e.getMessage());
            throw e;
        }

        Comment savedComment = commentRepo.save(comment);
        log.info("IN saveNewComment - comment with id " + savedComment.getId() + " saved");
        return savedComment;
    }

    @Override
    public Comment updateComment(@NotNull Long commentId, @NotNull Comment comment) throws CommentException, ValidationException {
        Comment commentFromRepo;
        try {
            commentFromRepo = getById(commentId);
        } catch (CommentException e) {
            log.warn("IN updateComment - " + e.getMessage());
            throw e;
        }

        comment.setId(commentFromRepo.getId());
        comment.setCreatedDate(commentFromRepo.getCreatedDate());
        comment.setLastModifiedDate(new Date());

        try {
            commentValidator.validate(comment);
        } catch (ValidationException e) {
            log.warn("IN updateComment - validation failure - " + e.getMessage());
            throw e;
        }

        Comment savedComment = commentRepo.save(comment);
        log.info("IN updateComment - comment with id " + savedComment.getId() + " updated");
        return savedComment;
    }

    @Override
    public Comment updateCommentText(@NotNull Long commentId, @NotNull String text) throws CommentException, ValidationException {
        Comment comment;
        try {
            comment = getById(commentId);
        } catch (CommentException e) {
            log.warn("IN updateCommentText - " + e.getMessage());
            throw e;
        }

        comment.setText(text);
        comment.setLastModifiedDate(new Date());

        try {
            commentValidator.validate(comment);
        } catch (ValidationException e) {
            log.warn("IN updateCommentText - validation failure - " + e.getMessage());
            throw e;
        }

        Comment savedComment = commentRepo.save(comment);
        log.info("IN updateCommentText - comment with id " + savedComment.getId() + " updated text");
        return savedComment;
    }

    @Override
    public void deleteById(@NotNull Long commentId) throws CommentException {
        try {
            commentRepo.deleteById(commentId);
            log.info("IN deleteById - comment with id " + commentId + " deleted");
        } catch (EmptyResultDataAccessException e) {
            CommentException commentException = new CommentException("Comment with id \'" + commentId + "\' has not found");
            log.warn("IN deleteById - " + commentException.getMessage());
            throw commentException;
        }
    }

    @Override
    public Long getCommentsCount() {
        return commentRepo.count();
    }
}
