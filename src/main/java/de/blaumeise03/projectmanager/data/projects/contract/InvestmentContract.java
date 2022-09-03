package de.blaumeise03.projectmanager.data.projects.contract;

import de.blaumeise03.projectmanager.data.baseData.Player;
import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "db_investmentContract")
@POJO(mappingClass = InvestmentContractPOJO.class)
public class InvestmentContract {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @POJOData
    private Long id;

    @ManyToOne
    @JoinColumn(name = "playerID", nullable = false)
    @POJOData(to = "playerID")
    private Player player;

    @Column(name = "created", nullable = false)
    @POJOData
    private Long created;

    @OneToMany(mappedBy = "investmentContract")
    @POJOData(recursive = true)
    private List<ContractItem> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvestmentContract contract = (InvestmentContract) o;
        return id.equals(contract.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
