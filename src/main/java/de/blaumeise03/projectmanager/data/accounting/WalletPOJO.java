package de.blaumeise03.projectmanager.data.accounting;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class WalletPOJO {
    Integer id;
    String name;
    Long verified;
    Long unverified;

    public WalletPOJO(Integer id, String name, BigDecimal verified, BigDecimal unverified) {
        this.id = id;
        this.name = name;
        this.verified = verified == null ? 0 : verified.longValue();
        this.unverified = unverified == null ? 0 : unverified.longValue();
    }
}
