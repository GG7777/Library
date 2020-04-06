package net.onlinelibrary.rest.admin;

import com.fasterxml.jackson.annotation.JsonView;
import net.onlinelibrary.dto.AuthorDto;
import net.onlinelibrary.dto.BookDto;
import net.onlinelibrary.dto.GenreDto;
import net.onlinelibrary.dto.view.Views;
import net.onlinelibrary.exception.GenreException;
import net.onlinelibrary.exception.ValidationException;
import net.onlinelibrary.exception.withResponseStatus.BadRequestException;
import net.onlinelibrary.exception.withResponseStatus.NotFoundException;
import net.onlinelibrary.mapper.GenreMapper;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.rest.GenreController;
import net.onlinelibrary.service.GenreService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/admin/genres")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminGenreController {
    private final GenreService genreService;

    private final GenreMapper genreMapper;
    private final GenreController genreController;

    public AdminGenreController(
            GenreService genreService,
            GenreMapper genreMapper,
            GenreController genreController) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
        this.genreController = genreController;
    }

    @GetMapping("")
    @JsonView(Views.ForAdmin.class)
    public Stream<GenreDto> getGenresInRange(@RequestParam Integer offset, @RequestParam Integer count) {
        return genreController.getGenresInRange(offset, count);
    }

    @GetMapping("{id}")
    @JsonView(Views.ForAdmin.class)
    public GenreDto getGenreById(@PathVariable("id") Long genreId) {
        return genreController.getGenreById(genreId);
    }

    @GetMapping("{id}/books")
    @JsonView(Views.ForAdmin.class)
    public Stream<BookDto> getBooksOfGenre(@PathVariable("id") Long genreId) {
        return genreController.getBooksOfGenre(genreId);
    }

    @GetMapping("{id}/authors")
    @JsonView(Views.ForAdmin.class)
    public Stream<AuthorDto> getAuthorsOfGenre(@PathVariable("id") Long genreId) {
        return genreController.getAuthorsOfGenre(genreId);
    }

    @GetMapping("count")
    @JsonView(Views.ForAdmin.class)
    public Map<Object, Object> getTotalGenresCount() {
        return genreController.getTotalGenresCount();
    }

    @GetMapping("search")
    @JsonView(Views.ForEvery.class)
    public Stream<GenreDto> search(@RequestParam String startsWith) {
        return genreController.search(startsWith);
    }

    @PostMapping("")
    @JsonView(Views.ForAdmin.class)
    public GenreDto saveGenre(@RequestBody GenreDto dto) {
        Genre genre = genreMapper.toEntity(dto);
        try {
            Genre savedGenre = genreService.saveNewGenre(genre);
            return genreMapper.toDto(savedGenre);
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @PutMapping("{id}")
    @JsonView(Views.ForAdmin.class)
    public GenreDto fullUpdateGenre(@PathVariable("id") Long genreId, @RequestBody GenreDto dto) {
        Genre genre = genreMapper.toEntity(dto);
        try {
            Genre updatedGenre = genreService.updateGenre(genreId, genre);
            return genreMapper.toDto(updatedGenre);
        } catch (GenreException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @PatchMapping("{id}")
    @JsonView(Views.ForAdmin.class)
    public GenreDto partUpdateGenre(@PathVariable("id") Long genreId, @RequestBody GenreDto dto) {
        Genre genre;
        try {
            genre = genreService.getById(genreId);
        } catch (GenreException e) {
            throw new NotFoundException(e.getMessage());
        }
        Genre genreByDto = genreMapper.toEntity(dto);

        if (genreByDto.getAuthors() != null)
            genre.setAuthors(genreByDto.getAuthors());
        if (genreByDto.getBooks() != null)
            genre.setBooks(genreByDto.getBooks());
        if (genreByDto.getGenre() != null)
            genre.setGenre(genreByDto.getGenre());

        try {
            Genre updatedGenre = genreService.updateGenre(genre.getId(), genre);
            return genreMapper.toDto(updatedGenre);
        } catch (GenreException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new BadRequestException("Validation failure - " + e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @JsonView(Views.ForAdmin.class)
    public void deleteGenre(@PathVariable("id") Long genreId) {
        try {
            genreService.deleteById(genreId);
        } catch (GenreException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
