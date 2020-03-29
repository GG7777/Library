package net.onlinelibrary.validator.implementation;

import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.User;
import net.onlinelibrary.validator.Validator;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator<User> {
    private final Pattern usernamePattern;
    private final Pattern emailPattern;
    private final Pattern passwordPattern;

    public UserValidator() {
        usernamePattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-_\\.]{1,20}$");
        emailPattern = Pattern.compile("^[\\\\w!#$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}$");
        passwordPattern = Pattern.compile("(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");
    }

    @Override
    public void validate(User user) throws ValidationException {
        validateUsername(user.getUsername());
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
    }

    private void validateUsername(@NotNull String username) throws ValidationException {
        if (!usernamePattern.matcher(username).matches())
            throw new ValidationException("Username is incorrect");
    }

    private void validateEmail(String email) throws ValidationException {
        if (!emailPattern.matcher(email).matches())
            throw new ValidationException("Email is incorrect");
    }

    private void validatePassword(String password) throws ValidationException {
        if (!passwordPattern.matcher(password).matches())
            throw new ValidationException("Password is incorrect");
    }
}
