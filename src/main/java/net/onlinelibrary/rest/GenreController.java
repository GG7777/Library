package net.onlinelibrary.rest;

import com.fasterxml.jackson.annotation.JsonView;
import net.onlinelibrary.dto.AuthorDto;
import net.onlinelibrary.dto.BookDto;
import net.onlinelibrary.dto.GenreDto;
import net.onlinelibrary.dto.view.Views;
import net.onlinelibrary.exception.GenreException;
import net.onlinelibrary.exception.withResponseStatus.NotFoundException;
import net.onlinelibrary.mapper.AuthorMapper;
import net.onlinelibrary.mapper.BookMapper;
import net.onlinelibrary.mapper.GenreMapper;
import net.onlinelibrary.service.GenreService;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.View;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/genres")
public class GenreController {
    private final GenreService genreService;

    private final GenreMapper genreMapper;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;

    public GenreController(
            GenreService genreService,
            GenreMapper genreMapper,
            BookMapper bookMapper,
            AuthorMapper authorMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
        this.bookMapper = bookMapper;
        this.authorMapper = authorMapper;
    }

    @GetMapping("")
    @JsonView(Views.ForEvery.class)
    public Stream<GenreDto> getGenresInRange(@RequestParam Integer offset, @RequestParam Integer count) {
        return genreService
                .getByRange(offset, count)
                .stream()
                .map(genre -> genreMapper.toDto(genre));
    }

    @GetMapping("{id}")
    @JsonView(Views.ForEvery.class)
    public GenreDto getGenreById(@PathVariable("id") Long genreId) {
        try {
            return genreMapper.toDto(genreService.getById(genreId));
        } catch (GenreException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/books")
    @JsonView(Views.ForEvery.class)
    public Stream<BookDto> getBooksOfGenre(@PathVariable("id") Long genreId) {
        try {
            return genreService
                    .getBooksOfGenre(genreId)
                    .stream()
                    .map(book -> bookMapper.toDto(book));
        } catch (GenreException e) {
            throw  new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/authors")
    @JsonView(Views.ForEvery.class)
    public Stream<AuthorDto> getAuthorsOfGenre(@PathVariable("id") Long genreId) {
        try {
            return genreService
                    .getAuthorsOfGenre(genreId)
                    .stream()
                    .map(author -> authorMapper.toDto(author));
        } catch (GenreException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("count")
    @JsonView(Views.ForEvery.class)
    public Map<Object, Object> getTotalGenresCount() {
        Long count = genreService.getGenresCount();

        HashMap<Object, Object> map = new HashMap<>();

        map.put("count", count);

        return map;
    }

    @GetMapping("search")
    @JsonView(Views.ForEvery.class)
    public Stream<GenreDto> search(@RequestParam String startsWith) {
        return genreService
                .searchBySubStr(startsWith)
                .stream()
                .map(genre -> genreMapper.toDto(genre));
    }
}
