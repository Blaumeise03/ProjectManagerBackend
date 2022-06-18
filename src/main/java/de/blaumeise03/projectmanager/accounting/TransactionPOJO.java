package de.blaumeise03.projectmanager.accounting;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;

@POJO(mappingClass = Transaction.class)
public class TransactionPOJO {
    @POJOData
    int tid;
    @POJOData(blocked = true)
    int fromID;
    @POJOData(blocked = true)
    int toID;
    @POJOData
    long price;
    @POJOData
    String purpose;
    @POJOData
    String reference;
    @POJOData
    long time;
    @POJOData
    boolean verified;

    String nameFrom;

    String nameTo;

    public String getNameFrom() {
        return nameFrom;
    }

    public void setNameFrom(String nameFrom) {
        this.nameFrom = nameFrom;
    }

    public String getNameTo() {
        return nameTo;
    }

    public void setNameTo(String nameTo) {
        this.nameTo = nameTo;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getFromID() {
        return fromID;
    }

    /*public int getFrom() {
        return this.getFromID();
    }*/

    public void setFromID(int fromID) {
        this.fromID = fromID;
    }

    public void setFrom(Integer from) {
        this.setFromID(from);
    }

    public void setFrom(long from) {
        this.setFromID((int) from);
    }

    public int getToID() {
        return toID;
    }

    /*public int getTo() {
        return this.getToID();
    }*/

    public void setTo(Integer to) {
        this.setToID(to);
    }

    public void setTo(long to) {
        this.setToID((int) to);
    }

    public void setToID(int toID) {
        this.toID = toID;
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

    static TransactionPOJO convert(Transaction transaction) {
        TransactionPOJO pojo = new TransactionPOJO();
        pojo.tid = transaction.getTid();
        pojo.fromID = transaction.getFrom() == null ? -1 : transaction.getFrom().getUid();
        pojo.toID = transaction.getTo() == null ? -1 : transaction.getTo().getUid();
        pojo.price = transaction.getPrice();
        pojo.purpose = transaction.getPurpose();
        pojo.reference = transaction.getReference();
        pojo.time = transaction.getTime();
        pojo.verified = transaction.isVerified();
        return pojo;
    }
}
