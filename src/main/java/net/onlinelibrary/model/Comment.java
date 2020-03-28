package net.onlinelibrary.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "comments")
@Data
public class Comment extends BaseEntity {
    @Column(name = "text", length = 500)
    private String text;

    @Column(name = "rating")
    private Long rating;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
