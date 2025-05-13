package com.hcmute.myanime.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "subscription_packages", schema = "hcmutemyanime")
public class SubscriptionPackageEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private Double price;
    private Long day;
    private String description;
    private Boolean enable;

    @OneToMany(mappedBy = "subscriptionPackageBySubscriptionPackageId", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Collection<UserPremiumEntity> userPremiums;

    @OneToMany(mappedBy = "subscriptionPackageById", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Collection<GiftCodeEntity> giftCodeEntityCollection;

    @OneToMany(mappedBy = "subscriptionPackageById", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Collection<OrderPremiumEntity> orderPremiumEntityCollection;

    public SubscriptionPackageEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
