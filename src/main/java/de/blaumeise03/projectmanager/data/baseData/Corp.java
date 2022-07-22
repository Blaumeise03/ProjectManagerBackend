package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.data.projects.Project;
import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;

import javax.persistence.*;
import java.util.Set;

@POJO(mappingClass = CorpPOJO.class)
@Entity
@Table(name="db_corps")
public class Corp {
    @POJOData
    @Column(name="CID")
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer cid;

    @POJOData
    @Column(name = "Tag", length = 10)
    private String tag;

    @POJOData
    @Column(name = "Name", length = 20)
    private String name;

    @OneToMany(mappedBy = "corp")
    private Set<Player> players;

    @OneToMany(mappedBy = "corp")
    private Set<Project> projects;

    @Transient
    private boolean isNew = true;

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    public boolean isNew() {
        return isNew;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Corp corp = (Corp) o;
        return cid.equals(corp.cid);
    }

    @Override
    public int hashCode() {
        return cid.hashCode();
    }
}
