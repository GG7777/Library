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
    public List<Comment> getByRange(@NotNull Integer begin, @NotNull Integer count) {
        List<Comment> allComments = commentRepo.findAll();

        List<Comment> commentsInRange = allComments.subList(
                NumberNormalizer.normalize(begin, 0, allComments.size() == 0 ? 0 : allComments.size() - 1),
                NumberNormalizer.normalize(begin + count, 0, allComments.size()));

        log.info("IN getByRange - found " + commentsInRange.size() + " comments");

        return commentsInRange;
    }

    @Override
    public Comment getById(@NotNull Long commentId) throws CommentException {
        Optional<Comment> commentOpt = commentRepo.findById(commentId);
        if(!commentOpt.isPresent()) {
            log.warn("IN getById - comment with id " + commentId + " has not found");
            throw new CommentException("Comment with id \'" + commentId + "\' has not found");
        }
        Comment comment = commentOpt.get();
        log.info("IN getById - comment with id " + comment.getId() + " found");
        return comment;
    }

    @Override
    public Book getBookOfComment(@NotNull Long commentId) throws CommentException {
        Optional<Comment> commentOpt = commentRepo.findById(commentId);
        if(!commentOpt.isPresent()) {
            log.warn("IN getBookOfComment - comment with id " + commentId + " has not found");
            throw new CommentException("Comment with id \'" + commentId + "\' has not found");
        }
        Book book = commentOpt.get().getBook();
        log.info("IN getBookOfComment - book with id " + book.getId() + " found in comment with id " + commentId);
        return book;
    }

    @Override
    public User getUserOfComment(@NotNull Long commentId) throws CommentException {
        Optional<Comment> commentOpt = commentRepo.findById(commentId);
        if(!commentOpt.isPresent()) {
            log.warn("IN getUserOfComment - comment with id " + commentId + " has not found");
            throw new CommentException("Comment with id \'" + commentId + "\' has not found");
        }
        User user = commentOpt.get().getUser();
        log.info("IN getUserOfComment - user with id " + user.getId() + " found in comment with id " + commentId);
        return user;
    }

    @Override
    public Comment saveComment(@NotNull Comment comment) throws ValidationException {
        try {
            commentValidator.validate(comment);
        } catch (ValidationException e) {
            log.warn("IN saveComment - validation failure - " + e.getMessage());
            throw e;
        }
        Comment savedComment = commentRepo.save(comment);
        log.info("IN saveComment - " +
                (comment.getId() == savedComment.getId() ? "updated" : "saved new") +
                " comment with id " + savedComment.getId());
        return savedComment;
    }

    @Override
    public void deleteById(@NotNull Long commentId) throws CommentException {
        try {
            commentRepo.deleteById(commentId);
            log.info("IN deleteById - comment with id " + commentId + " deleted");
        } catch (EmptyResultDataAccessException e) {
            log.warn("IN deleteById - comment with id " + commentId + " has not found");
            throw new CommentException("Comment with id \'" + commentId + "\' has not found");
        }
    }
}
