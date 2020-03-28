package net.onlinelibrary.service;

import net.onlinelibrary.exception.BookException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.Genre;

import java.util.List;

public interface BookService {

    List<Book> getByRange(Integer begin, Integer count);

    Book getById(Long bookId) throws BookException;

    List<Author> getAuthorsOfBook(Long bookId) throws BookException;

    List<Genre> getGenresOfBook(Long bookId) throws BookException;

    List<Comment> getCommentsOfBook(Long bookId) throws BookException;

    Book saveBook(Book book);

    void deleteById(Long bookId) throws BookException;
}
