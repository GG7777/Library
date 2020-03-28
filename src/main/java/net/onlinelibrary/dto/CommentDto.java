package net.onlinelibrary.dto;

import lombok.Data;

@Data
public class CommentDto extends BaseDto {
    private String text;
    private Long rating;
    private Long user;
    private Long book;
}
