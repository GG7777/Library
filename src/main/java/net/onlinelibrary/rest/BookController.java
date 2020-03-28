package net.onlinelibrary.rest;

import net.onlinelibrary.dto.AuthorDto;
import net.onlinelibrary.dto.BookDto;
import net.onlinelibrary.dto.CommentDto;
import net.onlinelibrary.dto.GenreDto;
import net.onlinelibrary.exception.BookException;
import net.onlinelibrary.exception.NotFoundException;
import net.onlinelibrary.mapper.AuthorMapper;
import net.onlinelibrary.mapper.BookMapper;
import net.onlinelibrary.mapper.CommentMapper;
import net.onlinelibrary.mapper.GenreMapper;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.service.BookService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;
    private final GenreMapper genreMapper;
    private final CommentMapper commentMapper;

    public BookController(BookService bookService, BookMapper bookMapper, AuthorMapper authorMapper, GenreMapper genreMapper, CommentMapper commentMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
        this.authorMapper = authorMapper;
        this.genreMapper = genreMapper;
        this.commentMapper = commentMapper;
    }

    @GetMapping("")
    public Stream<BookDto> getBooksInRange(@RequestParam Integer begin, @RequestParam Integer count) {
        return bookService
                .getByRange(begin, count)
                .stream()
                .map(book -> bookMapper.toDto(book));
    }

    @GetMapping("{id}")
    public BookDto getBookById(@PathVariable("id") Long bookId) {
        try {
            return bookMapper.toDto(bookService.getById(bookId));
        } catch (BookException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/authors")
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



    @PostMapping("")
    public BookDto saveBook(@RequestBody BookDto dto) {
        Book book = bookMapper.toEntity(dto);

        book.setId(null);
        book.setCreatedDate(new Date());
        book.setLastModifiedDate(new Date());

        book.setRating(0);

        return bookMapper.toDto(bookService.saveBook(book));
    }

    @PutMapping("{id}")
    public BookDto fullUpdateBook(@PathVariable("id") Long bookId, @RequestBody BookDto dto) {
        try {
            Book book = bookService.getById(bookId);
            Book bookByDto = bookMapper.toEntity(dto);

            bookByDto.setId(book.getId());
            bookByDto.setCreatedDate(book.getCreatedDate());
            bookByDto.setLastModifiedDate(new Date());

            BeanUtils.copyProperties(bookByDto, book);

            return bookMapper.toDto(bookService.saveBook(book));
        } catch (BookException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @PatchMapping("{id}")
    public BookDto partUpdateBook(@PathVariable("id") Long bookId, @RequestBody BookDto dto) {
        try {
            Book book = bookService.getById(bookId);
            Book bookByDto = bookMapper.toEntity(dto);

            if (bookByDto.getAuthors() != null)
                book.setAuthors(bookByDto.getAuthors());
            if (bookByDto.getGenres() != null)
                book.setGenres(bookByDto.getGenres());
            if (bookByDto.getComments() != null)
                book.setComments(bookByDto.getComments());
            if (bookByDto.getAvatar() != null)
                book.setAvatar(bookByDto.getAvatar());
            if (bookByDto.getName() != null)
                book.setName(bookByDto.getName());
            if (bookByDto.getPagesCount() != null)
                book.setPagesCount(bookByDto.getPagesCount());
            if (bookByDto.getPublicationYear() != null)
                book.setPublicationYear(bookByDto.getPublicationYear());
            if (bookByDto.getRating() != null)
                book.setRating(bookByDto.getRating());
            if (bookByDto.getShortDescription() != null)
                book.setShortDescription(bookByDto.getShortDescription());

            book.setLastModifiedDate(new Date());

            return bookMapper.toDto(bookService.saveBook(book));
        } catch (BookException e) {
            throw new NotFoundException(e.getMessage());
        }
    }



    @DeleteMapping("{id}")
    public BookDto deleteBook(@PathVariable("id") Long bookId) {
        try {
            return bookMapper.toDto(bookService.deleteById(bookId));
        } catch (BookException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}