package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.data.accounting.Transaction;
import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@SuppressWarnings("unused")
@POJO(mappingClass = Player.class)
@Getter
@Setter
public class PlayerPOJO {

    @POJOData
    private Integer id;

    @POJOData
    private String name;

    @POJOData
    private String ingameID;

    @POJOData
    private Integer corp;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Set<Transaction> from;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Set<Transaction> to;

    @POJOData
    private Integer parent = null;

    @POJOData
    private Long user;

    @POJOData
    private String rank;

    @POJOData
    private Boolean dummy = false;

    public void setUser(long user) {
        this.user = user;
    }

    public void setUser(Long user) {
        this.user = user;
    }
}
