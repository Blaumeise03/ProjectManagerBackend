package de.blaumeise03.projectmanager.data.projects.contract;

import de.blaumeise03.projectmanager.data.baseData.Item;
import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "db_contract_split_item")
@POJO(mappingClass = SplitItemPOJO.class)
public class SplitItem {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @POJOData
    private Long id;

    @ManyToOne
    @JoinColumn(name = "itemID", nullable = false, foreignKey = @ForeignKey(name = "Fk_projectSplitItem"))
    @POJOData(recursive = true)
    private Item item;

    @Column(name = "quantity", columnDefinition = "BigInt(20) default 0", nullable = false)
    @POJOData
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "contractID", nullable = false, foreignKey = @ForeignKey(name = "Fk_projectContractSplit"))
    private ContractSplit contractSplit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SplitItem splitItem = (SplitItem) o;
        return id.equals(splitItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
