package de.blaumeise03.projectmanager.data.projects.contract;

import de.blaumeise03.projectmanager.data.baseData.ItemPOJO;
import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Getter
@Setter
@POJO(mappingClass = ContractItem.class)
public class ContractItemPOJO {

    @POJOData
    private Long id;

    @POJOData
    private ItemPOJO item;

    @POJOData
    private Long quantity;

    @POJOData(blocked = true)
    private Long investmentContract;

    static class ContractItemComparator implements Comparator<ContractItemPOJO> {
        @Override
        public int compare(ContractItemPOJO o1, ContractItemPOJO o2) {
            return Long.compare(o1.getItem().getOrder(), o2.getItem().getOrder());
        }
    }
}
