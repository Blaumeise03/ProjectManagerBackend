package de.blaumeise03.projectmanager.data.projects;

import de.blaumeise03.projectmanager.data.baseData.Corp;
import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "db_projects")
@POJO(mappingClass = ProjectPOJO.class)
public class Project {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @POJOData
    private Long id;

    @ManyToOne
    @JoinColumn(name="corpID", foreignKey=@ForeignKey(name = "Fk_projectCorp"), nullable = false)
    @POJOData
    private Corp corp;

    @Column(name = "name")
    @POJOData
    private String name;

    @Column(name = "created")
    @POJOData
    private Long created;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @POJOData
    private List<ProjectContent> content;

    @OneToMany(mappedBy = "project")
    //@POJOData
    private List<ProjectRevenue> revenue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id.equals(project.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
