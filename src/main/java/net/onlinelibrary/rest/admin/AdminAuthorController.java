package net.onlinelibrary.rest.admin;

import com.fasterxml.jackson.annotation.JsonView;
import net.onlinelibrary.dto.AuthorDto;
import net.onlinelibrary.dto.BookDto;
import net.onlinelibrary.dto.GenreDto;
import net.onlinelibrary.dto.view.Views;
import net.onlinelibrary.exception.AuthorException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.exception.withResponseStatus.BadRequestException;
import net.onlinelibrary.exception.withResponseStatus.NotFoundException;
import net.onlinelibrary.mapper.AuthorMapper;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.rest.AuthorController;
import net.onlinelibrary.service.AuthorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/admin/authors")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminAuthorController {
    private final AuthorService authorService;

    private final AuthorMapper authorMapper;
    private final AuthorController authorController;

    public AdminAuthorController(
            AuthorMapper authorMapper,
            AuthorService authorService,
            AuthorController authorController) {
        this.authorMapper = authorMapper;
        this.authorService = authorService;
        this.authorController = authorController;
    }

    @GetMapping("{id}")
    @JsonView(Views.ForAdmin.class)
    public AuthorDto getAuthorById(@PathVariable("id") Long authorId) {
        return authorController.getAuthorById(authorId);
    }

    @GetMapping("")
    @JsonView(Views.ForAdmin.class)
    public Stream<AuthorDto> getAuthorsInRange(@RequestParam Integer offset, @RequestParam Integer count) {
        return authorController.getAuthorsInRange(offset, count);
    }

    @GetMapping("{id}/books")
    @JsonView(Views.ForAdmin.class)
    public Stream<BookDto> getBooksOfAuthor(@PathVariable("id") Long authorId) {
        return authorController.getBooksOfAuthor(authorId);
    }

    @GetMapping("{id}/genres")
    @JsonView(Views.ForAdmin.class)
    public Stream<GenreDto> getGenresOfAuthor(@PathVariable("id") Long authorId) {
        return authorController.getGenresOfAuthor(authorId);
    }

    @GetMapping("count")
    @JsonView(Views.ForAdmin.class)
    public Map<Object, Object> getTotalAuthorsCount() {
        return authorController.getTotalAuthorsCount();
    }

    @PostMapping("")
    @JsonView(Views.ForAdmin.class)
    public AuthorDto saveNewAuthor(@RequestBody AuthorDto dto) {
        Author authorByDto = authorMapper.toEntity(dto);
        try {
            Author savedAuthor = authorService.saveNewAuthor(authorByDto);
            return authorMapper.toDto(savedAuthor);
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @PutMapping("{id}")
    @JsonView(Views.ForAdmin.class)
    public AuthorDto fullUpdateAuthor(@PathVariable("id") Long authorId, @RequestBody AuthorDto dto) {
        Author authorByDto = authorMapper.toEntity(dto);
        try {
            Author updatedAuthor = authorService.updateAuthor(authorId, authorByDto);
            return authorMapper.toDto(updatedAuthor);
        } catch (AuthorException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @PatchMapping("{id}")
    @JsonView(Views.ForAdmin.class)
    public AuthorDto partUpdateAuthor(@PathVariable("id") Long authorId, @RequestBody AuthorDto dto) {
        Author author;
        try {
            author = authorService.getById(authorId);
        } catch (AuthorException e) {
            throw new NotFoundException(e.getMessage());
        }
        Author authorByDto = authorMapper.toEntity(dto);

        if (authorByDto.getFirstName() != null)
            author.setFirstName(authorByDto.getFirstName());
        if (authorByDto.getMiddleName() != null)
            author.setMiddleName(authorByDto.getMiddleName());
        if (authorByDto.getLastName() != null)
            author.setLastName(authorByDto.getLastName());
        if (authorByDto.getBooks() != null)
            author.setBooks(authorByDto.getBooks());
        if (authorByDto.getGenres() != null)
            author.setGenres(authorByDto.getGenres());

        try {
            Author updatedAuthor = authorService.updateAuthor(author.getId(), author);
            return authorMapper.toDto(updatedAuthor);
        } catch (AuthorException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @JsonView(Views.ForAdmin.class)
    public void deleteAuthor(@PathVariable("id") Long authorId) {
        try {
            authorService.deleteById(authorId);
        } catch (AuthorException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
