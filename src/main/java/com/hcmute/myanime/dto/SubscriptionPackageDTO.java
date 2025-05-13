package com.hcmute.myanime.dto;

public class SubscriptionPackageDTO {
    private int id;
    private long day;
    private String description;
    private boolean enable;
    private String name;
    private double price;
    private int numberOfTopUp;

    public SubscriptionPackageDTO() {
    }

    public SubscriptionPackageDTO(int id, long day, String description, boolean enable, String name, double price) {
        this.id = id;
        this.day = day;
        this.description = description;
        this.enable = enable;
        this.name = name;
        this.price = price;
    }

    public SubscriptionPackageDTO(int id, long day, String description, boolean enable, String name, double price, int numberOfTopUp) {
        this.id = id;
        this.day = day;
        this.description = description;
        this.enable = enable;
        this.name = name;
        this.price = price;
        this.numberOfTopUp = numberOfTopUp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isEnable() {
        return enable;
    }

    public int getNumberOfTopUp() {
        return numberOfTopUp;
    }

    public void setNumberOfTopUp(int numberOfTopUp) {
        this.numberOfTopUp = numberOfTopUp;
    }
}
