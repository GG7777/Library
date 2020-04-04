package net.onlinelibrary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import net.onlinelibrary.dto.view.Views;

import java.util.Date;

@Data
public class BaseDto {
    @JsonView(Views.ForEvery.class)
    private Long id;

    @JsonView(Views.ForEvery.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdDate;

    @JsonView(Views.ForEvery.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date lastModifiedDate;
}
