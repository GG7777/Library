package net.onlinelibrary.validator;

import net.onlinelibrary.exception.ValidationException;

public interface Validator<TValidation> {
    void validate(TValidation obj) throws ValidationException;
}
