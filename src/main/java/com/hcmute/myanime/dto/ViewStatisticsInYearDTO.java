package com.hcmute.myanime.dto;

public class ViewStatisticsInYearDTO {
    private Long totalView;
    private int year;

    public Long getTotalView() {
        return totalView;
    }

    public void setTotalView(Long totalView) {
        this.totalView = totalView;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ViewStatisticsInYearDTO(Long totalView, int year) {
        this.totalView = totalView;
        this.year = year;
    }
}
