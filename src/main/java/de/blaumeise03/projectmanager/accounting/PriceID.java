package de.blaumeise03.projectmanager.accounting;

import java.io.Serializable;
import java.util.Objects;

public class PriceID implements Serializable {

    private Item item;

    private Price.PriceType priceType;

    public PriceID(Item item, Price.PriceType priceType) {
        this.item = item;
        this.priceType = priceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, priceType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceID priceID = (PriceID) o;
        return item.equals(priceID.item) && priceType == priceID.priceType;
    }
}
