package com.hcmute.myanime.service;

import com.hcmute.myanime.dto.CategoryDTO;
import com.hcmute.myanime.dto.CategoryViewDTO;
import com.hcmute.myanime.model.CategoryEntity;
import com.hcmute.myanime.repository.CategoryRepository;
import com.hcmute.myanime.repository.ViewStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ViewStatisticService {
    @Autowired
    private ViewStatisticsRepository viewStatisticsRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public Long countViewStatisticsByYearAndMonth(int yearCreateAt, int montCreateAt) {
        return viewStatisticsRepository.countByYearCreateAtAndMonthCreateAt_FunctionSQL(yearCreateAt, montCreateAt);
    }

    public Long countViewStatisticsByYear(int yearCreateAt) {
        return viewStatisticsRepository.countByYearCreateAt_FunctionSQL(yearCreateAt);
    }

    public Long countViewStatisticsByCategory(int categoryId) {
        return viewStatisticsRepository.countByCategoryId_FunctionSQL(categoryId);
    }

    public List<CategoryViewDTO> getAllCategoriesViewStatistics() {
        List<CategoryViewDTO> categoryViewDTOList = new ArrayList<>();
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
        categoryEntityList.forEach((categoryEntity -> {
            categoryViewDTOList.add(
                    new CategoryViewDTO(
                            categoryEntity.getId(),
                            categoryEntity.getName(),
                            countViewStatisticsByCategory(categoryEntity.getId())
                    )
            );
        }));
        return categoryViewDTOList;
    }
}
