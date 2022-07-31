package de.blaumeise03.projectmanager.data.projects;

import de.blaumeise03.projectmanager.data.baseData.ItemService;
import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import de.blaumeise03.projectmanager.utils.POJOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public ProjectPOJO getProjectByID(Long id) throws POJOMappingException {
        Project project = projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Project with id " + id + " not found!"));
        ProjectPOJO res = (ProjectPOJO) POJOMapper.map(project);
        for (ProjectContentPOJO c : res.getContent()) {
            ItemService.mapFullItem(
                    c.getItem(),
                    project
                            .getContent()
                            .stream()
                            .filter(c2 -> c2.getItem().getItemID().equals(c.getItemID()))
                            .findFirst().orElseThrow(IllegalStateException::new)
                            .getItem()
            );
        }
        return res;
    }
}
