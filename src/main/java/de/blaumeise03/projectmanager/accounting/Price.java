package de.blaumeise03.projectmanager.accounting;

import javax.persistence.*;

@Entity
@IdClass(PriceID.class)
@Table(name = "db_prices")
public class Price {

    @Id
    @ManyToOne
    @JoinColumn(name="ItemID", foreignKey=@ForeignKey(name = "Fk_item"), nullable = false)
    private Item item;

    @Id
    @Column(name="PriceType")
    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    @Column(name="Value")
    private double value;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public PriceType getPriceType() {
        return priceType;
    }

    public void setPriceType(PriceType priceType) {
        this.priceType = priceType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    enum PriceType {
        MARKET_LOWEST_SELL, MARKET_SELL, MARKET_BUY, VCB_SELL, VCB_BUY;
    }
}
