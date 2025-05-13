package com.hcmute.myanime.dto;

public class TotalUsersCountDTO {
    private Long totalUser;

    public TotalUsersCountDTO(Long totalUser) {
        this.totalUser = totalUser;
    }

    public Long getTotalUser() {
        return totalUser;
    }

    public void setTotalUser(Long totalUser) {
        this.totalUser = totalUser;
    }
}
