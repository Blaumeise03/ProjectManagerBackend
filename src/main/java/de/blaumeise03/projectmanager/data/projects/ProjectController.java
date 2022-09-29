package de.blaumeise03.projectmanager.data.projects;

import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping("/{id}")
    public ProjectPOJO getProjectByID(Authentication authentication, @PathVariable String id) throws POJOMappingException {
        return projectService.getProjectByID(Long.valueOf(id));
    }

    @DeleteMapping("/{id}")
    public void deleteProjectByID(Authentication authentication, @PathVariable String id) {
        projectService.deleteProjectByID(Integer.parseInt(id));
    }

    @GetMapping("/all")
    public List<ProjectPOJO> getAllProjects(Authentication authentication,
                                            @RequestParam(defaultValue = "0") String page,
                                            @RequestParam(defaultValue = "50") String length
    ) {
        return projectService.getAllProjects(Integer.parseInt(page), Integer.parseInt(length));
    }

    @PostMapping
    public ProjectPOJO saveProject(Authentication authentication, @RequestBody ProjectPOJO projectPOJO) throws POJOMappingException {
        Project project = projectService.save(projectPOJO);
        return ProjectService.mapProject(project);
    }
}
