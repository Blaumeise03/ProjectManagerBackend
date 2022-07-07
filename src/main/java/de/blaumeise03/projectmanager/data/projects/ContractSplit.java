package de.blaumeise03.projectmanager.data.projects;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "db_contractSplit")
public class ContractSplit {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contractID", nullable = false)
    private InvestmentContract contract;

    @OneToMany(mappedBy = "contractSplit")
    private Set<SplitItem> splitItems;

    @ManyToOne
    @JoinColumn(name = "projectID", nullable = true)
    private Project project;
}
