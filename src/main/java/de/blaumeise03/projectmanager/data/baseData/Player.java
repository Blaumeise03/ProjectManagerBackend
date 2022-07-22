package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.data.accounting.Transaction;
import de.blaumeise03.projectmanager.userManagement.User;
import de.blaumeise03.projectmanager.userManagement.UserService;
import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="db_players")
@Getter
@Setter
@POJO(mappingClass = PlayerPOJO.class)
@Inheritance
public class Player{
    @Autowired
    @Transient
    CorpRepository corpRepository;

    @Autowired
    @Transient
    UserService userService;

    @Column(name="id")
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @POJOData
    private Integer id;

    @Column(name = "Name", length = 32)
    @POJOData
    private String name;

    @Column(name = "IngameID", length = 32)
    @POJOData
    private String ingameID;

    @POJOData
    @ManyToOne
    @JoinColumn(name="corpID", foreignKey=@ForeignKey(name = "Fk_corp"))
    private Corp corp;

    @OneToMany(mappedBy = "from")
    private Set<Transaction> from;

    @OneToMany(mappedBy = "to")
    private Set<Transaction> to;

    @POJOData
    @Column(name = "parent")
    private Integer parent = null;

    @POJOData
    @ManyToOne
    @JoinColumn(name="userID", foreignKey=@ForeignKey(name = "Fk_user"))
    private User user;

    @POJOData
    @Column(name = "rank")
    private String rank;

    @POJOData
    @Column(name = "dummy")
    private Boolean dummy = false;

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


    public void loadCorp(Integer id) {
        this.corp = corpRepository.findById(id).orElseThrow(EntityExistsException::new);
    }

    public void loadUser(Long id) {
        this.user = userService.getUserByID(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
