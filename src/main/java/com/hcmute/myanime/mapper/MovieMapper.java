package com.hcmute.myanime.mapper;

import com.hcmute.myanime.dto.CategoryDTO;
import com.hcmute.myanime.dto.MovieDTO;
import com.hcmute.myanime.model.CategoryEntity;
import com.hcmute.myanime.model.MovieEntity;

import java.util.ArrayList;
import java.util.List;

public class MovieMapper {
    public static MovieDTO toDTO (MovieEntity movieEntity)
    {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movieEntity.getId());
        movieDTO.setTitle(movieEntity.getTitle());
        movieDTO.setStudioName(movieEntity.getStudioName());
        movieDTO.setCreateAt(movieEntity.getCreateAt());

        List<CategoryEntity> categoryEntityList = movieEntity.getCategoryEntityCollection().stream().toList();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        categoryEntityList.forEach((categoryEntity) -> {
            CategoryDTO categoryDTO = CategoryMapper.toDTO(categoryEntity);
            categoryDTOList.add(categoryDTO);
        });

        movieDTO.setCategoryData(categoryDTOList);
        return movieDTO;
    }
}
