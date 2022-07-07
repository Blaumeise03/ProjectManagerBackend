package de.blaumeise03.projectmanager.data.projects;

import de.blaumeise03.projectmanager.data.baseData.Item;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "db_projectContent")
public class ProjectContent {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="projectID", foreignKey=@ForeignKey(name = "Fk_projectContent"), nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name="itemID", foreignKey=@ForeignKey(name = "Fk_projectContentItem"), nullable = false)
    private Item item;

    @Column(name = "amount", columnDefinition = "bigint(20) default 0", nullable = false)
    private Integer amount = 1;

    @Column(name = "efficiency", columnDefinition = "double default 0.92", nullable = false)
    private Double efficiency = 0.92;

    @ManyToOne
    @JoinColumn(name="parentID", foreignKey=@ForeignKey(name = "Fk_projectContentParent"))
    private ProjectContent parent;
}
