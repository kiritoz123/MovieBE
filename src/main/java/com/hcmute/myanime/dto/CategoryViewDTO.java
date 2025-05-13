package com.hcmute.myanime.dto;

public class CategoryViewDTO {
    private int categoryId;
    private String categoryName;
    private Long totalView;

    public CategoryViewDTO(int categoryId, String categoryName, Long totalView) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.totalView = totalView;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Long getTotalView() {
        return totalView;
    }

    public void setTotalView(Long totalView) {
        this.totalView = totalView;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
