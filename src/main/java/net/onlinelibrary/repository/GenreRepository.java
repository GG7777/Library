package net.onlinelibrary.repository;

import net.onlinelibrary.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    boolean existsByGenreIgnoreCase(String genre);
}
