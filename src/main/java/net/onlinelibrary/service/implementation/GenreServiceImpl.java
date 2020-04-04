package net.onlinelibrary.service.implementation;

import lombok.extern.slf4j.Slf4j;
import net.onlinelibrary.exception.GenreException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.repository.GenreRepository;
import net.onlinelibrary.service.GenreService;
import net.onlinelibrary.util.NumberNormalizer;
import net.onlinelibrary.validator.implementation.GenreValidator;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepo;
    private final GenreValidator genreValidator;

    public GenreServiceImpl(GenreRepository genreRepo, GenreValidator genreValidator) {
        this.genreRepo = genreRepo;
        this.genreValidator = genreValidator;
    }

    @Override
    public List<Genre> getByRange(@NotNull Integer offset, @NotNull Integer count) {
        List<Genre> allGenres = genreRepo.findAll();

        List<Genre> genresInRange = allGenres.subList(
                NumberNormalizer.normalize(offset, 0, allGenres.size() == 0 ? 0 : allGenres.size() - 1),
                NumberNormalizer.normalize(offset + count, 0, allGenres.size()));

        log.info("IN getByRange - found " + genresInRange.size() + " genres");

        return genresInRange;
    }

    @Override
    public Genre getById(@NotNull Long genreId) throws GenreException {
        Optional<Genre> genreOpt = genreRepo.findById(genreId);
        if(!genreOpt.isPresent()) {
            GenreException genreException = new GenreException("Genre with id \'" + genreId + "\' has not found");
            log.warn("IN getById - " + genreException.getMessage());
            throw genreException;
        }
        Genre genre = genreOpt.get();
        log.info("IN getById - genre with id " + genre.getId() + " found");
        return genre;
    }

    @Override
    public List<Book> getBooksOfGenre(@NotNull Long genreId) throws GenreException {
        Optional<Genre> genreOpt = genreRepo.findById(genreId);
        if(!genreOpt.isPresent()) {
            GenreException genreException = new GenreException("Genre with id \'" + genreId + "\' has not found");
            log.warn("IN getBooksOfGenre - " + genreException.getMessage());
            throw genreException;
        }
        List<Book> books = genreOpt.get().getBooks();
        log.info("IN getBooksOfGenre - found " + books.size() + " books of genre with id " + genreId);
        return books;
    }

    @Override
    public List<Author> getAuthorsOfGenre(@NotNull Long genreId) throws GenreException {
        Optional<Genre> genreOpt = genreRepo.findById(genreId);
        if(!genreOpt.isPresent()) {
            GenreException genreException = new GenreException("Genre with id \'" + genreId + "\' has not found");
            log.warn("IN getAuthorsOfGenre - " + genreException.getMessage());
            throw genreException;
        }
        List<Author> authors = genreOpt.get().getAuthors();
        log.info("IN getAuthorsOfGenre - found " + authors.size() + " authors of genre with id " + genreId);
        return authors;
    }

    @Override
    public Genre saveNewGenre(@NotNull Genre genre) throws ValidationException {
        genre.setId(null);
        genre.setCreatedDate(new Date());
        genre.setLastModifiedDate(new Date());

        try {
            genreValidator.validate(genre);
        } catch (ValidationException e) {
            log.warn("IN saveNewGenre - validation failure - " + e.getMessage());
            throw e;
        }

        Genre savedGenre = genreRepo.save(genre);
        log.info("IN saveNewGenre - genre with id " + savedGenre.getId() + " saved");
        return savedGenre;
    }

    @Override
    public Genre updateGenre(@NotNull Long genreId, @NotNull Genre genre) throws GenreException, ValidationException {
        Genre genreFromRepo;
        try {
            genreFromRepo = getById(genreId);
        } catch (GenreException e) {
            log.warn("IN updateGenre - " + e.getMessage());
            throw e;
        }

        genre.setId(genreFromRepo.getId());
        genre.setCreatedDate(genreFromRepo.getCreatedDate());
        genre.setLastModifiedDate(new Date());

        try {
            genreValidator.validate(genre);
        } catch (ValidationException e) {
            log.warn("IN updateGenre - validation failure - " + e.getMessage());
            throw e;
        }

        Genre savedGenre = genreRepo.save(genre);
        log.info("IN updateGenre - genre with id " + savedGenre.getId() + " updated");
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
            GenreException genreException = new GenreException("Genre with id \'" + genreId + "\' has not found");
            log.warn("IN deleteById - " + genreException.getMessage());
            throw genreException;
        }
    }
}
