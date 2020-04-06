package net.onlinelibrary.mapper.implementation;

import net.onlinelibrary.dto.AuthorDto;
import net.onlinelibrary.mapper.Mapper;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.repository.BookRepository;
import net.onlinelibrary.repository.GenreRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorMapper implements Mapper<Author, AuthorDto> {
    private final BookRepository bookRepo;
    private final GenreRepository genreRepo;

    public AuthorMapper(BookRepository bookRepo, GenreRepository genreRepo) {
        this.bookRepo = bookRepo;
        this.genreRepo = genreRepo;
    }

    @Override
    public Author toEntity(AuthorDto authorDto) {
        Author author = new Author();

        author.setId(authorDto.getId());
        author.setCreatedDate(authorDto.getCreatedDate());
        author.setLastModifiedDate(authorDto.getLastModifiedDate());

        author.setFirstName(authorDto.getFirstName());
        author.setMiddleName(authorDto.getMiddleName());
        author.setLastName(authorDto.getLastName());

        author.setBooks(authorDto.getBooks() == null
                ? null
                : bookRepo.findAllById(authorDto.getBooks()));

        author.setGenres(authorDto.getGenres() == null
                ? null
                : genreRepo.findAllById(authorDto.getGenres()));

        return author;
    }

    @Override
    public AuthorDto toDto(Author author) {
        AuthorDto dto = new AuthorDto();

        dto.setId(author.getId());
        dto.setCreatedDate(author.getCreatedDate());
        dto.setLastModifiedDate(author.getLastModifiedDate());

        dto.setFirstName(author.getFirstName());
        dto.setMiddleName(author.getMiddleName());
        dto.setLastName(author.getLastName());

        List<Book> books = author.getBooks();
        List<Genre> genres = author.getGenres();
        if (books != null)
            dto.setBooks(books.stream().map(book -> book.getId()).collect(Collectors.toList()));
        if (genres != null)
            dto.setGenres(genres.stream().map(genre -> genre.getId()).collect(Collectors.toList()));

        return dto;
    }
}
