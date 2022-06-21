package de.blaumeise03.projectmanager.userManagement;

import de.blaumeise03.projectmanager.exceptions.MissingPermissionsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/current")
    public UserSessionInfoPOJO getSessionInfo(Authentication authentication) throws MissingPermissionsException {
        return userService.getFromSession(authentication);
    }
}
