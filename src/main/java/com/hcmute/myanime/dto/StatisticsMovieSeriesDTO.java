package com.hcmute.myanime.dto;

public class StatisticsMovieSeriesDTO extends MovieSeriesDTO {
    private long statisticsViewTotal;

    public StatisticsMovieSeriesDTO() { }

    public StatisticsMovieSeriesDTO(long statisticsViewTotal) {
        this.statisticsViewTotal = statisticsViewTotal;
    }


    public long getStatisticsViewTotal() {
        return statisticsViewTotal;
    }

    public void setStatisticsViewTotal(long statisticsViewTotal) {
        this.statisticsViewTotal = statisticsViewTotal;
    }
}
