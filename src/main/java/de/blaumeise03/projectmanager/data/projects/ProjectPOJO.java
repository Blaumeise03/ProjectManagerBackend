package de.blaumeise03.projectmanager.data.projects;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Data;

import java.util.List;

@Data
@POJO(mappingClass = Project.class)
public class ProjectPOJO {
    @POJOData
    private Long id;

    @POJOData
    private Integer corp;

    @POJOData
    private String name;

    @POJOData
    private Long created;

    @POJOData(blocked = true) //Manual mapping required
    private List<ProjectContentPOJO> content;

    //@POJOData(blocked = true) //Manual mapping required
    private List<ProjectRevenue> revenue;
}
