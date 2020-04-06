package net.onlinelibrary.filter.implementation;

import net.onlinelibrary.filter.Filter;
import net.onlinelibrary.model.Genre;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenreFilter implements Filter<Genre> {
    private final Genre empty;

    public GenreFilter() {
        empty = Genre.getEmpty();
    }

    @Override
    public Genre doFilter(@NotNull Genre genre) {
        if (empty.getGenre().equals(genre.getGenre()))
            return null;
        return genre;
    }

    @Override
    public List<Genre> doFilter(@NotNull List<Genre> genres) {
        return genres
                .stream()
                .filter(genre -> doFilter(genre) != null)
                .collect(Collectors.toList());
    }
}
