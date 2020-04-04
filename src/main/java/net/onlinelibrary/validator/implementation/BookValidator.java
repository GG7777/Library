package net.onlinelibrary.validator.implementation;

import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.validator.Validator;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class BookValidator implements Validator<Book> {
    @Override
    public void validate(@NotNull Book book) throws ValidationException {
        validatePropertiesOnNullOrEmpty(book);
        validatePagesCount(book.getPagesCount());
        validateRating(book.getRating());
    }

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
