package net.onlinelibrary.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "books")
@Data
public class Book extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "pages_count")
    private Integer pagesCount;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "rating")
    private Long rating;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Author> authors;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Genre> genres;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
}