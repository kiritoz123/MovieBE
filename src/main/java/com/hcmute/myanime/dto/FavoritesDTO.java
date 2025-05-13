package com.hcmute.myanime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class FavoritesDTO {
    private int id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createAt;
    private int movieSeriesId;
    private String name;
    private String image;
    private int totalEpisode;


    public FavoritesDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalEpisode() {
        return totalEpisode;
    }

    public void setTotalEpisode(int totalEpisode) {
        this.totalEpisode = totalEpisode;
    }

    public FavoritesDTO(int id, Timestamp createAt, int movieSeriesId, String name, String image, int totalEpisode) {
        this.id = id;
        this.createAt = createAt;
        this.movieSeriesId = movieSeriesId;
        this.name = name;
        this.image = image;
        this.totalEpisode = totalEpisode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public int getMovieSeriesId() {
        return movieSeriesId;
    }

    public void setMovieSeriesId(int movieSeriesId) {
        this.movieSeriesId = movieSeriesId;
    }


    public FavoritesDTO(int id, Timestamp createAt, int movieSeriesId) {
        this.id = id;
        this.createAt = createAt;
        this.movieSeriesId = movieSeriesId;
    }
}
