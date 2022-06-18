package de.blaumeise03.projectmanager.accounting;

import de.blaumeise03.projectmanager.userManagement.User;
import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import de.blaumeise03.projectmanager.utils.POJOExtraMapping;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@SuppressWarnings("unused")
@POJO(mappingClass = Player.class)
@Getter
@Setter
public class PlayerPOJO {

    @POJOData
    private Integer uid;

    @POJOData
    private String name;

    @POJOData
    private String ingameID;

    @POJOData(blocked = true)
    @POJOExtraMapping(invokeMethodFrom = "intValue", invokeMethodTo = "loadCorp", type = Integer.class)
    private Integer corpID;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Set<Transaction> from;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Set<Transaction> to;

    @POJOData
    private Integer parent = null;

    @POJOExtraMapping(invokeMethodFrom = "longValue", invokeMethodTo = "loadUser", nullCheckMethod = "isNew", type = Long.class)
    private Long user;

    public void setUser(long user) {
        this.user = user;
    }

    public void setUser(Long user) {
        this.user = user;
    }
}
