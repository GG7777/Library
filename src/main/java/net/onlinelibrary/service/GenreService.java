package net.onlinelibrary.service;

import net.onlinelibrary.exception.GenreException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getByRange(Integer begin, Integer count);

    Genre getById(Long genreId) throws GenreException;

    List<Book> getBooksOfGenre(Long genreId) throws GenreException;

    List<Author> getAuthorsOfGenre(Long genreId) throws GenreException;

    Genre saveGenre(Genre genre);

    Genre deleteById(Long genreId) throws GenreException;
}
