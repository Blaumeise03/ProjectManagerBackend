package de.blaumeise03.projectmanager.data.projects;

import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project/")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping("{id}")
    public ProjectPOJO getProjectByID(Authentication authentication, @PathVariable String id) throws POJOMappingException {
        return projectService.getProjectByID(Long.valueOf(id));
    }
}
