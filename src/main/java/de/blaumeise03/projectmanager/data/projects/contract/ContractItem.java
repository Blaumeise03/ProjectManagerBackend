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
@Table(name = "db_contractItems")
@POJO(mappingClass = ContractItemPOJO.class)
public class ContractItem {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @POJOData
    private Long id;

    @ManyToOne
    @JoinColumn(name = "itemID", nullable = false, foreignKey=@ForeignKey(name = "Fk_contractItem_Item"))
    @POJOData(recursive = true)
    private Item item;

    @Column(name = "quantity", columnDefinition = "BigInt(20) default 0", nullable = false)
    @POJOData
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "contractID", nullable = false, foreignKey=@ForeignKey(name = "Fk_contractItem_Contract"))
    @POJOData
    private InvestmentContract investmentContract;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractItem that = (ContractItem) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
