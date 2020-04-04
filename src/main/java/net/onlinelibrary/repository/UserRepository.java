package net.onlinelibrary.repository;

import net.onlinelibrary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> findByUsernameOrEmailIgnoreCase(String username, String email);

    boolean existsByUsernameOrEmailIgnoreCase(String username, String email);
}
