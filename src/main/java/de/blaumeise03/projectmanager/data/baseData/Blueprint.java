package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "db_blueprints")
@POJO(mappingClass = BlueprintPOJO.class)
public class Blueprint implements Serializable {
    @Id
    @Column(name = "resItemID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "resItemID", unique = true)
    @POJOData
    @ToString.Exclude
    private Item item;

    @Column(name = "resultQuantity")
    @POJOData
    private Integer resultQuantity;

    @Column(name = "stationFees")
    @POJOData
    private Long stationFees;

    @OneToMany(mappedBy = "blueprint", cascade = {CascadeType.ALL})
    @POJOData
    private Set<ItemCost> baseCost;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blueprint blueprint = (Blueprint) o;
        return id.equals(blueprint.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


}
