package com.hcmute.myanime.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "order_premium", schema = "hcmutemyanime")
public class OrderPremiumEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private String method;
    private String description;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private UsersEntity usersEntityById;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcription_package_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private SubscriptionPackageEntity subscriptionPackageById;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bill_id", referencedColumnName = "id")
    private PaypalOrderEntity paypalOrderEntity;

    @CreationTimestamp
    private Timestamp createAt;

    public OrderPremiumEntity() {
    }

    public PaypalOrderEntity getPaypalOrderEntity() {
        return paypalOrderEntity;
    }

    public void setPaypalOrderEntity(PaypalOrderEntity paypalOrderEntity) {
        this.paypalOrderEntity = paypalOrderEntity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UsersEntity getUsersEntityById() {
        return usersEntityById;
    }

    public void setUsersEntityById(UsersEntity usersEntityById) {
        this.usersEntityById = usersEntityById;
    }

    public SubscriptionPackageEntity getSubscriptionPackageById() {
        return subscriptionPackageById;
    }

    public void setSubscriptionPackageById(SubscriptionPackageEntity subscriptionPackageById) {
        this.subscriptionPackageById = subscriptionPackageById;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
