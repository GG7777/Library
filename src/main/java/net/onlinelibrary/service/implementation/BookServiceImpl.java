package net.onlinelibrary.service.implementation;

import lombok.extern.slf4j.Slf4j;
import net.onlinelibrary.exception.BookException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.repository.BookRepository;
import net.onlinelibrary.service.BookService;
import net.onlinelibrary.util.NumberNormalizer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepo;

    public BookServiceImpl(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public List<Book> getByRange(@NotNull Integer begin, @NotNull Integer count) {
        List<Book> allBooks = bookRepo.findAll();

        List<Book> booksInRange = allBooks.subList(
                NumberNormalizer.normalize(begin, 0, allBooks.size() == 0 ? 0 : allBooks.size() - 1),
                NumberNormalizer.normalize(begin + count, 0, allBooks.size()));

        log.info("IN getByRange - found " + booksInRange.size() + " books");

        return booksInRange;
    }

    @Override
    public Book getById(@NotNull Long bookId) throws BookException {
        Optional<Book> bookOpt = bookRepo.findById(bookId);
        if (!bookOpt.isPresent()) {
            log.warn("IN getById - book with id " + bookId + " has not found");
            throw new BookException("Book with id \'" + bookId + "\' has not found");
        }
        Book book = bookOpt.get();
        log.info("IN getById - book with id " + book.getId() + " found");
        return book;
    }

    @Override
    public List<Author> getAuthorsOfBook(@NotNull Long bookId) throws BookException {
        Optional<Book> bookOpt = bookRepo.findById(bookId);
        if (!bookOpt.isPresent()) {
            log.warn("IN getAuthorsOfBook - book with id " + bookId + " has not found");
            throw new BookException("Book with id \'" + bookId + "\' has not found");
        }
        List<Author> authors = bookOpt.get().getAuthors();
        log.info("IN getAuthorsOfBook - found " + authors.size() + " authors of book with id " + bookId);
        return authors;
    }

    @Override
    public List<Genre> getGenresOfBook(@NotNull Long bookId) throws BookException {
        Optional<Book> bookOpt = bookRepo.findById(bookId);
        if (!bookOpt.isPresent()) {
            log.warn("IN getGenresOfBook - book with id " + bookId + " has not found");
            throw new BookException("Book with id \'" + bookId + "\' has not found");
        }
        List<Genre> genres = bookOpt.get().getGenres();
        log.info("IN getGenresOfBook - found " + genres.size() + " genres of book with id " + bookId);
        return genres;
    }

    @Override
    public List<Comment> getCommentsOfBook(@NotNull Long bookId) throws BookException {
        Optional<Book> bookOpt = bookRepo.findById(bookId);
        if (!bookOpt.isPresent()) {
            log.warn("IN getCommentsOfBook - book with id " + bookId + " has not found");
            throw new BookException("Book with id \'" + bookId + "\' has not found");
        }
        List<Comment> comments = bookOpt.get().getComments();
        log.info("IN getCommentsOfBook - found " + comments.size() + " comments of book with id " + bookId);
        return comments;
    }

    @Override
    public Book saveBook(@NotNull Book book) {
        Book savedBook = bookRepo.save(book);
        log.info("IN saveBook - " +
                (book.getId() == savedBook.getId() ? "updated" : "saved new") +
                " book with id " + savedBook.getId());
        return savedBook;
    }

    @Override
    public void deleteById(@NotNull Long bookId) throws BookException {
        try {
            bookRepo.deleteById(bookId);
            log.info("IN deleteById - book with id " + bookId + " deleted");
        } catch (EmptyResultDataAccessException e) {
            log.warn("IN deleteById - book with id " + bookId + " has not found");
            throw new BookException("Book with id \'" + bookId + "\' has not found");
        }
    }
}
