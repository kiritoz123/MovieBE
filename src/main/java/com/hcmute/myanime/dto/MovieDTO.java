package com.hcmute.myanime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.List;

public class MovieDTO {
    private int id;
    private String title;

    private String studioName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createAt;

    private List<CategoryDTO> categoryData;

    public MovieDTO() {
    }

    public MovieDTO(int id, String title, String studioName, Timestamp createAt) {
        this.id = id;
        this.title = title;
        this.studioName = studioName;
        this.createAt = createAt;
    }

    public List<CategoryDTO> getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(List<CategoryDTO> categoryData) {
        this.categoryData = categoryData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getStudioName() {
        return studioName;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
