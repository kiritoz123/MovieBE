package com.hcmute.myanime.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_premiums", schema = "hcmutemyanime")
public class UserPremiumEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private UsersEntity usersEntityById;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_package_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private SubscriptionPackageEntity subscriptionPackageBySubscriptionPackageId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp subscribeDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp expiredAt;

    public UserPremiumEntity() {
    }

    public UserPremiumEntity(int id, UsersEntity usersEntityById, SubscriptionPackageEntity subscriptionPackageBySubscriptionPackageId, Timestamp subscribeDate, Timestamp expiredAt) {
        this.id = id;
        this.usersEntityById = usersEntityById;
        this.subscriptionPackageBySubscriptionPackageId = subscriptionPackageBySubscriptionPackageId;
        this.subscribeDate = subscribeDate;
        this.expiredAt = expiredAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UsersEntity getUsersEntityById() {
        return usersEntityById;
    }

    public void setUsersEntityById(UsersEntity usersEntityById) {
        this.usersEntityById = usersEntityById;
    }

    public SubscriptionPackageEntity getSubscriptionPackageBySubscriptionPackageId() {
        return subscriptionPackageBySubscriptionPackageId;
    }

    public void setSubscriptionPackageBySubscriptionPackageId(SubscriptionPackageEntity subscriptionPackageBySubscriptionPackageId) {
        this.subscriptionPackageBySubscriptionPackageId = subscriptionPackageBySubscriptionPackageId;
    }

    public Timestamp getSubscribeDate() {
        return subscribeDate;
    }

    public void setSubscribeDate(Timestamp subscribeDate) {
        this.subscribeDate = subscribeDate;
    }

    public Timestamp getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Timestamp expiredAt) {
        this.expiredAt = expiredAt;
    }

    @Override
    public String toString() {
        return "UserPremiumEntity{" +
                "id=" + id +
                ", usersEntityById=" + usersEntityById +
                ", subscriptionPackageBySubscriptionPackageId=" + subscriptionPackageBySubscriptionPackageId +
                ", subscribeDate=" + subscribeDate +
                ", expiredAt=" + expiredAt +
                '}';
    }
}
