package net.onlinelibrary.repository;

import net.onlinelibrary.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByFirstNameStartsWithIgnoreCaseAndMiddleNameStartsWithIgnoreCaseAndLastNameStartsWithIgnoreCase(String firstName, String middleName, String lastName);

    List<Author> findByFirstNameStartsWithIgnoreCaseAndLastNameStartsWithIgnoreCase(String firstName, String lastName);

    List<Author> findByLastNameStartsWithIgnoreCase(String lastName);
}
