package net.onlinelibrary.rest;

import com.fasterxml.jackson.annotation.JsonView;
import net.onlinelibrary.dto.AuthorDto;
import net.onlinelibrary.dto.BookDto;
import net.onlinelibrary.dto.GenreDto;
import net.onlinelibrary.dto.view.Views;
import net.onlinelibrary.exception.AuthorException;
import net.onlinelibrary.exception.withResponseStatus.NotFoundException;
import net.onlinelibrary.mapper.AuthorMapper;
import net.onlinelibrary.mapper.BookMapper;
import net.onlinelibrary.mapper.GenreMapper;
import net.onlinelibrary.model.Author;
import net.onlinelibrary.service.AuthorService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
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
    @JsonView(Views.ForEvery.class)
    public Stream<AuthorDto> getAuthorsInRange(@RequestParam Integer offset, @RequestParam Integer count) {
        return authorService
                .getByRange(offset, count)
                .stream()
                .map(author -> authorMapper.toDto(author));
    }

    @GetMapping("{id}")
    @JsonView(Views.ForEvery.class)
    public AuthorDto getAuthorById(@PathVariable("id") Long authorId) {
        try {
            Author author = authorService.getById(authorId);
            return authorMapper.toDto(author);
        } catch (AuthorException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/books")
    @JsonView(Views.ForEvery.class)
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
    @JsonView(Views.ForEvery.class)
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

    @GetMapping("count")
    @JsonView(Views.ForEvery.class)
    public Map<Object, Object> getTotalAuthorsCount() {
        Long count = authorService.getAuthorsCount();

        HashMap<Object, Object> map = new HashMap<>();

        map.put("count", count);

        return map;
    }
}
