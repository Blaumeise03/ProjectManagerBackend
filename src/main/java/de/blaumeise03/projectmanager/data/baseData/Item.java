package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "db_items")
@POJO(mappingClass = ItemPOJO.class)
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ItemID")
    @POJOData
    private Long itemID;

    @Column(name = "ItemName")
    @POJOData
    private String itemName;

    @Column(name = "ItemType")
    @Enumerated(EnumType.STRING)
    @POJOData
    private ItemType itemType;

    @OneToMany(mappedBy = "item", orphanRemoval = true, cascade = {CascadeType.ALL})
    private Set<Price> prices;

    @OneToOne(mappedBy = "item", orphanRemoval = true, cascade = {CascadeType.ALL})
    private Blueprint blueprint;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return itemID.equals(item.itemID);
    }

    @Override
    public int hashCode() {
        return itemID.hashCode();
    }

    @SuppressWarnings("unused")
    public enum ItemType {
        PILOT_SERVICE,
        SHIPS,
        HIGH_SLOTS,
        MID_SLOTS,
        LOW_SLOTS,
        COMBAT_RIGS,
        ENGINEER_RIGS,
        STRUCTURES,
        MINERALS,
        RAW_ORES,
        PLANETARY_RESSOURCE,
        REPROCESSING_MATERIALS,
        VESSEL_DEBRIS,
        DATA,
        NANOCORE_MATERIALS,
        COMPONENTS,
        SHIP_BLUEPRINTS,
        MODULE_BLUEPRINTS,
        DRONE_BLUEPRINTS,
        RIG_BLUEPRINTS,
        STRUCTURE_BLUEPRINTS,
        COMPONENT_BLUEPRINTS,
        FIGHTER_BLUEPRINTS,
        NANOCORES,
        SHIP_SKINS,
        OTHER_ITEMS
    }
}
