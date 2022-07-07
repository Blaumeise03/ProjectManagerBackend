package de.blaumeise03.projectmanager.data.projects;

import de.blaumeise03.projectmanager.data.baseData.Player;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "db_investmentContract")
public class InvestmentContract {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "playerID", nullable = false)
    private Player player;

    @OneToMany(mappedBy = "investmentContract")
    private Set<ContractItem> items;
}
