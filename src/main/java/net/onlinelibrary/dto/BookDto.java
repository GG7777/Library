package net.onlinelibrary.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookDto extends BaseDto {
    private String name;
    private Integer pagesCount;
    private String avatar;
    private Integer publicationYear;
    private String shortDescription;
    private Integer rating;
    private List<Long> authors;
    private List<Long> genres;
    private List<Long> comments;
}
