package de.blaumeise03.projectmanager.data.projects;

import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping("/{id}")
    public ProjectPOJO getProjectByID(Authentication authentication, @PathVariable String id) throws POJOMappingException {
        return projectService.getProjectByID(Long.valueOf(id));
    }

    @PostMapping
    public void saveProject(Authentication authentication, @RequestBody ProjectPOJO projectPOJO) throws POJOMappingException {
        projectService.save(projectPOJO);
    }
}
