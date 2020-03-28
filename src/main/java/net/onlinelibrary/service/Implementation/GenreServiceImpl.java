package net.onlinelibrary.service.Implementation;

import net.onlinelibrary.exception.GenreException;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.repository.GenreRepository;
import net.onlinelibrary.service.GenreService;
import net.onlinelibrary.util.NumberNormalizer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepo;
    private final NumberNormalizer normalizer;

    public GenreServiceImpl(GenreRepository genreRepo, NumberNormalizer normalizer) {
        this.genreRepo = genreRepo;
        this.normalizer = normalizer;
    }

    @Override
    public List<Genre> getByRange(Integer begin, Integer count) {
        List<Genre> genres = genreRepo.findAll();
        return genres.subList(
                normalizer.normalize(begin, 0, genres.size() - 1),
                normalizer.normalize(begin + count - 1, 0, genres.size() - 1)
        );
    }

    @Override
    public Genre getById(Long genreId) throws GenreException {
        Optional<Genre> genreOpt = genreRepo.findById(genreId);
        if(!genreOpt.isPresent())
            throw new GenreException("Genre with id \'" + genreId + "\' has not found");
        return genreOpt.get();
    }

    @Override
    public List<Book> getBooksOfGenre(Long genreId) throws GenreException {
        Optional<Genre> genreOpt = genreRepo.findById(genreId);
        if(!genreOpt.isPresent())
            throw new GenreException("Genre with id \'" + genreId + "\' has not found");
        return genreOpt.get().getBooks();
    }

    @Override
    public List<Author> getAuthorsOfGenre(Long genreId) throws GenreException {
        Optional<Genre> genreOpt = genreRepo.findById(genreId);
        if(!genreOpt.isPresent())
            throw new GenreException("Genre with id \'" + genreId + "\' has not found");
        return genreOpt.get().getAuthors();
    }

    @Override
    public Genre saveGenre(Genre genre) {
        return genreRepo.save(genre);
    }

    @Override
    public void deleteById(Long genreId) throws GenreException {
        try {
            genreRepo.deleteById(genreId);
        }
        catch (EmptyResultDataAccessException e)
        {
            throw new GenreException("Genre with id \'" + genreId + "\' has not found");
        }
    }
}
