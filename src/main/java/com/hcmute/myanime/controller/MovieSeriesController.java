package com.hcmute.myanime.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hcmute.myanime.dto.*;
import com.hcmute.myanime.exception.BadRequestException;
import com.hcmute.myanime.mapper.MovieSeriesMapper;
import com.hcmute.myanime.mapper.SeriesDetailMapper;
import com.hcmute.myanime.model.MovieSeriesEntity;
import com.hcmute.myanime.service.CategoryService;
import com.hcmute.myanime.service.CommentService;
import com.hcmute.myanime.service.MovieSeriesService;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping()
public class MovieSeriesController {
    @Autowired
    private MovieSeriesService movieSeriesService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CategoryService categoryService;


    //region Module Admin
    @GetMapping("/admin/movie-series")
    public ResponseEntity<?> findAll(@RequestParam Map<String, String> requestParams) {
        String page = requestParams.get("page");
        String limit = requestParams.get("limit");
        String keywordSearch = requestParams.get("keyword");
        List<MovieSeriesEntity> movieSeriesEntityList = movieSeriesService.findAll(page, limit, keywordSearch);
        List<MovieSeriesDTO> movieSeriesDTOList = new ArrayList<>();
        movieSeriesEntityList.forEach(movieSeriesEntity -> {
            Long viewOfSeries = movieSeriesService.totalViewByMovieSeriesEntity(movieSeriesEntity);
            movieSeriesDTOList.add(MovieSeriesMapper.toDTO(movieSeriesEntity, viewOfSeries));
        });
        return ResponseEntity.ok(movieSeriesDTOList);
    }

    @PostMapping("/admin/movie-series")
    public ResponseEntity<?> storage(
            @RequestParam String model,
            @RequestParam(value = "sourceFile", required = false) MultipartFile sourceFile
    ) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            MovieSeriesDTO movieSeriesDTO = mapper.readValue(model, MovieSeriesDTO.class);

            MovieSeriesEntity movieSeriesEntity = movieSeriesService.save(movieSeriesDTO, sourceFile);
            Long viewOfSeries = movieSeriesService.totalViewByMovieSeriesEntity(movieSeriesEntity);
            MovieSeriesDTO movieSeriesResponseDTO = MovieSeriesMapper.toDTO(movieSeriesEntity, viewOfSeries);
            return ResponseEntity.ok(
                    new ResponseDTO(
                            HttpStatus.OK,
                            "Create series success",
                            movieSeriesResponseDTO
                    )
            );
        }
        catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(HttpStatus.BAD_REQUEST, "Error: " + ex.getMessage()));
        }
    }

    @PutMapping("/admin/movie-series/{seriesID}")
    public ResponseEntity<?> updateSeriesById(
            @RequestParam String model,
            @RequestParam(value = "sourceFile", required = false) MultipartFile sourceFile,
            @PathVariable int seriesID) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            MovieSeriesDTO movieSeriesDTO = mapper.readValue(model, MovieSeriesDTO.class);

            MovieSeriesEntity movieSeriesEntity = movieSeriesService.updateById(seriesID, movieSeriesDTO, sourceFile);
            Long viewOfSeries = movieSeriesService.totalViewByMovieSeriesEntity(movieSeriesEntity);
            MovieSeriesDTO movieSeriesResponseDTO = MovieSeriesMapper.toDTO(movieSeriesEntity, viewOfSeries);
            return ResponseEntity.ok(
                    new ResponseDTO(HttpStatus.OK,
                            "Update series success",
                            movieSeriesResponseDTO)
            );
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(HttpStatus.BAD_REQUEST, "Error: " + ex.getMessage()));
        }
    }

    @DeleteMapping("/admin/movie-series/{seriesID}")
    public ResponseEntity<?> deleteSeriesById(@PathVariable int seriesID) {
        if (movieSeriesService.deleteById(seriesID)) {
            return ResponseEntity.ok(
                    new ResponseDTO(HttpStatus.OK, "Delete series success")
            );
        } else {
            throw new BadRequestException("Delete series fail");
        }
    }
    //endregion

    //region Mudule Client
    @GetMapping("/movie-and-series/count")
    public ResponseEntity<?> countSeries(@RequestParam Map<String, String> requestParams) {
        String keywordSearch = requestParams.get("keyword");
        return ResponseEntity.ok(new TotalSeriesDTO(movieSeriesService.countSeriesByKeyword(keywordSearch)));
    }

    //Movie va series co phan trang
    @GetMapping("/movie-and-series")
    public ResponseEntity<?> movieAndSeriesFindAll(@RequestParam Map<String, String> requestParams) {
        String page = requestParams.get("page");
        String limit = requestParams.get("limit");
        String keywordSearch = requestParams.get("keyword");
        List<MovieSeriesEntity> movieSeriesEntityList = movieSeriesService.getByPageAndLimit(page, limit, keywordSearch);
        List<SeriesDetailDTO> seriesDetailDTOList = new ArrayList<>();
        movieSeriesEntityList.forEach(movieSeriesEntity -> {
            Long seriesTotalView = movieSeriesService.totalViewByMovieSeriesEntity(movieSeriesEntity);
            Long seriesTotalComment = commentService.totalCommentByMovieSeriesEntity(movieSeriesEntity);
            seriesDetailDTOList.add(SeriesDetailMapper.toDTO(movieSeriesEntity, seriesTotalView, seriesTotalComment));
        });
        return ResponseEntity.ok(seriesDetailDTOList);
    }

    //Movie va series by category co phan trang
    @GetMapping("/movie-and-series/category/{categoryId}")
    public ResponseEntity<?> movieAndSeriesFindByCategory(@PathVariable int categoryId, @RequestParam Map<String, String> requestParams) {
        String page = requestParams.get("page");
        String limit = requestParams.get("limit");
        List<MovieSeriesEntity> movieSeriesEntityList = categoryService.findMovieSeriesEntitiesByCategory(categoryId, page, limit);
        List<SeriesDetailDTO> seriesDetailDTOList = new ArrayList<>();
        movieSeriesEntityList.forEach(movieSeriesEntity -> {
            Long seriesTotalView = movieSeriesService.totalViewByMovieSeriesEntity(movieSeriesEntity);
            Long seriesTotalComment = commentService.totalCommentByMovieSeriesEntity(movieSeriesEntity);
            seriesDetailDTOList.add(SeriesDetailMapper.toDTO(movieSeriesEntity, seriesTotalView, seriesTotalComment));
        });
        return ResponseEntity.ok(seriesDetailDTOList);
    }

    //so luong category id
    @GetMapping("/movie-and-series/category/{categoryId}/count")
    public ResponseEntity<?> countSeriesByCategoryId(@PathVariable int categoryId) {
        return ResponseEntity.ok(new TotalSeriesDTO(categoryService.countSeriesByCategoryId(categoryId)));
    }

    //Movie va series lay id
    @GetMapping("/movie-and-series/{seriesId}")
    public ResponseEntity<?> movieAndSeriesBySeriesId(@PathVariable int seriesId) {
        MovieSeriesEntity movieSeriesEntity = movieSeriesService.findById(seriesId);
        Long seriesTotalView = movieSeriesService.totalViewByMovieSeriesEntity(movieSeriesEntity);
        Long seriesTotalComment = commentService.totalCommentByMovieSeriesEntity(movieSeriesEntity);
        SeriesDetailDTO seriesDetailDTO = SeriesDetailMapper.toDTO(movieSeriesEntity, seriesTotalView, seriesTotalComment);
        return ResponseEntity.ok(seriesDetailDTO);
    }

    //Movie va series lay cac series cùng 1 movie id
    @GetMapping("/movie-and-series/get-all-series/{seriesId}")
    public ResponseEntity<?> getAllMovieAndSeriesBySeriesId(@PathVariable int seriesId) {
        List<MovieSeriesEntity> movieSeriesEntityList = movieSeriesService.findAllMovieAndSeriesById(seriesId);
        List<SeriesDetailDTO> seriesDetailDTOList = new ArrayList<>();
        movieSeriesEntityList.forEach(movieSeriesEntity -> {
            Long seriesTotalView = movieSeriesService.totalViewByMovieSeriesEntity(movieSeriesEntity);
            Long seriesTotalComment = commentService.totalCommentByMovieSeriesEntity(movieSeriesEntity);
            seriesDetailDTOList.add(SeriesDetailMapper.toDTO(movieSeriesEntity, seriesTotalView, seriesTotalComment));
        });
        return ResponseEntity.ok(seriesDetailDTOList);
    }

    @GetMapping("/movie-and-series/get-recently-added-series")
    public ResponseEntity<?> getRecentlyAddedMovieAndSeries(@RequestParam Map<String, String> requestParams) {
        String page = requestParams.get("page");
        String limit = requestParams.get("limit");
        List<MovieSeriesEntity> movieSeriesEntityList = movieSeriesService.getRecentlyAddedShow(page, limit, 7);
        List<SeriesDetailDTO> seriesDetailDTOList = new ArrayList<>();
        movieSeriesEntityList.forEach(movieSeriesEntity -> {
            Long seriesTotalView = movieSeriesService.totalViewByMovieSeriesEntity(movieSeriesEntity);
            Long seriesTotalComment = commentService.totalCommentByMovieSeriesEntity(movieSeriesEntity);
            seriesDetailDTOList.add(SeriesDetailMapper.toDTO(movieSeriesEntity, seriesTotalView, seriesTotalComment));
        });
        return ResponseEntity.ok(seriesDetailDTOList);
    }
    //endregion
}
