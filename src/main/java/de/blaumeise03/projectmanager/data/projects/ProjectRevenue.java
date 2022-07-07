package de.blaumeise03.projectmanager.data.projects;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "db_projectRevenue")
public class ProjectRevenue {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="projectID", foreignKey=@ForeignKey(name = "Fk_projectRevenue"), nullable = false)
    private Project project;

    @Column(name = "value", nullable = false)
    private Long value;

    @Column(name = "time")
    private Long time;

    @Column(name = "description")
    private String description;
}
