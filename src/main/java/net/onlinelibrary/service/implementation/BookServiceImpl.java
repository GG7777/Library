package net.onlinelibrary.service.implementation;

import lombok.extern.slf4j.Slf4j;
import net.onlinelibrary.exception.BookException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.repository.BookRepository;
import net.onlinelibrary.service.BookService;
import net.onlinelibrary.substitute.implementation.BookPropertiesSubstitute;
import net.onlinelibrary.util.NumberNormalizer;
import net.onlinelibrary.validator.implementation.BookValidator;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepo;
    private final BookValidator bookValidator;
    private final BookPropertiesSubstitute bookPropsSubstitute;

    public BookServiceImpl(
            BookRepository bookRepo,
            BookValidator bookValidator,
            BookPropertiesSubstitute bookPropsSubstitute) {
        this.bookRepo = bookRepo;
        this.bookValidator = bookValidator;
        this.bookPropsSubstitute = bookPropsSubstitute;
    }

    @Override
    public List<Book> getByRange(@NotNull Integer offset, @NotNull Integer count) {
        List<Book> allBooks = bookRepo.findAll();

        List<Book> booksInRange = allBooks.subList(
                NumberNormalizer.normalize(offset, 0, allBooks.size() == 0 ? 0 : allBooks.size() - 1),
                NumberNormalizer.normalize(offset + count, 0, allBooks.size()));

        log.info("IN getByRange - found " + booksInRange.size() + " books");

        return booksInRange;
    }

    @Override
    public Book getById(@NotNull Long bookId) throws BookException {
        Optional<Book> bookOpt = bookRepo.findById(bookId);
        if (!bookOpt.isPresent()) {
            BookException bookException = new BookException("Book with id \'" + bookId + "\' has not found");
            log.warn("IN getById - " + bookException.getMessage());
            throw bookException;
        }
        Book book = bookOpt.get();
        log.info("IN getById - book with id " + book.getId() + " found");
        return book;
    }

    @Override
    public List<Author> getAuthorsOfBook(@NotNull Long bookId) throws BookException {
        Optional<Book> bookOpt = bookRepo.findById(bookId);
        if (!bookOpt.isPresent()) {
            BookException bookException = new BookException("Book with id \'" + bookId + "\' has not found");
            log.warn("IN getAuthorsOfBook - " + bookException.getMessage());
            throw bookException;
        }
        List<Author> authors = bookOpt.get().getAuthors();
        log.info("IN getAuthorsOfBook - found " + authors.size() + " authors of book with id " + bookId);
        return authors;
    }

    @Override
    public List<Genre> getGenresOfBook(@NotNull Long bookId) throws BookException {
        Optional<Book> bookOpt = bookRepo.findById(bookId);
        if (!bookOpt.isPresent()) {
            BookException bookException = new BookException("Book with id \'" + bookId + "\' has not found");
            log.warn("IN getGenresOfBook - " + bookException.getMessage());
            throw bookException;
        }
        List<Genre> genres = bookOpt.get().getGenres();
        log.info("IN getGenresOfBook - found " + genres.size() + " genres of book with id " + bookId);
        return genres;
    }

    @Override
    public List<Comment> getCommentsOfBook(@NotNull Long bookId) throws BookException {
        Optional<Book> bookOpt = bookRepo.findById(bookId);
        if (!bookOpt.isPresent()) {
            BookException bookException = new BookException("Book with id \'" + bookId + "\' has not found");
            log.warn("IN getCommentsOfBook - " + bookException.getMessage());
            throw bookException;
        }
        List<Comment> comments = bookOpt.get().getComments();
        log.info("IN getCommentsOfBook - found " + comments.size() + " comments of book with id " + bookId);
        return comments;
    }

    @Override
    public Book saveNewBook(@NotNull Book book) throws ValidationException {
        book.setId(null);
        book.setCreatedDate(new Date());
        book.setLastModifiedDate(new Date());

        book = bookPropsSubstitute.substitute(book);

        try {
            bookValidator.validate(book);
        } catch (ValidationException e) {
            log.warn("IN saveNewBook - validation failure - " + e.getMessage());
            throw e;
        }

        Book savedBook = bookRepo.save(book);
        log.info("IN saveNewBook - book with id " + savedBook.getId() + " saved");
        return savedBook;
    }

    @Override
    public Book updateBook(@NotNull Long bookId, @NotNull Book book) throws BookException, ValidationException {
        Book bookFromRepo;
        try {
            bookFromRepo = getById(bookId);
        } catch (BookException e) {
            log.warn("IN updateBook - " + e.getMessage());
            throw e;
        }

        book.setId(bookFromRepo.getId());
        book.setCreatedDate(bookFromRepo.getCreatedDate());
        book.setLastModifiedDate(new Date());

        try {
            bookValidator.validate(book);
        } catch (ValidationException e) {
            log.warn("IN updateBook - validation failure - " + e.getMessage());
            throw e;
        }

        Book savedBook = bookRepo.save(book);
        log.info("IN updateBook - book with id " + savedBook.getId() + " updated");
        return savedBook;
    }

    @Override
    public void deleteById(@NotNull Long bookId) throws BookException {
        try {
            bookRepo.deleteById(bookId);
            log.info("IN deleteById - book with id " + bookId + " deleted");
        } catch (EmptyResultDataAccessException e) {
            BookException bookException = new BookException("Book with id \'" + bookId + "\' has not found");
            log.warn("IN deleteById - " + bookException.getMessage());
            throw bookException;
        }
    }

    @Override
    public Long getBooksCount() {
        long count = bookRepo.count();
        log.info("IN getBooksCount - total books count = " + count);
        return count;
    }
}
