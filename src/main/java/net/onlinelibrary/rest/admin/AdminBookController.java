package net.onlinelibrary.rest.admin;

import com.fasterxml.jackson.annotation.JsonView;
import net.onlinelibrary.dto.AuthorDto;
import net.onlinelibrary.dto.BookDto;
import net.onlinelibrary.dto.CommentDto;
import net.onlinelibrary.dto.GenreDto;
import net.onlinelibrary.dto.view.Views;
import net.onlinelibrary.exception.BookException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.exception.withResponseStatus.BadRequestException;
import net.onlinelibrary.exception.withResponseStatus.NotFoundException;
import net.onlinelibrary.mapper.BookMapper;
import net.onlinelibrary.model.Book;
import net.onlinelibrary.rest.BookController;
import net.onlinelibrary.service.BookService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping("api/admin/books")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminBookController {
    private final BookService bookService;

    private final BookMapper bookMapper;
    private final BookController bookController;

    public AdminBookController(
            BookService bookService,
            BookMapper bookMapper,
            BookController bookController) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
        this.bookController = bookController;
    }

    @GetMapping("")
    @JsonView(Views.ForAdmin.class)
    public Stream<BookDto> getBooksInRange(@RequestParam Integer offset, @RequestParam Integer count) {
        return bookController.getBooksInRange(offset, count);
    }

    @GetMapping("{id}")
    @JsonView(Views.ForAdmin.class)
    public BookDto getBookById(@PathVariable("id") Long bookId) {
        return bookController.getBookById(bookId);
    }

    @GetMapping("{id}/authors")
    @JsonView(Views.ForAdmin.class)
    public Stream<AuthorDto> getAuthorsOfBook(@PathVariable("id") Long bookId) {
        return bookController.getAuthorsOfBook(bookId);
    }

    @GetMapping("{id}/genres")
    @JsonView(Views.ForAdmin.class)
    public Stream<GenreDto> getGenresOfBook(@PathVariable("id") Long bookId) {
        return bookController.getGenresOfBook(bookId);
    }

    @GetMapping("{id}/comments")
    @JsonView(Views.ForAdmin.class)
    public Stream<CommentDto> getCommentsOfBook(@PathVariable("id") Long bookId) {
        return bookController.getCommentsOfBook(bookId);
    }

    @PostMapping("")
    @JsonView(Views.ForAdmin.class)
    public BookDto saveBook(@RequestBody BookDto dto) {
        Book bookByDto = bookMapper.toEntity(dto);
        try {
            Book savedBook = bookService.saveNewBook(bookByDto);
            return bookMapper.toDto(savedBook);
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @PutMapping("{id}")
    @JsonView(Views.ForAdmin.class)
    public BookDto fullUpdateBook(@PathVariable("id") Long bookId, @RequestBody BookDto dto) {
        Book bookByDto = bookMapper.toEntity(dto);
        try {
            Book updatedBook = bookService.updateBook(bookId, bookByDto);
            return bookMapper.toDto(updatedBook);
        } catch (BookException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @PatchMapping("{id}")
    @JsonView(Views.ForAdmin.class)
    public BookDto partUpdateBook(@PathVariable("id") Long bookId, @RequestBody BookDto dto) {
        Book book;
        try {
            book = bookService.getById(bookId);
        } catch (BookException e) {
            throw new NotFoundException(e.getMessage());
        }
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

        try {
            Book updatedBook = bookService.updateBook(book.getId(), book);
            return bookMapper.toDto(updatedBook);
        } catch (BookException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @JsonView(Views.ForAdmin.class)
    public void deleteBook(@PathVariable("id") Long bookId) {
        try {
            bookService.deleteById(bookId);
        } catch (BookException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
