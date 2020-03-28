package net.onlinelibrary.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserDto extends BaseDto {
    private String username;
    private String email;
    private String password;
    private boolean active;
    private Set<String> roles;
    private List<Long> comments;
}
