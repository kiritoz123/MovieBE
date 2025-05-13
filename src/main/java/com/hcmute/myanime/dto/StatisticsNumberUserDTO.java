package com.hcmute.myanime.dto;

public class StatisticsNumberUserDTO {
    private int premiumUserNumber;
    private int normalUserNumber;
    private Long allUserNumber;

    public StatisticsNumberUserDTO(int premiumUserNumber, int normalUserNumber, Long allUserNumber) {
        this.premiumUserNumber = premiumUserNumber;
        this.normalUserNumber = normalUserNumber;
        this.allUserNumber = allUserNumber;
    }

    public int getPremiumUserNumber() {
        return premiumUserNumber;
    }

    public void setPremiumUserNumber(int premiumUserNumber) {
        this.premiumUserNumber = premiumUserNumber;
    }

    public int getNormalUserNumber() {
        return normalUserNumber;
    }

    public void setNormalUserNumber(int normalUserNumber) {
        this.normalUserNumber = normalUserNumber;
    }

    public Long getAllUserNumber() {
        return allUserNumber;
    }

    public void setAllUserNumber(Long allUserNumber) {
        this.allUserNumber = allUserNumber;
    }
}
