package net.onlinelibrary.service.Implementation;

import net.onlinelibrary.exception.AuthorException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    @Override
    public List<Author> getByRange(Integer begin, Integer count) {
        return null;
    }

    @Override
    public Author getById(Long authorId) throws AuthorException {
        return null;
    }

    @Override
    public List<Book> getBooksOfAuthor(Long authorId) throws AuthorException {
        return null;
    }

    @Override
    public List<Genre> getGenresOfAuthor(Long authorId) throws AuthorException {
        return null;
    }

    @Override
    public Author saveAuthor(Author author) {
        return null;
    }

    @Override
    public Author deleteById(Long authorId) throws AuthorException {
        return null;
    }
}
