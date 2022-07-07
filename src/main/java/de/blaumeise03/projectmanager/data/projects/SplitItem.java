package de.blaumeise03.projectmanager.data.projects;

import de.blaumeise03.projectmanager.data.baseData.Item;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "db_contract_split_item")
public class SplitItem {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "itemID", nullable = false, foreignKey = @ForeignKey(name = "Fk_projectSplitItem"))
    private Item item;

    @Column(name = "quantity", columnDefinition = "BigInt(20) default 0", nullable = false)
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "contractID", nullable = false, foreignKey = @ForeignKey(name = "Fk_projectContractSplit"))
    private ContractSplit contractSplit;
}
