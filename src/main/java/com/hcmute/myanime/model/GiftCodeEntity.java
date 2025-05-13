package com.hcmute.myanime.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "gift_codes", schema = "hcmutemyanime")
public class GiftCodeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcription_package_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private SubscriptionPackageEntity subscriptionPackageById;

    private String redemptionCode;
    @CreationTimestamp
    private Timestamp createAt;

    public GiftCodeEntity() {
    }

    public GiftCodeEntity(int id, SubscriptionPackageEntity subscriptionPackageById, String redemptionCode, Timestamp createAt) {
        this.id = id;
        this.subscriptionPackageById = subscriptionPackageById;
        this.redemptionCode = redemptionCode;
        this.createAt = createAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SubscriptionPackageEntity getSubscriptionPackageById() {
        return subscriptionPackageById;
    }

    public void setSubscriptionPackageById(SubscriptionPackageEntity subscriptionPackageById) {
        this.subscriptionPackageById = subscriptionPackageById;
    }

    public String getRedemptionCode() {
        return redemptionCode;
    }

    public void setRedemptionCode(String redemptionCode) {
        this.redemptionCode = redemptionCode;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
