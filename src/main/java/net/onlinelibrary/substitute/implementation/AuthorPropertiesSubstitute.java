package net.onlinelibrary.substitute.implementation;

import net.onlinelibrary.model.Author;
import net.onlinelibrary.substitute.PropertiesSubstitute;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AuthorPropertiesSubstitute implements PropertiesSubstitute<Author> {
    @Override
    public Author substitute(Author author) {
        if (author.getGenres() == null)
            author.setGenres(new ArrayList<>());
        if (author.getBooks() == null)
            author.setBooks(new ArrayList<>());

        return author;
    }
}
