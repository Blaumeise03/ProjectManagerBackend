package de.blaumeise03.projectmanager.userManagement;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Data;

import java.util.Objects;

@POJO(mappingClass = User.class)
@Data
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
    private Boolean enabled;
    @POJOData
    private Boolean tokenExpired;

    public boolean isEnabled() {
        return enabled;
    }
}
