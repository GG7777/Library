package net.onlinelibrary.repository;

import net.onlinelibrary.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    boolean existsByGenreIgnoreCase(String genre);

    List<Genre> findByGenreStartsWithIgnoreCase(String subStr);
}
