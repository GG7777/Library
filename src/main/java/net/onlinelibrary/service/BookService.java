package net.onlinelibrary.service;

import net.onlinelibrary.exception.BookException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.Genre;

import java.util.List;

public interface BookService {

    List<Book> getByRange(Integer offset, Integer count);

    Book getById(Long bookId) throws BookException;

    List<Author> getAuthorsOfBook(Long bookId) throws BookException;

    List<Genre> getGenresOfBook(Long bookId) throws BookException;

    List<Comment> getCommentsOfBook(Long bookId) throws BookException;

    Book saveNewBook(Book book) throws ValidationException;

    Book updateBook(Long bookId, Book book) throws BookException, ValidationException;

    void deleteById(Long bookId) throws BookException;
}
