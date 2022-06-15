package de.blaumeise03.projectmanager.accounting;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import de.blaumeise03.projectmanager.utils.POJOExtraMapping;

import javax.persistence.*;

@Entity
@POJO(mappingClass = TransactionPOJO.class)
@Table(name="db_transactions")
public class Transaction {
    @Id
    @POJOData
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="TID", nullable = false)
    private int tid;

    @POJOData(invokeMethod = "getUid")
    @POJOExtraMapping(invokeMethodFrom = "getName", invokeMethodTo = "setNameFrom", nullCheckMethod = "isNew", type = String.class)
    @ManyToOne
    @JoinColumn(name = "IDFrom", foreignKey=@ForeignKey(name = "Fk_userFrom"), columnDefinition = "INTEGER DEFAULT NULL")
    private Player from;

    @POJOData(invokeMethod = "getUid")
    @POJOExtraMapping(invokeMethodFrom = "getName", invokeMethodTo = "setNameTo", nullCheckMethod = "isNew", type = String.class)
    @ManyToOne
    @JoinColumn(name = "IDTo", foreignKey=@ForeignKey(name = "Fk_userTo"), columnDefinition = "INTEGER DEFAULT NULL")
    private Player to;

    @POJOData
    @Column(name="Price", nullable = false)
    private long price;

    @POJOData
    @Column(name="Purpose", columnDefinition = "TEXT")
    private String purpose;

    @POJOData
    @Column(name="Reference", columnDefinition = "TEXT")
    private String reference;

    @POJOData
    @Column(name="time", nullable = false)
    private long time;

    @POJOData
    @Column(name="Verified", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean verified;

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public Player getFrom() {
        return from;
    }

    public void setFrom(Player from) {
        this.from = from;
    }

    public Player getTo() {
        return to;
    }

    public void setTo(Player to) {
        this.to = to;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
