package net.onlinelibrary.mapper;

import net.onlinelibrary.dto.BookDto;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.repository.AuthorRepository;
import net.onlinelibrary.repository.CommentRepository;
import net.onlinelibrary.repository.GenreRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper implements Mapper<Book, BookDto> {

    private final AuthorRepository authorRepo;
    private final CommentRepository commentRepo;
    private final GenreRepository genreRepo;

    public BookMapper(AuthorRepository authorRepo, CommentRepository commentRepo, GenreRepository genreRepo) {
        this.authorRepo = authorRepo;
        this.commentRepo = commentRepo;
        this.genreRepo = genreRepo;
    }

    @Override
    public Book toEntity(BookDto bookDto) {
        Book book = new Book();

        book.setId(bookDto.getId());
        book.setCreatedDate(bookDto.getCreatedDate());
        book.setLastModifiedDate(bookDto.getLastModifiedDate());

        book.setAvatar(bookDto.getAvatar());
        book.setName(bookDto.getName());
        book.setPagesCount(bookDto.getPagesCount());
        book.setPublicationYear(bookDto.getPublicationYear());
        book.setRating(bookDto.getRating());
        book.setShortDescription(bookDto.getShortDescription());

        book.setAuthors(bookDto.getAuthors() == null
                ? null
                : authorRepo.findAllById(bookDto.getAuthors()));

        book.setGenres(bookDto.getGenres() == null
                ? null
                : genreRepo.findAllById(bookDto.getGenres()));

        book.setComments(bookDto.getComments() == null
                ? null
                : commentRepo.findAllById(bookDto.getComments()));

        return book;
    }

    @Override
    public BookDto toDto(Book book) {
        BookDto dto = new BookDto();

        dto.setId(book.getId());
        dto.setCreatedDate(book.getCreatedDate());
        dto.setLastModifiedDate(book.getLastModifiedDate());

        dto.setAvatar(book.getAvatar());
        dto.setName(book.getName());
        dto.setPagesCount(book.getPagesCount());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setRating(book.getRating());
        dto.setShortDescription(book.getShortDescription());

        List<Author> authors = book.getAuthors();
        List<Genre> genres = book.getGenres();
        List<Comment> comments = book.getComments();
        if (authors != null)
            dto.setAuthors(authors.stream().map(author -> author.getId()).collect(Collectors.toList()));
        if (genres != null)
            dto.setGenres(genres.stream().map(genre -> genre.getId()).collect(Collectors.toList()));
        if (comments != null)
            dto.setComments(comments.stream().map(comment -> comment.getId()).collect(Collectors.toList()));

        return dto;
    }
}
