package com.hcmute.myanime.dto;

public class TotalSeriesDTO {
    private Long totalSeries;

    public TotalSeriesDTO(Long totalSeries) {
        this.totalSeries = totalSeries;
    }

    public Long getTotalSeries() {
        return totalSeries;
    }

    public void setTotalSeries(Long totalSeries) {
        this.totalSeries = totalSeries;
    }
}
