package net.onlinelibrary.service.implementation;

import lombok.extern.slf4j.Slf4j;
import net.onlinelibrary.exception.AuthorException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.repository.AuthorRepository;
import net.onlinelibrary.service.AuthorService;
import net.onlinelibrary.util.NumberNormalizer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepo;

    public AuthorServiceImpl(AuthorRepository authorRepo) {
        this.authorRepo = authorRepo;
    }

    @Override
    public List<Author> getByRange(@NotNull Integer begin, @NotNull Integer count) {
        List<Author> allAuthors = authorRepo.findAll();

        List<Author> authorsInRange = allAuthors.subList(
                NumberNormalizer.normalize(begin, 0, allAuthors.size() == 0 ? 0 : allAuthors.size() - 1),
                NumberNormalizer.normalize(begin + count, 0, allAuthors.size()));

        log.info("IN getByRange - found " + authorsInRange.size() + " authors");

        return authorsInRange;
    }

    @Override
    public Author getById(@NotNull Long authorId) throws AuthorException {
        Optional<Author> authorOpt = authorRepo.findById(authorId);
        if (!authorOpt.isPresent()) {
            log.warn("IN getById - author with id " + authorId + " has not found");
            throw new AuthorException("Author with id \'" + authorId + "\' has not found");
        }
        Author author = authorOpt.get();
        log.info("IN getById - author with id " + author.getId() + " found");
        return author;
    }

    @Override
    public List<Book> getBooksOfAuthor(@NotNull Long authorId) throws AuthorException {
        Optional<Author> authorOpt = authorRepo.findById(authorId);
        if (!authorOpt.isPresent()) {
            log.warn("IN getBooksOfAuthor - author with id " + authorId + " has not found");
            throw new AuthorException("Author with id \'" + authorId + "\' has not found");
        }
        List<Book> books = authorOpt.get().getBooks();
        log.info("IN getBooksOfAuthor - found " + books.size() + " books of author with id " + authorId);
        return books;
    }

    @Override
    public List<Genre> getGenresOfAuthor(@NotNull Long authorId) throws AuthorException {
        Optional<Author> authorOpt = authorRepo.findById(authorId);
        if (!authorOpt.isPresent()) {
            log.warn("IN getGenresOfAuthor - author with id " + authorId + " has not found");
            throw new AuthorException("Author with id \'" + authorId + "\' has not found");
        }
        List<Genre> genres = authorOpt.get().getGenres();
        log.info("IN getGenresOfAuthor - found " + genres.size() + " genres of author with id " + authorId);
        return genres;
    }

    @Override
    public Author saveAuthor(@NotNull Author author) {
        Author savedAuthor = authorRepo.save(author);
        log.info("IN saveAuthor - " +
                (author.getId() == savedAuthor.getId() ? "updated" : "saved new") +
                " author with id " + savedAuthor.getId());
        return savedAuthor;
    }

    @Override
    public void deleteById(@NotNull Long authorId) throws AuthorException {
        try {
            authorRepo.deleteById(authorId);
            log.info("IN deleteById - author with id " + authorId + " deleted");
        } catch (EmptyResultDataAccessException e) {
            log.warn("IN deleteById - author with id " + authorId + " has not found");
            throw new AuthorException("Author with id \'" + authorId + "\' has not found");
        }
    }
}
