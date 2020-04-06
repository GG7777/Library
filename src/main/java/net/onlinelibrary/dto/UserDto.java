package net.onlinelibrary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import net.onlinelibrary.dto.view.Views;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class UserDto extends BaseDto {
    @JsonView(Views.ForEvery.class)
    private String username;

    @JsonView(Views.ForSuperAdmin.class)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonView(Views.ForSuperAdmin.class)
    private Boolean active;

    @JsonView(Views.ForSuperAdmin.class)
    private Set<String> roles;

    @JsonView(Views.ForSuperAdmin.class)
    private List<Long> comments;



    @JsonView(Views.ForSuperAdmin.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdDate;

    @JsonView(Views.ForSuperAdmin.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date lastModifiedDate;
}
