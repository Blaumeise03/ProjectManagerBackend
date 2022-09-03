package de.blaumeise03.projectmanager.data.projects.contract;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@POJO(mappingClass = ContractSplit.class)
public class ContractSplitPOJO {
    @POJOData
    private Long id;

    @POJOData(blocked = true)
    private Long contract;

    @POJOData
    private Set<SplitItemPOJO> splitItems;

    @POJOData
    private Long project;
}
