package net.onlinelibrary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import net.onlinelibrary.dto.view.Views;

import java.util.Date;
import java.util.List;

@Data
public class BookDto extends BaseDto {
    @JsonView(Views.ForEvery.class)
    private String name;

    @JsonView(Views.ForEvery.class)
    private Integer pagesCount;

    @JsonView(Views.ForEvery.class)
    private String avatar;

    @JsonView(Views.ForEvery.class)
    private Integer publicationYear;

    @JsonView(Views.ForEvery.class)
    private String shortDescription;

    @JsonView(Views.ForEvery.class)
    private Long rating;

    @JsonView(Views.ForAdmin.class)
    private List<Long> authors;

    @JsonView(Views.ForAdmin.class)
    private List<Long> genres;

    @JsonView(Views.ForAdmin.class)
    private List<Long> comments;



    @JsonView(Views.ForAdmin.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdDate;

    @JsonView(Views.ForAdmin.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date lastModifiedDate;
}
