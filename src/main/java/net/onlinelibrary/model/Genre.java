package net.onlinelibrary.model;

import lombok.Data;

import javax.persistence.*;
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
}