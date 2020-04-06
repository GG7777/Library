package net.onlinelibrary.substitute.implementation;

import net.onlinelibrary.model.Book;
import net.onlinelibrary.substitute.PropertiesSubstitute;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Component
public class BookPropertiesSubstitute implements PropertiesSubstitute<Book> {
    @Override
    public Book substitute(@NotNull Book book) {
        if (book.getGenres() == null)
            book.setGenres(new ArrayList<>());
        if (book.getAuthors() == null)
            book.setAuthors(new ArrayList<>());
        if (book.getComments() == null)
            book.setComments(new ArrayList<>());

        book.setRating(0l);

        return book;
    }
}
