package net.onlinelibrary.service.implementation;

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

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepo;
    private final Validator<Comment> commentValidator;

    public CommentServiceImpl(CommentRepository commentRepo, Validator<Comment> commentValidator) {
        this.commentRepo = commentRepo;
        this.commentValidator = commentValidator;
    }

    @Override
    public List<Comment> getByRange(Integer begin, Integer count) {
        List<Comment> comments = commentRepo.findAll();
        return comments.subList(
                NumberNormalizer.normalize(begin, 0, comments.size() == 0 ? 0 : comments.size() - 1),
                NumberNormalizer.normalize(begin + count, 0, comments.size()));
    }

    @Override
    public Comment getById(Long commentId) throws CommentException {
        Optional<Comment> commentOpt = commentRepo.findById(commentId);
        if(!commentOpt.isPresent())
            throw new CommentException("Comment with id \'" + commentId + "\' has not found");
        return commentOpt.get();
    }

    @Override
    public Book getBookOfComment(Long commentId) throws CommentException {
        Optional<Comment> commentOpt = commentRepo.findById(commentId);
        if(!commentOpt.isPresent())
            throw new CommentException("Comment with id \'" + commentId + "\' has not found");
        return commentOpt.get().getBook();
    }

    @Override
    public User getUserOfComment(Long commentId) throws CommentException {
        Optional<Comment> commentOpt = commentRepo.findById(commentId);
        if(!commentOpt.isPresent())
            throw new CommentException("Comment with id \'" + commentId + "\' has not found");
        return commentOpt.get().getUser();
    }

    @Override
    public Comment saveComment(Comment comment) throws ValidationException {
        commentValidator.validate(comment);
        return commentRepo.save(comment);
    }

    @Override
    public void deleteById(Long commentId) throws CommentException {
        try {
            commentRepo.deleteById(commentId);
        } catch (EmptyResultDataAccessException e) {
            throw new CommentException("Comment with id \'" + commentId + "\' has not found");
        }
    }
}
