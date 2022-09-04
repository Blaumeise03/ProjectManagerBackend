package de.blaumeise03.projectmanager.data.projects.contract;

import de.blaumeise03.projectmanager.data.projects.Project;
import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "db_contractSplit")
@POJO(mappingClass = ContractSplitPOJO.class)
public class ContractSplit {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @POJOData
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contractID", nullable = false, foreignKey=@ForeignKey(name = "Fk_splitContract"))
    @POJOData
    private InvestmentContract contract;

    @OneToMany(mappedBy = "contractSplit")
    @POJOData(recursive = true)
    private Set<SplitItem> splitItems;

    @ManyToOne
    @JoinColumn(name = "projectID", foreignKey=@ForeignKey(name = "Fk_splitProject"))
    @POJOData
    private Project project;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractSplit that = (ContractSplit) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
