package de.blaumeise03.projectmanager.userManagement;

import de.blaumeise03.projectmanager.accounting.Player;
import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "auth_users")
@POJO(mappingClass = UserPOJO.class)
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
    private boolean enabled;
    @POJOData
    private boolean tokenExpired;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isTokenExpired() {
        return tokenExpired;
    }

    public void setTokenExpired(boolean tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return enabled == user.enabled && tokenExpired == user.tokenExpired && id.equals(user.id) && username.equals(user.username) && Objects.equals(email, user.email) && password.equals(user.password) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, enabled, tokenExpired, roles);
    }
}
