package net.onlinelibrary.rest;

import net.onlinelibrary.dto.AuthorDto;
import net.onlinelibrary.dto.BookDto;
import net.onlinelibrary.dto.GenreDto;
import net.onlinelibrary.exception.GenreException;
import net.onlinelibrary.exception.NotFoundException;
import net.onlinelibrary.mapper.AuthorMapper;
import net.onlinelibrary.mapper.BookMapper;
import net.onlinelibrary.mapper.GenreMapper;
import net.onlinelibrary.model.Genre;
import net.onlinelibrary.service.GenreService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/genres")
public class GenreController {
    private final GenreService genreService;

    private final GenreMapper genreMapper;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;

    public GenreController(GenreService genreService, GenreMapper genreMapper, BookMapper bookMapper, AuthorMapper authorMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
        this.bookMapper = bookMapper;
        this.authorMapper = authorMapper;
    }

    @GetMapping("")
    public Stream<GenreDto> getGenresInRange(@RequestParam Integer begin, @RequestParam Integer count) {
        return genreService
                .getByRange(begin, count)
                .stream()
                .map(genre -> genreMapper.toDto(genre));
    }

    @GetMapping("{id}")
    public GenreDto getGenreById(@PathVariable("id") Long genreId) {
        try {
            return genreMapper.toDto(genreService.getById(genreId));
        } catch (GenreException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping("{id}/books")
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



    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("")
    public GenreDto saveGenre(@RequestBody GenreDto dto) {
        Genre genre = genreMapper.toEntity(dto);

        genre.setId(null);
        genre.setCreatedDate(new Date());
        genre.setLastModifiedDate(new Date());

        return genreMapper.toDto(genreService.saveGenre(genre));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public GenreDto fullUpdateGenre(@PathVariable("id") Long genreId, @RequestBody GenreDto dto) {
        try {
            Genre genre = genreService.getById(genreId);

            dto.setId(genre.getId());
            dto.setCreatedDate(genre.getCreatedDate());
            dto.setLastModifiedDate(new Date());

            Genre genreByDto = genreMapper.toEntity(dto);
            BeanUtils.copyProperties(genreByDto, genre);

            return genreMapper.toDto(genreService.saveGenre(genre));
        } catch (GenreException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("{id}")
    public GenreDto partUpdateGenre(@PathVariable("id") Long genreId, @RequestBody GenreDto dto) {
        try {
            Genre genre = genreService.getById(genreId);
            Genre genreByDto = genreMapper.toEntity(dto);

            if (genreByDto.getAuthors() != null)
                genre.setAuthors(genreByDto.getAuthors());
            if (genreByDto.getBooks() != null)
                genre.setBooks(genreByDto.getBooks());
            if (genreByDto.getGenre() != null)
                genre.setGenre(genreByDto.getGenre());

            genre.setLastModifiedDate(new Date());

            return genreMapper.toDto(genreService.saveGenre(genre));
        } catch (GenreException e) {
            throw new NotFoundException(e.getMessage());
        }
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public void deleteGenre(@PathVariable("id") Long genreId) {
        try {
            genreService.deleteById(genreId);
        } catch (GenreException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
