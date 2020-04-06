package net.onlinelibrary.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
@Data
public class Author extends BaseEntity {
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "authors", fetch = FetchType.LAZY)
    private List<Book> books;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Genre> genres;

    public static Author getEmpty() {
        Author author = new Author();
        author.setFirstName("");
        author.setLastName("");
        author.setMiddleName("");
        author.setGenres(new ArrayList<>());
        author.setBooks(new ArrayList<>());
        return author;
    }
}