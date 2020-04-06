package net.onlinelibrary.service;

import net.onlinelibrary.exception.AuthorException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Genre;

import java.util.BitSet;
import java.util.List;

public interface AuthorService {
    List<Author> getByRange(Integer offset, Integer count);

    Author getById(Long authorId) throws AuthorException;

    List<Book> getBooksOfAuthor(Long authorId) throws AuthorException;

    List<Genre> getGenresOfAuthor(Long authorId) throws AuthorException;

    Author saveNewAuthor(Author author) throws ValidationException;

    Author updateAuthor(Long authorId, Author author) throws AuthorException, ValidationException;

    void deleteById(Long authorId) throws AuthorException;

    Long getAuthorsCount();

    List<Author> searchBy(String firstName, String middleName, String lastName);
}
