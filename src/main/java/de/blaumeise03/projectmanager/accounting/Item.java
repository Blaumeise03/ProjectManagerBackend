package de.blaumeise03.projectmanager.accounting;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "db_items")
public class Item {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ItemID")
    private int itemID;

    @Column(name = "ItemName")
    private String itemName;

    @Column(name = "ItemType")
    private String itemType;

    @OneToMany(mappedBy = "item")
    private Set<Price> prices;

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Set<Price> getPrices() {
        return prices;
    }

    public void setPrices(Set<Price> prices) {
        this.prices = prices;
    }

    @SuppressWarnings("unused")
    public enum ItemType {
        PILOT_SERVICE_SHIPS,
        HIGH_SLOTS,
        MID_SLOTS,
        LOW_SLOTS,
        COMBAT_RIGS,
        ENGINEER_RIGS,
        STRUCTURES_MINERALS,
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
        OTHER_ITEMS;


    }
}
