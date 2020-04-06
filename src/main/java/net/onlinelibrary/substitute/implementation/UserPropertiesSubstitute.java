package net.onlinelibrary.substitute.implementation;

import net.onlinelibrary.model.Role;
import net.onlinelibrary.model.User;
import net.onlinelibrary.substitute.PropertiesSubstitute;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

@Component
public class UserPropertiesSubstitute implements PropertiesSubstitute<User> {
    @Override
    public User substitute(User user) {
        if (user.getComments() == null)
            user.setComments(new ArrayList<>());
        if (user.getActive() == null)
            user.setActive(true);
        if (user.getRoles() == null)
            user.setRoles(Collections.singleton(Role.USER));

        return user;
    }
}
