package net.onlinelibrary.service.implementation;

import lombok.extern.slf4j.Slf4j;
import net.onlinelibrary.exception.AuthorException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.repository.AuthorRepository;
import net.onlinelibrary.service.AuthorService;
import net.onlinelibrary.util.NumberNormalizer;
import net.onlinelibrary.validator.implementation.AuthorValidator;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepo;
    private final AuthorValidator authorValidator;

    public AuthorServiceImpl(AuthorRepository authorRepo, AuthorValidator authorValidator) {
        this.authorRepo = authorRepo;
        this.authorValidator = authorValidator;
    }

    @Override
    public List<Author> getByRange(@NotNull Integer offset, @NotNull Integer count) {
        List<Author> allAuthors = authorRepo.findAll();

        List<Author> authorsInRange = allAuthors.subList(
                NumberNormalizer.normalize(offset, 0, allAuthors.size() == 0 ? 0 : allAuthors.size() - 1),
                NumberNormalizer.normalize(offset + count, 0, allAuthors.size()));

        log.info("IN getByRange - found " + authorsInRange.size() + " authors");

        return authorsInRange;
    }

    @Override
    public Author getById(@NotNull Long authorId) throws AuthorException {
        Optional<Author> authorOpt = authorRepo.findById(authorId);
        if (!authorOpt.isPresent()) {
            AuthorException authorException = new AuthorException("Author with id \'" + authorId + "\' has not found");
            log.warn("IN getById - " + authorException.getMessage());
            throw authorException;
        }
        Author author = authorOpt.get();
        log.info("IN getById - author with id " + author.getId() + " found");
        return author;
    }

    @Override
    public List<Book> getBooksOfAuthor(@NotNull Long authorId) throws AuthorException {
        Optional<Author> authorOpt = authorRepo.findById(authorId);
        if (!authorOpt.isPresent()) {
            AuthorException authorException = new AuthorException("Author with id \'" + authorId + "\' has not found");
            log.warn("IN getBooksOfAuthor - " + authorException.getMessage());
            throw authorException;
        }
        List<Book> books = authorOpt.get().getBooks();
        log.info("IN getBooksOfAuthor - found " + books.size() + " books of author with id " + authorId);
        return books;
    }

    @Override
    public List<Genre> getGenresOfAuthor(@NotNull Long authorId) throws AuthorException {
        Optional<Author> authorOpt = authorRepo.findById(authorId);
        if (!authorOpt.isPresent()) {
            AuthorException authorException = new AuthorException("Author with id \'" + authorId + "\' has not found");
            log.warn("IN getGenresOfAuthor - " + authorException.getMessage());
            throw authorException;
        }
        List<Genre> genres = authorOpt.get().getGenres();
        log.info("IN getGenresOfAuthor - found " + genres.size() + " genres of author with id " + authorId);
        return genres;
    }

    @Override
    public Author saveNewAuthor(@NotNull Author author) throws ValidationException {
        author.setId(null);
        author.setCreatedDate(new Date());
        author.setLastModifiedDate(new Date());

        try {
            authorValidator.validate(author);
        } catch (ValidationException e) {
            log.warn("IN saveNewAuthor - validation failure - " + e.getMessage());
            throw e;
        }

        Author savedAuthor = authorRepo.save(author);
        log.info("IN saveNewAuthor - author with id " + savedAuthor.getId() + " saved");
        return savedAuthor;
    }

    @Override
    public Author updateAuthor(@NotNull Long authorId, @NotNull Author author) throws AuthorException, ValidationException {
        Author authorFromRepo;
        try {
            authorFromRepo = getById(authorId);
        } catch (AuthorException e) {
            log.warn("IN updateAuthor - " + e.getMessage());
            throw e;
        }

        author.setId(authorFromRepo.getId());
        author.setCreatedDate(authorFromRepo.getCreatedDate());
        author.setLastModifiedDate(new Date());

        try {
            authorValidator.validate(author);
        } catch (ValidationException e) {
            log.warn("IN updateAuthor - validation failure - " + e.getMessage());
            throw e;
        }

        Author savedAuthor = authorRepo.save(author);
        log.info("IN updateAuthor - author with id " + savedAuthor.getId() + " updated");
        return savedAuthor;
    }

    @Override
    public void deleteById(@NotNull Long authorId) throws AuthorException {
        try {
            authorRepo.deleteById(authorId);
            log.info("IN deleteById - author with id " + authorId + " deleted");
        } catch (EmptyResultDataAccessException e) {
            AuthorException authorException = new AuthorException("Author with id \'" + authorId + "\' has not found");
            log.warn("IN deleteById - " + authorException.getMessage());
            throw authorException;
        }
    }
}
