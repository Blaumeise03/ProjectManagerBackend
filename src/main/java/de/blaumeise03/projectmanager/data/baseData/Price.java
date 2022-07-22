package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@IdClass(Price.PriceID.class)
@Table(name = "db_prices")
@POJO(mappingClass = PricePOJO.class)
public class Price {

    @Id
    @ManyToOne
    @JoinColumn(name="ItemID", foreignKey=@ForeignKey(name = "Fk_item"), nullable = false)
    @POJOData
    @ToString.Exclude
    private Item item;

    @Id
    @Column(name="PriceType")
    @Enumerated(EnumType.STRING)
    @POJOData
    private PriceType priceType;

    @Column(name="Value")
    @POJOData
    private Double value;

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

    public enum PriceType {
        MARKET_LOWEST_SELL, MARKET_SELL, MARKET_BUY, VCB_SELL, VCB_BUY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return item.equals(price.item) && priceType == price.priceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, priceType);
    }

    @Data
    @NoArgsConstructor
    public static class PriceID implements Serializable {

        private Long item;

        private Price.PriceType priceType;

        public PriceID(Long item, Price.PriceType priceType) {
            this.item = item;
            this.priceType = priceType;
        }
    }
}
