package net.onlinelibrary.service.Implementation;

import net.onlinelibrary.exception.GenreException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.service.GenreService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    @Override
    public List<Genre> getByRange(Integer begin, Integer count) {
        return null;
    }

    @Override
    public Genre getById(Long genreId) throws GenreException {
        return null;
    }

    @Override
    public List<Book> getBooksOfGenre(Long genreId) throws GenreException {
        return null;
    }

    @Override
    public List<Author> getAuthorsOfGenre(Long genreId) throws GenreException {
        return null;
    }

    @Override
    public Genre saveGenre(Genre genre) {
        return null;
    }

    @Override
    public Genre deleteById(Long genreId) throws GenreException {
        return null;
    }
}
