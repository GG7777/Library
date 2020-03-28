package net.onlinelibrary.service.Implementation;

import net.onlinelibrary.exception.AuthorException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.repository.AuthorRepository;
import net.onlinelibrary.service.AuthorService;
import net.onlinelibrary.util.NumberNormalizer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepo;

    public AuthorServiceImpl(AuthorRepository authorRepo) {
        this.authorRepo = authorRepo;
    }

    @Override
    public List<Author> getByRange(Integer begin, Integer count) {
        List<Author> authors = authorRepo.findAll();

        return authors.subList(
                NumberNormalizer.normalize(begin, 0, authors.size() - 1),
                NumberNormalizer.normalize(begin + count - 1, 0, authors.size() - 1));
    }

    @Override
    public Author getById(Long authorId) throws AuthorException {
        Optional<Author> authorOpt = authorRepo.findById(authorId);
        if (!authorOpt.isPresent())
            throw new AuthorException("Author with id \'" + authorId + "\' has not found");
        return authorOpt.get();
    }

    @Override
    public List<Book> getBooksOfAuthor(Long authorId) throws AuthorException {
        Optional<Author> authorOpt = authorRepo.findById(authorId);
        if (!authorOpt.isPresent())
            throw new AuthorException("Author with id \'" + authorId + "\' has not found");
        return authorOpt.get().getBooks();
    }

    @Override
    public List<Genre> getGenresOfAuthor(Long authorId) throws AuthorException {
        Optional<Author> authorOpt = authorRepo.findById(authorId);
        if (!authorOpt.isPresent())
            throw new AuthorException("Author with id \'" + authorId + "\' has not found");
        return authorOpt.get().getGenres();
    }

    @Override
    public Author saveAuthor(Author author) {
        return authorRepo.save(author);
    }

    @Override
    public void deleteById(Long authorId) throws AuthorException {
        try {
            authorRepo.deleteById(authorId);
        } catch (EmptyResultDataAccessException e) {
            throw new AuthorException("Author with id \'" + authorId + "\' has not found");
        }
    }
}
