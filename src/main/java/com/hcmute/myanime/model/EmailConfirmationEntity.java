package com.hcmute.myanime.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "email_confirmation", schema = "hcmutemyanime")
public class EmailConfirmationEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String otpCode;
    private String status;
    private String email;
    private String confirmationType;
    private Timestamp expiredDate;
    private Timestamp createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private UsersEntity usersEntityByUserId;

    public EmailConfirmationEntity(String otpCode, String status, String email, String confirmationType, Timestamp expiredDate, Timestamp createAt, UsersEntity usersEntityByUserId) {
        this.otpCode = otpCode;
        this.status = status;
        this.email = email;
        this.confirmationType = confirmationType;
        this.expiredDate = expiredDate;
        this.createAt = createAt;
        this.usersEntityByUserId = usersEntityByUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConfirmationType() {
        return confirmationType;
    }

    public void setConfirmationType(String confirmationType) {
        this.confirmationType = confirmationType;
    }

    public Timestamp getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Timestamp expiredDate) {
        this.expiredDate = expiredDate;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public UsersEntity getUsersEntityByUserId() {
        return usersEntityByUserId;
    }

    public void setUsersEntityByUserId(UsersEntity usersEntityByUserId) {
        this.usersEntityByUserId = usersEntityByUserId;
    }

    public EmailConfirmationEntity() {
    }
}
