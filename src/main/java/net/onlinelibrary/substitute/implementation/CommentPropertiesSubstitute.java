package net.onlinelibrary.substitute.implementation;

import net.onlinelibrary.model.Comment;
import net.onlinelibrary.substitute.PropertiesSubstitute;
import org.springframework.stereotype.Component;

@Component
public class CommentPropertiesSubstitute implements PropertiesSubstitute<Comment> {
    @Override
    public Comment substitute(Comment comment) {
        comment.setRating(0l);

        return comment;
    }
}
