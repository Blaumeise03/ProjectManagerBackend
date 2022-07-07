package de.blaumeise03.projectmanager.data.projects;

import de.blaumeise03.projectmanager.data.baseData.Corp;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "db_projects")
public class Project {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="corpID", foreignKey=@ForeignKey(name = "Fk_projectCorp"), nullable = false)
    private Corp corp;

    @Column(name = "name")
    private String name;

    @Column(name = "created")
    private Long created;

    @OneToMany(mappedBy = "project")
    private List<ProjectContent> content;

    @OneToMany(mappedBy = "project")
    private List<ProjectRevenue> revenue;
}
