package net.onlinelibrary.rest;

import com.fasterxml.jackson.annotation.JsonView;
import net.onlinelibrary.dto.AuthorDto;
import net.onlinelibrary.dto.BookDto;
import net.onlinelibrary.dto.CommentDto;
import net.onlinelibrary.dto.GenreDto;
import net.onlinelibrary.dto.view.Views;
import net.onlinelibrary.exception.BookException;
import net.onlinelibrary.exception.withResponseStatus.NotFoundException;
import net.onlinelibrary.mapper.AuthorMapper;
import net.onlinelibrary.mapper.BookMapper;
import net.onlinelibrary.mapper.CommentMapper;
import net.onlinelibrary.mapper.GenreMapper;
import net.onlinelibrary.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping("api/books")
public class BookController {
    private final BookService bookService;

    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;
    private final GenreMapper genreMapper;
    private final CommentMapper commentMapper;

    public BookController(
            BookService bookService,
            BookMapper bookMapper,
            AuthorMapper authorMapper,
            GenreMapper genreMapper,
            CommentMapper commentMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
        this.authorMapper = authorMapper;
        this.genreMapper = genreMapper;
        this.commentMapper = commentMapper;
    }

    @GetMapping("")
    @JsonView(Views.ForEvery.class)
    public Stream<BookDto> getBooksInRange(@RequestParam Integer offset, @RequestParam Integer count) {
        return bookService
                .getByRange(offset, count)
                .stream()
                .map(book -> bookMapper.toDto(book));
    }

    @GetMapping("{id}")
    @JsonView(Views.ForEvery.class)
    public BookDto getBookById(@PathVariable("id") Long bookId) {
        try {
            return bookMapper.toDto(bookService.getById(bookId));
        } catch (BookException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/authors")
    @JsonView(Views.ForEvery.class)
    public Stream<AuthorDto> getAuthorsOfBook(@PathVariable("id") Long bookId) {
        try {
            return bookService
                    .getAuthorsOfBook(bookId)
                    .stream()
                    .map(author -> authorMapper.toDto(author));
        } catch (BookException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/genres")
    @JsonView(Views.ForEvery.class)
    public Stream<GenreDto> getGenresOfBook(@PathVariable("id") Long bookId) {
        try {
            return bookService
                    .getGenresOfBook(bookId)
                    .stream()
                    .map(genre -> genreMapper.toDto(genre));
        } catch (BookException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/comments")
    @JsonView(Views.ForEvery.class)
    public Stream<CommentDto> getCommentsOfBook(@PathVariable("id") Long bookId) {
        try {
            return bookService
                    .getCommentsOfBook(bookId)
                    .stream()
                    .map(comment -> commentMapper.toDto(comment));
        } catch (BookException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}