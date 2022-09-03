package de.blaumeise03.projectmanager.data.projects.contract;

import de.blaumeise03.projectmanager.data.baseData.ItemPOJO;
import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@POJO(mappingClass = SplitItem.class)
public class SplitItemPOJO {
    @POJOData
    private Long id;

    @POJOData
    private ItemPOJO item;

    @POJOData
    private Long quantity;
}
