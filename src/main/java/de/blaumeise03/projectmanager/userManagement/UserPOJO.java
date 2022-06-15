package de.blaumeise03.projectmanager.userManagement;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;

import java.util.Objects;

@POJO(mappingClass = User.class)
public class UserPOJO {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPOJO pojo = (UserPOJO) o;
        return enabled == pojo.enabled && tokenExpired == pojo.tokenExpired && id.equals(pojo.id) && username.equals(pojo.username) && Objects.equals(email, pojo.email) && password.equals(pojo.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, enabled, tokenExpired);
    }
}
