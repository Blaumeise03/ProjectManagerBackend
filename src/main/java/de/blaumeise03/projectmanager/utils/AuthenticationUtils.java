package de.blaumeise03.projectmanager.utils;

import de.blaumeise03.projectmanager.exceptions.MissingPermissionsException;
import de.blaumeise03.projectmanager.userManagement.User;
import de.blaumeise03.projectmanager.userManagement.UserService;
import org.springframework.security.core.Authentication;

public class AuthenticationUtils {
    private AuthenticationUtils () {

    }

    public static User getUser(Authentication authentication, UserService userService) throws MissingPermissionsException {
        User user = null;
        if(authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            user = userService.findUserByUsername(springUser.getUsername());
        }
        if(user == null) {
            throw new MissingPermissionsException("User not found!");
        }
        return user;
    }
}
