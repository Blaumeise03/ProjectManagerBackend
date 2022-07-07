package de.blaumeise03.projectmanager.data.accounting;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.Getter;
import lombok.Setter;

@POJO(mappingClass = Transaction.class)
@Getter
@Setter
public class TransactionPOJO {
    @POJOData
    Integer tid;
    @POJOData
    Integer from;
    @POJOData
    Integer to;
    @POJOData
    Long price;
    @POJOData
    String purpose;
    @POJOData
    String reference;
    @POJOData
    Long time;
    @POJOData
    Boolean verified;

    String nameFrom;

    String nameTo;

    public boolean isVerified() {
        return Boolean.TRUE.equals(verified);
    }
}
