package net.onlinelibrary.validator.implementation;

import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.repository.GenreRepository;
import net.onlinelibrary.validator.Validator;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class GenreValidator implements Validator<Genre> {
    private final GenreRepository genreRepo;

    public GenreValidator(GenreRepository genreRepo) {
        this.genreRepo = genreRepo;
    }

    @Override
    public void validate(@NotNull Genre genre) throws ValidationException {
        validateOnNullOrEmpty(genre);
        validateGenreName(genre.getGenre());
        validateOnUniqueGenreName(genre.getGenre());
    }

    private void validateOnUniqueGenreName(String genre) throws ValidationException {
        if (genreRepo.existsByGenreIgnoreCase(genre))
            throw new ValidationException("genre must be unique, \'" + genre + "\' already exists");
    }

    private void validateGenreName(String genre) {
    }

    private void validateOnNullOrEmpty(Genre genre) throws ValidationException {
        if (genre.getGenre() == null || genre.getGenre().isEmpty() ||
            genre.getBooks() == null ||
            genre.getAuthors() == null ||
            genre.getCreatedDate() == null ||
            genre.getLastModifiedDate() == null)
            throw new ValidationException("genre name can not be null or empty and " +
                    "books, authors, created and last modified dates cat not be null");
    }
}
