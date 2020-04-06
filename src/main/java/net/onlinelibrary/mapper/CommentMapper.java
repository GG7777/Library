package net.onlinelibrary.mapper;

import net.onlinelibrary.dto.CommentDto;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.repository.BookRepository;
import net.onlinelibrary.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper implements Mapper<Comment, CommentDto> {

    private final UserRepository userRepo;
    private final BookRepository bookRepo;

    public CommentMapper(UserRepository userRepo, BookRepository bookRepo) {
        this.userRepo = userRepo;
        this.bookRepo = bookRepo;
    }

    @Override
    public Comment toEntity(CommentDto commentDto) {
        Comment comment = new Comment();

        comment.setId(commentDto.getId());
        comment.setCreatedDate(commentDto.getCreatedDate());
        comment.setLastModifiedDate(commentDto.getLastModifiedDate());

        comment.setText(commentDto.getText());
        comment.setRating(commentDto.getRating());

        if (commentDto.getUser() != null)
            comment.setUser(userRepo.getOne(commentDto.getUser()));
        if (commentDto.getBook() != null)
            comment.setBook(bookRepo.getOne(commentDto.getBook()));

        return comment;
    }

    @Override
    public CommentDto toDto(Comment comment) {
        CommentDto dto = new CommentDto();

        dto.setId(comment.getId());
        dto.setCreatedDate(comment.getCreatedDate());
        dto.setLastModifiedDate(comment.getLastModifiedDate());

        dto.setText(comment.getText());
        dto.setRating(comment.getRating());

        dto.setUser(comment.getUser().getId());
        dto.setBook(comment.getBook().getId());

        return dto;
    }
}
