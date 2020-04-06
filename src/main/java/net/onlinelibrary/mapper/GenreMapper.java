package net.onlinelibrary.mapper;

import net.onlinelibrary.dto.GenreDto;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.repository.AuthorRepository;
import net.onlinelibrary.repository.BookRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenreMapper implements Mapper<Genre, GenreDto> {

    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;

    public GenreMapper(BookRepository bookRepo, AuthorRepository authorRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
    }

    @Override
    public Genre toEntity(GenreDto genreDto) {
        Genre genre = new Genre();

        genre.setId(genreDto.getId());
        genre.setCreatedDate(genreDto.getCreatedDate());
        genre.setLastModifiedDate(genreDto.getLastModifiedDate());

        genre.setGenre(genreDto.getGenre());

        genre.setBooks(genreDto.getBooks() == null
                ? null
                : bookRepo.findAllById(genreDto.getBooks()));

        genre.setAuthors(genreDto.getAuthors() == null
                ? null
                : authorRepo.findAllById(genreDto.getAuthors()));

        return genre;
    }

    @Override
    public GenreDto toDto(Genre genre) {
        GenreDto dto = new GenreDto();

        dto.setId(genre.getId());
        dto.setCreatedDate(genre.getCreatedDate());
        dto.setLastModifiedDate(genre.getLastModifiedDate());

        dto.setGenre(genre.getGenre());

        List<Book> books = genre.getBooks();
        List<Author> authors = genre.getAuthors();
        if (books != null)
            dto.setBooks(books.stream().map(book -> book.getId()).collect(Collectors.toList()));
        if (authors != null)
            dto.setAuthors(authors.stream().map(author -> author.getId()).collect(Collectors.toList()));

        return dto;
    }
}
