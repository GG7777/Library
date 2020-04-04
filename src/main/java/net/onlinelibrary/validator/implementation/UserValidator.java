package net.onlinelibrary.validator.implementation;

import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.User;
import net.onlinelibrary.repository.UserRepository;
import net.onlinelibrary.validator.Validator;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator<User> {
    private final Pattern usernamePattern;
    private final Pattern emailPattern;
    private final Pattern passwordPattern;

    private final UserRepository userRepo;

    public UserValidator(UserRepository userRepo) {
        this.userRepo = userRepo;
        usernamePattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-_\\.]{1,20}$");
        emailPattern = Pattern.compile(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                        "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        passwordPattern = Pattern.compile("(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");
    }

    @Override
    public void validate(@NotNull User user) throws ValidationException {
        validate(user, true, true);
    }

    public void validate(@NotNull User user,
                         boolean includePasswordValidation,
                         boolean includeValidateOnUnique) throws ValidationException {
        validatePropertiesOnNullOrEmpty(user);
        validateUsername(user.getUsername());
        validateEmail(user.getEmail());
        if (includePasswordValidation)
            validatePassword(user.getPassword());
        if (includeValidateOnUnique)
            validateUsernameAndEmailOnUnique(user.getUsername(), user.getEmail());
    }

    private void validateUsernameAndEmailOnUnique(String username, String email) throws ValidationException {
        if (userRepo.existsByUsernameOrEmailIgnoreCase(username, email))
            throw new ValidationException("username or email is already taken");
    }

    private void validatePropertiesOnNullOrEmpty(User user) throws ValidationException {
        if (user.getUsername() == null || user.getUsername().isEmpty() ||
            user.getEmail() == null || user.getEmail().isEmpty() ||
            user.getPassword() == null || user.getPassword().isEmpty() ||
            user.getRoles() == null || user.getRoles().isEmpty() ||
            user.getCreatedDate() == null ||
            user.getLastModifiedDate() == null ||
            user.getComments() == null ||
            user.getActive() == null)
            throw new ValidationException("username, email, password, roles, can not be null or empty and " +
                    "created and last modified dates, list of comments, active can not be null");
    }

    private void validateUsername(String username) throws ValidationException {
        if (!usernamePattern.matcher(username).matches())
            throw new ValidationException("username is incorrect");
    }

    private void validateEmail(String email) throws ValidationException {
        if (!emailPattern.matcher(email).matches())
            throw new ValidationException("email is incorrect");
    }

    private void validatePassword(String password) throws ValidationException {
        if (!passwordPattern.matcher(password).matches())
            throw new ValidationException("password is incorrect");
    }
}
