package com.hcmute.myanime.model;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Table(name = "paypal_order", schema = "hcmutemyanime")
public class PaypalOrderEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Column(nullable = false)
    private double price;

    @Column(columnDefinition = "varchar(255) default 'USD'")
    private String currency;

    @Column(columnDefinition = "varchar(255) default 'paypal'")
    private String method;

    @Column(columnDefinition = "varchar(255) default 'sale'")
    private String intent;

    private String description;

    @Column(nullable = false)
    private String token;

    private String paymentId;
    private String payerId;

    @Column(columnDefinition = "varchar(255) default 'pending'")
    private String status;

    @OneToOne(mappedBy = "paypalOrderEntity")
    private OrderPremiumEntity orderPremiumEntity;

    public PaypalOrderEntity() {
    }

    public OrderPremiumEntity getOrderPremiumEntity() {
        return orderPremiumEntity;
    }

    public void setOrderPremiumEntity(OrderPremiumEntity orderPremiumEntity) {
        this.orderPremiumEntity = orderPremiumEntity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
