package com.hcmute.myanime.dto;

public class ViewStatisticsInMonthDTO {
    private int id;
    private String month;
    private int year;
    private Long totalView;

    public ViewStatisticsInMonthDTO(int id, String month , int year, Long totalView) {
        this.id = id;
        this.month = month;
        this.year = year;
        this.totalView = totalView;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getTotalView() {
        return totalView;
    }

    public void setTotalView(Long totalView) {
        this.totalView = totalView;
    }
}
