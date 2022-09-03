package de.blaumeise03.projectmanager.data.projects.contract;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@POJO(mappingClass = InvestmentContract.class)
public class InvestmentContractPOJO {

    @POJOData
    private Long id;

    @POJOData(blocked = true, to = "player")
    private Integer playerID;

    @POJOData
    private Long created;

    @POJOData(blocked = true)
    private List<ContractItemPOJO> items;
}
