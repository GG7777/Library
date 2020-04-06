package net.onlinelibrary.substitute.implementation;

import net.onlinelibrary.model.Genre;
import net.onlinelibrary.substitute.PropertiesSubstitute;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GenrePropertiesSubstitute implements PropertiesSubstitute<Genre> {
    @Override
    public Genre substitute(Genre genre) {
        if (genre.getAuthors() == null)
            genre.setAuthors(new ArrayList<>());
        if (genre.getBooks() == null)
            genre.setBooks(new ArrayList<>());

        return genre;
    }
}
