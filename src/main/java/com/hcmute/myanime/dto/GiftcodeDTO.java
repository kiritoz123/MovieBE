package com.hcmute.myanime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class GiftcodeDTO {
    private int id;
    private String redemption_code;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createAt;
    private SubscriptionPackageDTO subcriptionPackageDTO;

    public GiftcodeDTO(int id, String redemption_code, Timestamp createAt, SubscriptionPackageDTO subcriptionPackageDTO) {
        this.id = id;
        this.redemption_code = redemption_code;
        this.createAt = createAt;
        this.subcriptionPackageDTO = subcriptionPackageDTO;
    }

    public GiftcodeDTO(int id, String redemption_code, Timestamp createAt) {
        this.id = id;
        this.redemption_code = redemption_code;
        this.createAt = createAt;
    }

    public GiftcodeDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRedemption_code() {
        return redemption_code;
    }

    public void setRedemption_code(String redemption_code) {
        this.redemption_code = redemption_code;
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
}
