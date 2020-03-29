package net.onlinelibrary.validator.implementation;

import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class CommentValidator implements Validator<Comment> {
    @Override
    public void validate(Comment comment) throws ValidationException {
        validateText(comment.getText());
        validateRating(comment.getRating());
    }

    private void validateText(String text) {

    }

    private void validateRating(Long rating) throws ValidationException {
        if (rating < 0)
            throw new ValidationException("Rating must be not negative number");
    }
}
