package com.hcmute.myanime.controller;

import com.hcmute.myanime.common.Common;
import com.hcmute.myanime.dto.CategoryDTO;
import com.hcmute.myanime.dto.MovieDTO;
import com.hcmute.myanime.dto.ResponseDTO;
import com.hcmute.myanime.exception.BadRequestException;
import com.hcmute.myanime.model.CategoryEntity;
import com.hcmute.myanime.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class CategoryMovieController {
    @Autowired
    private CategoryService categoryService;

    //region Module Admin
    @PostMapping("/admin/category-movie/{movieId}")
    public ResponseEntity<?> addNewCategoryMovie(@RequestBody List<Integer> categoryId,
                                                 @PathVariable int movieId)
    {
        if(categoryService.saveCategoryMovie(movieId, categoryId))
        {
            return ResponseEntity.ok(
                    new ResponseDTO(
                            HttpStatus.OK, "add categories success")
            );
        }
        else
        {
            throw new BadRequestException("add categories fail");
        }
    }
    //endregion

    //region Module Client
    @GetMapping("/category-movie/{movieId}")
    public ResponseEntity<?> getCategoryMovie(@PathVariable int movieId)
    {
        List<CategoryEntity> categoryByMovieId = categoryService.findCategoryByMovieId(movieId);
        return ResponseEntity.ok(categoryByMovieId);
    }
    //endregion

}
