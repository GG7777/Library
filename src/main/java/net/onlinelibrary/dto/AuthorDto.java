package net.onlinelibrary.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthorDto extends BaseDto {
    private String lastName;
    private String firstName;
    private String middleName;
    private List<Long> books;
    private List<Long> genres;
}
