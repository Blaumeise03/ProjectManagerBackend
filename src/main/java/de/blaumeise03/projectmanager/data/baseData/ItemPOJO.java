package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Data;

import java.util.Collection;

@Data
@POJO(mappingClass = Item.class)
public class ItemPOJO {
    @POJOData
    private Long itemID;

    @POJOData
    private String itemName;

    @POJOData
    private String itemType;

    @POJOData
    private Long order;

    private BlueprintPOJO blueprint = null;

    private Collection<PricePOJO> prices = null;
}
