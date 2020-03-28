package net.onlinelibrary.service;

import net.onlinelibrary.exception.CommentException;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.User;

import java.util.List;

public interface CommentService {
    List<Comment> getByRange(Integer begin, Integer count);

    Comment getById(Long commentId) throws CommentException;

    Book getBookOfComment(Long commentId) throws CommentException;

    User getUserOfComment(Long commentId) throws CommentException;

    Comment saveComment(Comment comment);

    void deleteById(Long commentId) throws CommentException;
}
