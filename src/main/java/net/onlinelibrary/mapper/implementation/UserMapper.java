package net.onlinelibrary.mapper.implementation;

import net.onlinelibrary.dto.UserDto;
import net.onlinelibrary.mapper.Mapper;
import net.onlinelibrary.model.Comment;
import net.onlinelibrary.model.Role;
import net.onlinelibrary.model.User;
import net.onlinelibrary.repository.CommentRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper implements Mapper<User, UserDto> {
    private final CommentRepository commentRepo;

    public UserMapper(CommentRepository commentRepo) {
        this.commentRepo = commentRepo;
    }

    @Override
    public User toEntity(UserDto userDto) {
        User user = new User();

        user.setId(userDto.getId());
        user.setCreatedDate(userDto.getCreatedDate());
        user.setLastModifiedDate(userDto.getLastModifiedDate());

        user.setActive(userDto.getActive());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());

        user.setRoles(userDto.getRoles() == null
                ? null
                : Arrays.stream(Role.values())
                .filter(role ->
                        userDto
                                .getRoles()
                                .stream()
                                .anyMatch(r -> r.equals(role.name())))
                .collect(Collectors.toSet()));

        user.setComments(userDto.getComments() == null ? null : commentRepo.findAllById(userDto.getComments()));

        return user;
    }

    @Override
    public UserDto toDto(User user) {
        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setCreatedDate(user.getCreatedDate());
        dto.setLastModifiedDate(user.getLastModifiedDate());

        dto.setActive(user.getActive());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setUsername(user.getUsername());

        Set<Role> roles = user.getRoles();
        List<Comment> comments = user.getComments();
        if (roles != null)
            dto.setRoles(roles.stream().map(role -> role.name()).collect(Collectors.toSet()));
        if (comments != null)
            dto.setComments(comments.stream().map(comment -> comment.getId()).collect(Collectors.toList()));

        return dto;
    }
}
