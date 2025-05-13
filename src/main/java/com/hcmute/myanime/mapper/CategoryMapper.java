package com.hcmute.myanime.mapper;

import com.hcmute.myanime.dto.CategoryDTO;
import com.hcmute.myanime.dto.MovieDTO;
import com.hcmute.myanime.model.CategoryEntity;
import com.hcmute.myanime.model.MovieEntity;

public class CategoryMapper {
    public static CategoryDTO toDTO (CategoryEntity categoryEntity)
    {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryEntity.getId());
        categoryDTO.setName(categoryEntity.getName());
        categoryDTO.setCreateAt(categoryEntity.getCreateAt());

        return categoryDTO;
    }
}
