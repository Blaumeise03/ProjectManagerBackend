package de.blaumeise03.projectmanager.data.projects;

import de.blaumeise03.projectmanager.data.baseData.Item;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "db_contractItems")
public class ContractItem {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "itemID", nullable = false)
    private Item item;

    @Column(name = "quantity", columnDefinition = "BigInt(20) default 0", nullable = false)
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "contractID", nullable = false)
    private InvestmentContract investmentContract;

}
