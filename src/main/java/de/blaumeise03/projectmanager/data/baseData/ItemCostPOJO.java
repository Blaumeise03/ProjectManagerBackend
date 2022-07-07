package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Data;

@Data
@POJO(mappingClass = ItemCost.class)
public class ItemCostPOJO {

    @POJOData
    private Long item;

    private String itemName;

    @POJOData
    private Long quantity;
}
