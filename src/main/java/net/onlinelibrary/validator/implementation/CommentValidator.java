package net.onlinelibrary.validator.implementation;

import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.validator.Validator;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class CommentValidator implements Validator<Comment> {
    @Override
    public void validate(@NotNull Comment comment) throws ValidationException {
        validatePropertiesOnNullOrEmpty(comment);
        validateText(comment.getText());
    }

    private void validatePropertiesOnNullOrEmpty(Comment comment) throws ValidationException {
        if (comment.getText() == null || comment.getText().isEmpty() ||
            comment.getRating() == null ||
            comment.getUser() == null ||
            comment.getBook() == null ||
            comment.getCreatedDate() == null ||
            comment.getLastModifiedDate() == null)
            throw new ValidationException("text can not be null or empty and " +
                    "rating, user, book, created and last modified date can not be null");
    }

    private void validateText(String text) throws ValidationException {
        if (text.length() > 500)
            throw new ValidationException("comment text length must be less or equal to 500");
    }
}
