package net.onlinelibrary.service.Implementation;

import net.onlinelibrary.exception.CommentException;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.User;
import net.onlinelibrary.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Override
    public List<Comment> getByRange(Integer begin, Integer count) {
        return null;
    }

    @Override
    public Comment getById(Long commentId) throws CommentException {
        return null;
    }

    @Override
    public Book getBookOfComment(Long commentId) throws CommentException {
        return null;
    }

    @Override
    public User getUserOfComment(Long commentId) throws CommentException {
        return null;
    }

    @Override
    public Comment saveComment(Comment comment) {
        return null;
    }

    @Override
    public Comment deleteById(Long authorId) throws CommentException {
        return null;
    }
}
