package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import de.blaumeise03.projectmanager.utils.POJOExtraMapping;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Set;

@Data
@NoArgsConstructor
@POJO(mappingClass = Blueprint.class)
public class BlueprintPOJO {
    @POJOData
    @POJOExtraMapping(to = "id")
    private Long item;

    @POJOData
    private Integer resultQuantity;

    @POJOData
    private Long stationFees;

    @POJOData
    private Collection<ItemCostPOJO> baseCost;
}
