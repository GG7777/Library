package net.onlinelibrary.validator.implementation;

import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.validator.Validator;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class BookValidator implements Validator<Book> {
    @Override
    public void validate(@NotNull Book book) throws ValidationException {
        validatePropertiesOnNullOrEmpty(book);
        validatePagesCount(book.getPagesCount());
        validateRating(book.getRating());
//        validateAuthors(book.getAuthors());
//        validateGenres(book.getGenres());
    }

//    private void validateGenres(List<Genre> genres) throws ValidationException {
//        if (genres.size() == 0)
//            throw new ValidationException("book must contain one or more genres");
//        Genre empty = Genre.getEmpty();
//        if (genres
//                .stream()
//                .anyMatch(genre -> empty.getGenre().equals(genre.getGenre())) &&
//            genres.size() > 1)
//            throw new ValidationException("book should not contain both empty and non-empty genres");
//    }
//
//    private void validateAuthors(List<Author> authors) throws ValidationException {
//        if (authors.size() == 0)
//            throw new ValidationException("book must contain one or more authors");
//        Author empty = Author.getEmpty();
//        if (authors
//                .stream()
//                .anyMatch(author ->
//                        empty.getFirstName().equals(author.getFirstName()) &&
//                        empty.getMiddleName().equals(author.getMiddleName()) &&
//                        empty.getLastName().equals(author.getLastName())) &&
//            authors.size() > 1)
//            throw new ValidationException("book should not contain both empty and non-empty authors");
//    }

    private void validateRating(Integer rating) throws ValidationException {
        if (rating < 0)
            throw new ValidationException("rating must be more or equal to zero");
    }

    private void validatePagesCount(Integer pagesCount) throws ValidationException {
        if (pagesCount < 0)
            throw new ValidationException("pages count must be more or equal to zero");
    }

    private void validatePropertiesOnNullOrEmpty(Book book) throws ValidationException {
        if (book.getComments() == null ||
            book.getGenres() == null ||
            book.getAuthors() == null ||
            book.getShortDescription() == null ||
            book.getRating() == null ||
            book.getPublicationYear() == null ||
            book.getPagesCount() == null ||
            book.getName() == null ||
            book.getAvatar() == null ||
            book.getCreatedDate() == null ||
            book.getLastModifiedDate() == null)
            throw new ValidationException("comments, genres, authors, short description, " +
                    "rating, publication year, pages count, name, avatar, " +
                    "created and last modified dates can not be null");
    }
}
