package net.onlinelibrary.service;

import net.onlinelibrary.exception.AuthorException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Genre;

import java.util.List;

public interface AuthorService {
    List<Author> getByRange(Integer begin, Integer count);

    Author getById(Long authorId) throws AuthorException;

    List<Book> getBooksOfAuthor(Long authorId) throws AuthorException;

    List<Genre> getGenresOfAuthor(Long authorId) throws AuthorException;

    Author saveAuthor(Author author);

    Author deleteById(Long authorId) throws AuthorException;
}
