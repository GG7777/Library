package net.onlinelibrary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import net.onlinelibrary.dto.view.Views;

import java.util.Date;
import java.util.List;

@Data
public class AuthorDto extends BaseDto {
    @JsonView(Views.ForEvery.class)
    private String lastName;

    @JsonView(Views.ForEvery.class)
    private String firstName;

    @JsonView(Views.ForEvery.class)
    private String middleName;

    @JsonView(Views.ForAdmin.class)
    private List<Long> books;

    @JsonView(Views.ForAdmin.class)
    private List<Long> genres;



    @JsonView(Views.ForAdmin.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdDate;

    @JsonView(Views.ForAdmin.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date lastModifiedDate;
}
