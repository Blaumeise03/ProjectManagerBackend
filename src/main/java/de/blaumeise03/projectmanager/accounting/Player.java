package de.blaumeise03.projectmanager.accounting;

import de.blaumeise03.projectmanager.userManagement.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="db_players")
@Getter
@Setter
public class Player {
    @Column(name="UID")
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int uid;

    @Column(name = "Name", length = 32)
    private String name;

    @Column(name = "IngameID", length = 32)
    private String ingameID;

    @ManyToOne
    @JoinColumn(name="CID", foreignKey=@ForeignKey(name = "Fk_corp"))
    private Corp corp;

    @OneToMany(mappedBy = "from")
    private Set<Transaction> from;

    @OneToMany(mappedBy = "to")
    private Set<Transaction> to;

    @Column(name = "parent")
    private Integer parent = null;

    @ManyToOne
    @JoinColumn(name="userID", foreignKey=@ForeignKey(name = "Fk_user"))
    private User user;

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
}
