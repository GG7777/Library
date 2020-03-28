package net.onlinelibrary.service.Implementation;

import net.onlinelibrary.exception.BookException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Override
    public List<Book> getByRange(Integer begin, Integer count) {
        return null;
    }

    @Override
    public Book getById(Long bookId) throws BookException {
        return null;
    }

    @Override
    public List<Author> getAuthorsOfBook(Long bookId) throws BookException {
        return null;
    }

    @Override
    public List<Genre> getGenresOfBook(Long bookId) throws BookException {
        return null;
    }

    @Override
    public List<Comment> getCommentsOfBook(Long bookId) throws BookException {
        return null;
    }

    @Override
    public Book saveBook(Book book) {
        return null;
    }

    @Override
    public Book deleteById(Long bookId) throws BookException {
        return null;
    }
}
