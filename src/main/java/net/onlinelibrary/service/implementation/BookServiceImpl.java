package net.onlinelibrary.service.implementation;

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

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepo;

    public BookServiceImpl(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public List<Book> getByRange(Integer begin, Integer count) {
        List<Book> books = bookRepo.findAll();

        return books.subList(
                NumberNormalizer.normalize(begin, 0, books.size() == 0 ? 0 : books.size() - 1),
                NumberNormalizer.normalize(begin + count - 1, 0, books.size()));
    }

    @Override
    public Book getById(Long bookId) throws BookException {
        Optional<Book> bookOpt = bookRepo.findById(bookId);
        if (!bookOpt.isPresent())
            throw new BookException("Book with id \'" + bookId + "\' has not found");
        return bookOpt.get();
    }

    @Override
    public List<Author> getAuthorsOfBook(Long bookId) throws BookException {
        Optional<Book> bookOpt = bookRepo.findById(bookId);
        if (!bookOpt.isPresent())
            throw new BookException("Book with id \'" + bookId + "\' has not found");
        return bookOpt.get().getAuthors();
    }

    @Override
    public List<Genre> getGenresOfBook(Long bookId) throws BookException {
        Optional<Book> bookOpt = bookRepo.findById(bookId);
        if (!bookOpt.isPresent())
            throw new BookException("Book with id \'" + bookId + "\' has not found");
        return bookOpt.get().getGenres();
    }

    @Override
    public List<Comment> getCommentsOfBook(Long bookId) throws BookException {
        Optional<Book> bookOpt = bookRepo.findById(bookId);
        if (!bookOpt.isPresent())
            throw new BookException("Book with id \'" + bookId + "\' has not found");
        return bookOpt.get().getComments();
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepo.save(book);
    }

    @Override
    public void deleteById(Long bookId) throws BookException {
        try {
            bookRepo.deleteById(bookId);
        } catch (EmptyResultDataAccessException e) {
            throw new BookException("Book with id \'" + bookId + "\' has not found");
        }
    }
}
