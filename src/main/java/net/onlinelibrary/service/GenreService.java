package net.onlinelibrary.service;

import net.onlinelibrary.exception.GenreException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getByRange(Integer offset, Integer count);

    Genre getById(Long genreId) throws GenreException;

    List<Book> getBooksOfGenre(Long genreId) throws GenreException;

    List<Author> getAuthorsOfGenre(Long genreId) throws GenreException;

    Genre saveNewGenre(Genre genre) throws ValidationException;

    Genre updateGenre(Long genreId, Genre genre) throws GenreException, ValidationException;

    void deleteById(Long genreId) throws GenreException;
}
