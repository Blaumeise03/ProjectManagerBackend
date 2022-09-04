package de.blaumeise03.projectmanager.userManagement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class AuthController {
    @GetMapping("/checkAuth")
    public boolean checkAuth() {
        return true;
    }
}
