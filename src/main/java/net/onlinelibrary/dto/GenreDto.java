package net.onlinelibrary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import net.onlinelibrary.dto.view.Views;

import java.util.Date;
import java.util.List;

@Data
public class GenreDto extends BaseDto {
    @JsonView(Views.ForEvery.class)
    private String genre;

    @JsonView(Views.ForAdmin.class)
    private List<Long> books;

    @JsonView(Views.ForAdmin.class)
    private List<Long> authors;



    @JsonView(Views.ForAdmin.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdDate;

    @JsonView(Views.ForAdmin.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date lastModifiedDate;
}
