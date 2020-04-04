package net.onlinelibrary.validator.implementation;

import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class AuthorValidator implements Validator<Author> {
    @Override
    public void validate(Author author) throws ValidationException {
        validateOnNullOrEmpty(author);
    }

    private void validateOnNullOrEmpty(Author author) throws ValidationException {
        if (author.getGenres() == null ||
            author.getBooks() == null ||
            author.getLastName() == null ||
            author.getMiddleName() == null ||
            author.getFirstName() == null ||
            author.getCreatedDate() == null ||
            author.getLastModifiedDate() == null)
            throw new ValidationException("genres, books, last name, middle name, first name, " +
                    "created and last modified dates can not be null");
    }
}
