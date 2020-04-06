package net.onlinelibrary.filter.implementation;

import net.onlinelibrary.filter.Filter;
import net.onlinelibrary.model.Author;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorFilter implements Filter<Author> {
    private final Author empty;

    public AuthorFilter() {
        empty = Author.getEmpty();
    }

    @Override
    public Author doFilter(@NotNull Author author) {
        if (empty.getLastName().equals(author.getLastName()) &&
            empty.getMiddleName().equals(author.getMiddleName()) &&
            empty.getFirstName().equals(author.getFirstName()))
            return null;
        return author;
    }

    @Override
    public List<Author> doFilter(@NotNull List<Author> authors) {
        return authors
                .stream()
                .filter(author -> doFilter(author) != null)
                .collect(Collectors.toList());
    }
}
