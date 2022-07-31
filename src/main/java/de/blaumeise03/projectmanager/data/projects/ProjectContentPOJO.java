package de.blaumeise03.projectmanager.data.projects;

import de.blaumeise03.projectmanager.data.baseData.ItemPOJO;
import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Data;

@Data
@POJO(mappingClass = ProjectContent.class)
public class ProjectContentPOJO {

    @POJOData
    private Long id;

    @POJOData
    private Long project;

    @POJOData(to = "item")
    private Long itemID;

    @POJOData(blocked = true)
    private ItemPOJO item;

    @POJOData
    private Integer order;

    @POJOData
    private Integer amount;

    @POJOData
    private Integer build;

    @POJOData
    private Double efficiency;

    @POJOData
    private Long parent;
}
