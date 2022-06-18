package de.blaumeise03.projectmanager.accounting;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import de.blaumeise03.projectmanager.utils.POJOExtraMapping;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@POJO(mappingClass = TransactionPOJO.class)
@Table(name="db_transactions")
@Getter
@Setter
public class Transaction extends BaseEntity{
    @Id
    @POJOData
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="TID", nullable = false)
    private Integer tid;

    @POJOData
    @POJOExtraMapping(to = "nameFrom")
    @ManyToOne
    @JoinColumn(name = "IDFrom", foreignKey=@ForeignKey(name = "Fk_userFrom"), columnDefinition = "INTEGER DEFAULT NULL")
    private Player from;

    @POJOData
    @POJOExtraMapping(to = "nameTo")
    @ManyToOne
    @JoinColumn(name = "IDTo", foreignKey=@ForeignKey(name = "Fk_userTo"), columnDefinition = "INTEGER DEFAULT NULL")
    private Player to;

    @POJOData
    @Column(name="Price", nullable = false)
    private Long price;

    @POJOData
    @Column(name="Purpose", columnDefinition = "TEXT")
    private String purpose;

    @POJOData
    @Column(name="Reference", columnDefinition = "TEXT")
    private String reference;

    @POJOData
    @Column(name="time", nullable = false)
    private Long time;

    @POJOData
    @Column(name="Verified", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean verified;

    public boolean isVerified() {
        return Boolean.TRUE.equals(verified);
    }
}
