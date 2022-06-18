package de.blaumeise03.projectmanager.userManagement;

import de.blaumeise03.projectmanager.accounting.Player;
import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "auth_users")
@POJO(mappingClass = UserPOJO.class)
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @POJOData
    private Long id;

    @POJOData
    private String username;
    @POJOData
    private String email;
    @POJOData
    private String password;
    @POJOData
    private Boolean enabled;
    @POJOData
    private Boolean tokenExpired;

    @ManyToMany
    @JoinTable(
            name = "auth_users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @OneToMany(mappedBy = "user")
    private Set<Player> players;

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

    public User() {
    }

    public boolean ownsWallet(int id) {
        for(Player player : players) {
            if(id == player.getUid()) return true;
        }
        return false;
    }

    public boolean hasAdminPerms(int corpID) {
        for(Role role : roles) {
            System.out.println(role.getName());
            if(role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_CORP_ADMIN")) return true;
        }
        return false;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
