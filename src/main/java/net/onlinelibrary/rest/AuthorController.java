package net.onlinelibrary.rest;

import net.onlinelibrary.dto.AuthorDto;
import net.onlinelibrary.dto.BookDto;
import net.onlinelibrary.dto.GenreDto;
import net.onlinelibrary.exception.AuthorException;
import net.onlinelibrary.exception.withResponseStatus.NotFoundException;
import net.onlinelibrary.mapper.AuthorMapper;
import net.onlinelibrary.mapper.BookMapper;
import net.onlinelibrary.mapper.GenreMapper;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.service.AuthorService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/authors")
public class AuthorController {
    private final AuthorService authorService;

    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;
    private final GenreMapper genreMapper;

    public AuthorController(
            AuthorService authorService,
            AuthorMapper authorMapper,
            BookMapper bookMapper,
            GenreMapper genreMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
        this.bookMapper = bookMapper;
        this.genreMapper = genreMapper;
    }

    @GetMapping("")
    public Stream<AuthorDto> getAuthorsInRange(@RequestParam Integer begin, @RequestParam Integer count) {
        return authorService
                .getByRange(begin, count)
                .stream()
                .map(author -> authorMapper.toDto(author));
    }

    @GetMapping("{id}")
    public AuthorDto getAuthorById(@PathVariable("id") Long authorId) {
        try {
            return authorMapper.toDto(authorService.getById(authorId));
        } catch (AuthorException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/books")
    public Stream<BookDto> getBooksOfAuthor(@PathVariable("id") Long authorId) {
        try {
            return authorService
                    .getBooksOfAuthor(authorId)
                    .stream()
                    .map(book -> bookMapper.toDto(book));
        } catch (AuthorException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/genres")
    public Stream<GenreDto> getGenresOfAuthor(@PathVariable("id") Long authorId) {
        try {
            return authorService
                    .getGenresOfAuthor(authorId)
                    .stream()
                    .map(genre -> genreMapper.toDto(genre));
        } catch (AuthorException e) {
            throw new NotFoundException(e.getMessage());
        }
    }



    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("")
    public AuthorDto saveAuthor(@RequestBody AuthorDto dto) {
        Author author = authorMapper.toEntity(dto);

        author.setId(null);
        author.setCreatedDate(new Date());
        author.setLastModifiedDate(new Date());

        return authorMapper.toDto(authorService.saveAuthor(author));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public AuthorDto fullUpdateAuthor(@PathVariable("id") Long authorId, @RequestBody AuthorDto dto) {
        try {
            Author author = authorService.getById(authorId);
            Author authorByDto = authorMapper.toEntity(dto);

            authorByDto.setId(author.getId());
            authorByDto.setCreatedDate(author.getCreatedDate());
            authorByDto.setLastModifiedDate(new Date());

            BeanUtils.copyProperties(authorByDto, author);

            return authorMapper.toDto(authorService.saveAuthor(author));
        } catch (AuthorException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("{id}")
    public AuthorDto partUpdateAuthor(@PathVariable("id") Long authorId, @RequestBody AuthorDto dto) {
        try {
            Author author = authorService.getById(authorId);
            Author authorByDto = authorMapper.toEntity(dto);

            if (authorByDto.getBooks() != null)
                author.setBooks(authorByDto.getBooks());
            if (authorByDto.getGenres() != null)
                author.setGenres(authorByDto.getGenres());
            if (authorByDto.getFirstName() != null)
                author.setFirstName(authorByDto.getFirstName());
            if (authorByDto.getMiddleName() != null)
                author.setMiddleName(authorByDto.getMiddleName());
            if (authorByDto.getLastName() != null)
                author.setLastName(authorByDto.getLastName());

            author.setLastModifiedDate(new Date());

            return authorMapper.toDto(authorService.saveAuthor(author));
        } catch (AuthorException e) {
            throw new NotFoundException(e.getMessage());
        }
    }



    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public void deleteAuthor(@PathVariable("id") Long authorId) {
        try {
            authorService.deleteById(authorId);
        } catch (AuthorException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
