package de.blaumeise03.projectmanager.accounting;

import de.blaumeise03.projectmanager.userManagement.User;
import de.blaumeise03.projectmanager.userManagement.UserService;
import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import de.blaumeise03.projectmanager.utils.POJOExtraMapping;
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

    @Column(name="UID")
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @POJOData
    private Integer uid;

    @Column(name = "Name", length = 32)
    @POJOData
    private String name;

    @Column(name = "IngameID", length = 32)
    @POJOData
    private String ingameID;

    @POJOData
    @ManyToOne
    @JoinColumn(name="CID", foreignKey=@ForeignKey(name = "Fk_corp"))
    private Corp corp;

    @OneToMany(mappedBy = "from")
    private Set<Transaction> from;

    @OneToMany(mappedBy = "to")
    private Set<Transaction> to;

    @Column(name = "parent")
    private Integer parent = null;

    @POJOData
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


    public void loadCorp(Integer id) {
        this.corp = corpRepository.findById(id).orElseThrow(EntityExistsException::new);
    }

    public void loadUser(Long id) {
        this.user = userService.getUserByID(id).orElseThrow(EntityNotFoundException::new);
    }
}
