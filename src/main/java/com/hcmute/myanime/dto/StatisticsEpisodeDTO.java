package com.hcmute.myanime.dto;

import java.sql.Timestamp;

public class StatisticsEpisodeDTO extends EpisodeDTO{

    private long statisticsView;
    private int SeriesId;


    public StatisticsEpisodeDTO(int id, Timestamp createAt, String resource, String title, long statisticsView, int seriesId) {
        super(id, createAt, resource, title);
        this.statisticsView = statisticsView;
        SeriesId = seriesId;
    }


    public long getStatisticsView() {
        return statisticsView;
    }

    public void setStatisticsView(long statisticsView) {
        this.statisticsView = statisticsView;
    }

    public int getSeriesId() {
        return SeriesId;
    }

    public void setSeriesId(int seriesId) {
        SeriesId = seriesId;
    }

}
