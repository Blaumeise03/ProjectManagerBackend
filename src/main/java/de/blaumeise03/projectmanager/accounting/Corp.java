package de.blaumeise03.projectmanager.accounting;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="db_corps")
public class Corp {
    @Column(name="CID")
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int cid;

    @Column(name = "Tag", length = 10)
    private String tag;

    @Column(name = "Name", length = 20)
    private String name;

    @OneToMany(mappedBy = "corp")
    private Set<Player> players;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
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
