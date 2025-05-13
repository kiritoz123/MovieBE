package com.hcmute.myanime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class OrderPremiumDTO {
    private int id;
    private String method;
    private String description;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createAt;

    private SubscriptionPackageDTO subcriptionPackageDTO;
    private UserDTO userDTO;

    public OrderPremiumDTO() {
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

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public SubscriptionPackageDTO getSubcriptionPackageDTO() {
        return subcriptionPackageDTO;
    }

    public void setSubcriptionPackageDTO(SubscriptionPackageDTO subcriptionPackageDTO) {
        this.subcriptionPackageDTO = subcriptionPackageDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
