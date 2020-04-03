package net.onlinelibrary.service.implementation;

import lombok.extern.slf4j.Slf4j;
import net.onlinelibrary.exception.GenreException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.repository.GenreRepository;
import net.onlinelibrary.service.GenreService;
import net.onlinelibrary.util.NumberNormalizer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepo;

    public GenreServiceImpl(GenreRepository genreRepo) {
        this.genreRepo = genreRepo;
    }

    @Override
    public List<Genre> getByRange(@NotNull Integer begin, @NotNull Integer count) {
        List<Genre> allGenres = genreRepo.findAll();

        List<Genre> genresInRange = allGenres.subList(
                NumberNormalizer.normalize(begin, 0, allGenres.size() == 0 ? 0 : allGenres.size() - 1),
                NumberNormalizer.normalize(begin + count, 0, allGenres.size()));

        log.info("IN getByRange - found " + genresInRange.size() + " genres");

        return genresInRange;
    }

    @Override
    public Genre getById(@NotNull Long genreId) throws GenreException {
        Optional<Genre> genreOpt = genreRepo.findById(genreId);
        if(!genreOpt.isPresent()) {
            log.warn("IN getById - genre with id " + genreId + " has not found");
            throw new GenreException("Genre with id \'" + genreId + "\' has not found");
        }
        Genre genre = genreOpt.get();
        log.info("IN getById - genre with id " + genre.getId() + " found");
        return genre;
    }

    @Override
    public List<Book> getBooksOfGenre(@NotNull Long genreId) throws GenreException {
        Optional<Genre> genreOpt = genreRepo.findById(genreId);
        if(!genreOpt.isPresent()) {
            log.warn("IN getBooksOfGenre - genre with id " + genreId + " has not found");
            throw new GenreException("Genre with id \'" + genreId + "\' has not found");
        }
        List<Book> books = genreOpt.get().getBooks();
        log.info("IN getBooksOfGenre - found " + books.size() + " books of genre with id " + genreId);
        return books;
    }

    @Override
    public List<Author> getAuthorsOfGenre(@NotNull Long genreId) throws GenreException {
        Optional<Genre> genreOpt = genreRepo.findById(genreId);
        if(!genreOpt.isPresent()) {
            log.warn("IN getAuthorsOfGenre - genre with id " + genreId + " has not found");
            throw new GenreException("Genre with id \'" + genreId + "\' has not found");
        }
        List<Author> authors = genreOpt.get().getAuthors();
        log.info("IN getAuthorsOfGenre - found " + authors.size() + " authors of genre with id " + genreId);
        return authors;
    }

    @Override
    public Genre saveGenre(@NotNull Genre genre) {
        Genre savedGenre = genreRepo.save(genre);
        log.info("IN saveGenre - " +
                (genre.getId() == savedGenre.getId() ? "updated" : "saved new") +
                " genre with id " + savedGenre.getId());
        return savedGenre;
    }

    @Override
    public void deleteById(@NotNull Long genreId) throws GenreException {
        try {
            genreRepo.deleteById(genreId);
            log.info("IN deleteById - genre with id " + genreId + " deleted");
        }
        catch (EmptyResultDataAccessException e)
        {
            log.warn("IN deleteById - genre with id " + genreId + " has not found");
            throw new GenreException("Genre with id \'" + genreId + "\' has not found");
        }
    }
}
