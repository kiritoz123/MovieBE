package com.hcmute.myanime.service;

import com.hcmute.myanime.common.GlobalVariable;
import com.hcmute.myanime.dto.CategoryDTO;
import com.hcmute.myanime.exception.BadRequestException;
import com.hcmute.myanime.exception.ResourceNotFoundException;
import com.hcmute.myanime.mapper.CategoryMapper;
import com.hcmute.myanime.model.CategoryEntity;
import com.hcmute.myanime.model.MovieEntity;
import com.hcmute.myanime.model.MovieSeriesEntity;
import com.hcmute.myanime.repository.CategoryRepository;
import com.hcmute.myanime.repository.MovieRepository;
import com.hcmute.myanime.repository.MovieSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieSeriesRepository movieSeriesRepository;

    public List<CategoryDTO> findAll() {
//        List<CategoryEntity> categoryEntityList = categoryRepository.findAll(); //JPA
        List<CategoryEntity> categoryEntityList = categoryRepository.findAllByView();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        categoryEntityList.forEach((categoryEntity -> {
            categoryDTOList.add(CategoryMapper.toDTO(categoryEntity));
        }));
        return categoryDTOList;
    }

    private Boolean isNumber(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Boolean isValidPage(String page) {
        return page != null && !page.equals("") && isNumber(page) && Long.parseLong(page) >= 0;
    }

    public List<MovieSeriesEntity> findMovieSeriesEntitiesByCategory(int categoryId, String page, String limit) {
        limit = (limit == null || limit.equals("")
                || !isNumber(limit) || Long.parseLong(limit) < 0) ? GlobalVariable.DEFAULT_LIMIT : limit;

        page = (!isValidPage(page)) ? GlobalVariable.DEFAULT_PAGE : page;
        Pageable pageable = PageRequest.of((Integer.parseInt(page) - 1), Integer.parseInt(limit));

        List<MovieSeriesEntity> movieSeriesEntityList = new ArrayList<>();
//        movieSeriesEntityList = movieSeriesRepository.findByCategoryId(categoryId, pageable); //call with jpa
        movieSeriesEntityList = movieSeriesRepository.findByCategoryIdByStoredProcedures(categoryId, pageable.getPageNumber() + 1, pageable.getPageSize()); //call with stored procedure
        return movieSeriesEntityList;
    }
    public boolean save(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = new CategoryEntity(
                categoryDTO.getName()
        );
        try {
            categoryRepository.save(categoryEntity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean deleteById(int categoryId) {
        try {
            categoryRepository.deleteById(categoryId);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean updateById(int categoryId, CategoryDTO categoryDTO) {
        Optional<CategoryEntity> cateById = categoryRepository.findById(categoryId);
        if(!cateById.isPresent()) {
            return false;
        }
        CategoryEntity updateCategoryEntity = cateById.get();
        updateCategoryEntity.setName(categoryDTO.getName());
        try {
            categoryRepository.save(updateCategoryEntity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<CategoryEntity> findCategoryByMovieId(int id) {
        Optional<MovieEntity> movieEntityOptional = movieRepository.findById(id);
        if(!movieEntityOptional.isPresent()) {
            throw new BadRequestException("movie id not found");
        }
        MovieEntity movieEntity = movieEntityOptional.get();
        List<CategoryEntity> categoryEntityList = movieEntity.getCategoryEntityCollection().stream().toList();

        return categoryEntityList;
    }

    public boolean saveCategoryMovie(int movieId, List<Integer> categoryId) {

        Collection<CategoryEntity> categoryEntityCollection = categoryRepository.findByIds(categoryId);
        Optional<MovieEntity> movieEntityOptional = movieRepository.findById(movieId);
        if(!movieEntityOptional.isPresent()) {
            throw new BadRequestException("movie id not found");
        }
        MovieEntity movieEntity = movieEntityOptional.get();
        movieEntity.setCategoryEntityCollection(categoryEntityCollection);
        try {
            movieRepository.save(movieEntity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public CategoryEntity findById(int categoryId) {
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(categoryId);
        if(!categoryEntityOptional.isPresent()) {
            throw new ResourceNotFoundException("not found category");
        }
        CategoryEntity categoryEntity = categoryEntityOptional.get();
        return categoryEntity;
    }

    public Long countSeriesByCategoryId(int categoryId) {
        Long totalSeries;
        totalSeries = categoryRepository.countSeriesByCategoryId(categoryId);
        return totalSeries;
    }
}
