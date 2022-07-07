package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@POJO(mappingClass = Price.class)
public class PricePOJO {
    @POJOData
    private Long item;

    @POJOData
    private String priceType;

    @POJOData
    private Double value;
}
