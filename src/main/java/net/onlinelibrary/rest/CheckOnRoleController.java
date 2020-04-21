package net.onlinelibrary.rest;

import net.onlinelibrary.model.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/check")
public class CheckOnRoleController {
    @GetMapping("on-user")
    public Map<String, Boolean> checkOnUser() {
        return toMap("isUser", checkOn(Role.USER));
    }

    @GetMapping("on-moderator")
    public Map<String, Boolean> checkOnModerator() {
        return toMap("isModerator", checkOn(Role.MODERATOR));
    }

    @GetMapping("on-admin")
    public Map<String, Boolean> checkOnAdmin() {
        return toMap("isAdmin", checkOn(Role.ADMIN));
    }

    @GetMapping("on-super-admin")
    public Map<String, Boolean> checkOnSuperAdmin() {
        return toMap("isSuperAdmin", checkOn(Role.SUPER_ADMIN));
    }

    private Map<String, Boolean> toMap(String name, Boolean value) {
        Map<String, Boolean> map = new HashMap<>();

        map.put(name, value);

        return map;
    }

    private boolean checkOn(Role role) {
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.name());

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority
                                .getAuthority()
                                .equalsIgnoreCase(grantedAuthority.getAuthority()));
    }
}
