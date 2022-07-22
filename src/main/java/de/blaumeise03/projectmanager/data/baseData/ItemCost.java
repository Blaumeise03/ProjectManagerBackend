package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import de.blaumeise03.projectmanager.utils.POJOExtraMapping;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@Table(name = "db_bp_item_cost")
@IdClass(ItemCost.ItemCostId.class)
@POJO(mappingClass = ItemCostPOJO.class)
public class ItemCost {
    @Id
    @ManyToOne
    @JoinColumn(name = "blueprintID", foreignKey = @ForeignKey(name = "Fk_itemCost_blueprint"))
    @ToString.Exclude
    private Blueprint blueprint;

    @Id
    @ManyToOne
    @JoinColumn(name = "itemID", foreignKey = @ForeignKey(name = "Fk_itemCost_item"))
    @POJOData
    @POJOExtraMapping(to = "itemName")
    private Item item;

    @Column(name = "quantity", nullable = false)
    @POJOData
    private Long quantity;

    @Data
    public static class ItemCostId implements Serializable {
        private Blueprint blueprint;

        private Item item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCost itemCost = (ItemCost) o;
        return blueprint.equals(itemCost.blueprint) && item.equals(itemCost.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blueprint, item);
    }
}
