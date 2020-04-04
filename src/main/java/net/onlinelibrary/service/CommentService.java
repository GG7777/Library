package net.onlinelibrary.service;

import net.onlinelibrary.exception.CommentException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.User;

import java.util.List;

public interface CommentService {
    List<Comment> getByRange(Integer offset, Integer count);

    Comment getById(Long commentId) throws CommentException;

    Book getBookOfComment(Long commentId) throws CommentException;

    User getUserOfComment(Long commentId) throws CommentException;

    Comment saveNewComment(Comment comment) throws ValidationException;

    Comment updateComment(Long commentId, Comment comment) throws CommentException, ValidationException;

    Comment updateCommentText(Long commentId, String text) throws CommentException, ValidationException;

    void deleteById(Long commentId) throws CommentException;
}
