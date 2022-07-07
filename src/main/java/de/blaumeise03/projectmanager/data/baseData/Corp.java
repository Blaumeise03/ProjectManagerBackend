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

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Player> getUsers() {
        return players;
    }

    public void setUsers(Set<Player> players) {
        this.players = players;
    }
}
