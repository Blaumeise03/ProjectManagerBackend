package de.blaumeise03.projectmanager.data.projects;

import de.blaumeise03.projectmanager.data.baseData.Item;
import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import de.blaumeise03.projectmanager.utils.POJOExtraMapping;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity
@Table(name = "db_projectContent")
@POJO(mappingClass = ProjectContentPOJO.class)
public class ProjectContent {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @POJOData
    private Long id;

    @ManyToOne
    @JoinColumn(name="projectID", foreignKey=@ForeignKey(name = "Fk_projectContent"), nullable = false)
    @POJOData
    private Project project;

    @ManyToOne
    @JoinColumn(name="itemID", foreignKey=@ForeignKey(name = "Fk_projectContentItem"), nullable = false)
    @POJOData(to = "itemID")
    @POJOExtraMapping(to = "item", recursive = true)
    private Item item;

    @Column(name = "def_order", columnDefinition = "int default 0", nullable = false)
    @POJOData
    private Integer order = 0;

    @Column(name = "amount", columnDefinition = "bigint(20) default 0", nullable = false)
    @POJOData
    private Integer amount = 0;

    @Column(name = "build", columnDefinition = "bigint(20) default 0", nullable = false)
    @POJOData
    private Integer build = 0;

    @Column(name = "efficiency", columnDefinition = "double default 0.92", nullable = false)
    @POJOData
    private Double efficiency = 0.92;

    @ManyToOne
    @JoinColumn(name="parentID", foreignKey=@ForeignKey(name = "Fk_projectContentParent"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @POJOData
    private ProjectContent parent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectContent that = (ProjectContent) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
