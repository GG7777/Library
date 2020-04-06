package net.onlinelibrary.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "genres")
@Data
public class Genre extends BaseEntity {
    @Column(name = "genre", unique = true)
    private String genre;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "genres", fetch = FetchType.LAZY)
    private List<Book> books;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "genres", fetch = FetchType.LAZY)
    private List<Author> authors;

    public static Genre getEmpty() {
        Genre genre = new Genre();
        genre.setGenre("");
        genre.setBooks(new ArrayList<>());
        genre.setAuthors(new ArrayList<>());
        return genre;
    }
}