package com.hcmute.myanime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class LogHistoryDTO {
    private int id;
    private Long lastSecond;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createAt;
    private int episode_id;
    private int series_id;
    private String image;
    private String seriesName;
    private int episodeNumber;



    public LogHistoryDTO(int id, Long lastSecond, Timestamp createAt, int episode_id, int series_id, String image, String seriesName, int episodeNumber) {
        this.id = id;
        this.lastSecond = lastSecond;
        this.createAt = createAt;
        this.episode_id = episode_id;
        this.series_id = series_id;
        this.image = image;
        this.seriesName = seriesName;
        this.episodeNumber = episodeNumber;
    }

    public int getSeries_id() {
        return series_id;
    }

    public void setSeries_id(int series_id) {
        this.series_id = series_id;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public LogHistoryDTO() {
    }

    public int getEpisode_id() {
        return episode_id;
    }

    public void setEpisode_id(int episode_id) {
        this.episode_id = episode_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getLastSecond() {
        return lastSecond;
    }

    public void setLastSecond(Long lastSecond) {
        this.lastSecond = lastSecond;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
