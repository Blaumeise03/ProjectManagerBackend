package de.blaumeise03.projectmanager.data.projects;

import de.blaumeise03.projectmanager.data.baseData.ItemService;
import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import de.blaumeise03.projectmanager.utils.POJOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectContentRepository projectContentRepository;

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

    public void save(ProjectPOJO projectPOJO) throws POJOMappingException {
        Project project;
        if (projectPOJO.getId() != null) {
            project = projectRepository.findById(projectPOJO.getId()).orElseThrow(() -> new EntityNotFoundException("Project with id " + projectPOJO.getId() + " not found, can't save it!"));
            POJOMapper.map(projectPOJO, project);
            List<Long> exclude = new ArrayList<>(); //Don't delete these
            Map<ProjectContentPOJO, ProjectContent> contentMap = new HashMap<>();
            for (ProjectContent content : project.getContent()) {
                for (ProjectContentPOJO contentPOJO : projectPOJO.getContent()) {
                    if(contentPOJO.getId().equals(content.getId())) {
                        POJOMapper.map(contentPOJO, content);
                        contentMap.put(contentPOJO, content);
                        exclude.add(content.getId());
                        break;
                    }
                }
            }
            List<ProjectContentPOJO> newContent = new ArrayList<>();
            List<ProjectContent> nContent = new ArrayList<>();
            for (ProjectContentPOJO contentPOJO : projectPOJO.getContent()) {
                boolean found = false;
                for (ProjectContent projectContent : project.getContent()) {
                    if (contentPOJO.getId() >= 0 && projectContent.getId().equals(contentPOJO.getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    newContent.add(contentPOJO);
                    ProjectContent content = (ProjectContent) POJOMapper.map(contentPOJO);
                    contentMap.put(contentPOJO, content);
                    nContent.add(content);
                    content.setId(null);
                }
            }
            for (Map.Entry<ProjectContentPOJO, ProjectContent> entry : contentMap.entrySet()) {
                ProjectContent content = entry.getValue();
                ProjectContentPOJO pojo = entry.getKey();
                if(pojo.getParent() == null) {
                    content.setParent(null);
                } else if (content.getParent() == null) {
                    ProjectContent parent = null;
                    for (Map.Entry<ProjectContentPOJO, ProjectContent> e : contentMap.entrySet()) {
                        if (e.getKey().getId().equals(pojo.getParent())) {
                            parent = e.getValue();
                            break;
                        }
                    }
                    if (parent == null)
                        parent = projectContentRepository.findById(pojo.getParent()).orElseThrow(() -> new EntityNotFoundException("Project Content with id " + pojo.getParent() + " not found, can't set it as parent!"));
                    content.setParent(parent);
                }
            }
            List<Long> deleteChilds = new ArrayList<>();
            List<Long> deleteParents = new ArrayList<>();
            for (ProjectContent content : project.getContent()) {
                if (!exclude.contains(content.getId())) {
                    if(content.getParent() != null) {
                        deleteChilds.add(content.getId());
                    } else {
                        deleteParents.add(content.getId());
                    }
                }
            }
            projectContentRepository.deleteContents(deleteChilds);
            //projectRepository.deleteContents(project.getId(), exclude);
            projectContentRepository.deleteContents(deleteParents);
            Project finalProject = project;
            nContent.forEach(c -> c.setProject(finalProject));
            projectContentRepository.saveAll(nContent);
        } else {
            project = (Project) POJOMapper.map(projectPOJO);
            project = projectRepository.save(project);
            List<ProjectContent> contents = new ArrayList<>(projectPOJO.getContent().size());
            for (ProjectContentPOJO contentPOJO : projectPOJO.getContent()) {
                ProjectContent content = (ProjectContent) POJOMapper.map(contentPOJO);
                content.setProject(project);
                contents.add(content);
            }
            project.setContent(contents);
        }
        for (ProjectContentPOJO contentPOJO : projectPOJO.getContent()) {
            for (ProjectContent content : project.getContent()) {
                if (content.getId().equals(contentPOJO.getId())) {
                    if (contentPOJO.getParent() != null) {
                        for (ProjectContent p : project.getContent()) {
                            if (p.getId().equals(contentPOJO.getParent())) {
                                content.setParent(p);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        projectRepository.save(project);
    }
}
