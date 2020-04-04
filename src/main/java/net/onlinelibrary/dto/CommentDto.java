package net.onlinelibrary.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import net.onlinelibrary.dto.view.Views;

@Data
public class CommentDto extends BaseDto {
    @JsonView(Views.ForEvery.class)
    private String text;

    @JsonView(Views.ForEvery.class)
    private Long rating;

    @JsonView(Views.ForUser.class)
    private Long user;

    @JsonView(Views.ForUser.class)
    private Long book;
}
