package net.onlinelibrary.dto;

import lombok.Data;

import java.util.List;

@Data
public class GenreDto extends BaseDto {
    private String genre;
    private List<Long> books;
    private List<Long> authors;
}
