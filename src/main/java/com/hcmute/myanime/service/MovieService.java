package com.hcmute.myanime.service;

import com.hcmute.myanime.common.GlobalVariable;
import com.hcmute.myanime.dto.MovieDTO;
import com.hcmute.myanime.exception.BadRequestException;
import com.hcmute.myanime.mapper.MovieMapper;
import com.hcmute.myanime.model.MovieEntity;
import com.hcmute.myanime.model.MovieSeriesEntity;
import com.hcmute.myanime.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

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

    public List<MovieDTO> findAll(String page, String limit, String keywordSearch)
    {
        limit = (limit == null || limit.equals("")
                || !isNumber(limit) || Long.parseLong(limit) < 0) ? GlobalVariable.DEFAULT_LIMIT : limit;

        page = (!isValidPage(page)) ? GlobalVariable.DEFAULT_PAGE : page;
        Pageable pageable = PageRequest.of((Integer.parseInt(page) - 1), Integer.parseInt(limit));

        List<MovieEntity> movieEntities = new ArrayList<>();
        List<MovieDTO> movieDTO = new ArrayList<>();
        if(keywordSearch != null) {
            //        List<MovieEntity> movieEntities = movieRepository.findAll(Sort.by("createAt").descending());
            movieEntities = movieRepository.findByTitleContaining(keywordSearch, pageable);
            movieEntities.forEach((movieEntity) -> {
                MovieDTO movieDTO1 = MovieMapper.toDTO(movieEntity);
                movieDTO.add(movieDTO1);
            });
        } else {
            //        List<MovieEntity> movieEntities = movieRepository.findAll(Sort.by("createAt").descending());
            movieEntities = movieRepository.findAllByStoredProcedures(Integer.parseInt(page), Integer.parseInt(limit));
            movieEntities.forEach((movieEntity) -> {
                MovieDTO movieDTO1 = MovieMapper.toDTO(movieEntity);
                movieDTO.add(movieDTO1);
            });
        }
        return movieDTO;
    }

    public MovieDTO findById(Integer movieId) {
        Optional<MovieEntity> movieEntityOptional = movieRepository.findById(movieId);
        if(!movieEntityOptional.isPresent()) {
            throw new BadRequestException("Can not find movie with id " + movieId);
        }
        MovieEntity movieEntity = movieEntityOptional.get();
        return MovieMapper.toDTO(movieEntity);
    }

    public MovieEntity save(MovieDTO movieDTO)
    {
        MovieEntity movieEntity = new MovieEntity(
                movieDTO.getTitle(),
                movieDTO.getStudioName()
        );
        try
        {
            MovieEntity movieEntityCurrent = movieRepository.save(movieEntity); //jpa
//            MovieEntity movieEntityCurrent = movieRepository.usp_InsertOrUpdateMovie(
//                0,
//                    movieEntity.getStudioName(),
//                    movieEntity.getTitle()
//            ); //stored procedure
            return movieEntityCurrent;
        }
        catch (Exception ex)
        {
            throw new BadRequestException("Can not add movie");
        }
    }

    public boolean deleteById(int movieId) {
        Optional<MovieEntity> movieEntityOptional = movieRepository.findById(movieId);
        if(!movieEntityOptional.isPresent()) {
            return false;
        }
//        MovieEntity movieEntity = movieEntityOptional.get();
//        for (MovieSeriesEntity movieSeriesEntity : movieEntity.getMovieSeriesById()) {
//            movieSeriesEntity.setMovieByMovieId(null);
//        }
        try {
            movieRepository.deleteById(movieId);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public MovieEntity updateById(int movieId, MovieDTO movieDTO) {
        Optional<MovieEntity> movieById = movieRepository.findById(movieId);
        if(!movieById.isPresent()) {
            throw new BadRequestException("Can not find movie with this Id");
        }
        MovieEntity movieEntity = movieById.get();
        movieEntity.setTitle(movieDTO.getTitle());
        movieEntity.setStudioName(movieDTO.getStudioName());
        try {
//            MovieEntity saveMovieEntity = movieRepository.save(movieEntity); //jpa
            MovieEntity saveMovieEntity = movieRepository.usp_InsertOrUpdateMovie(
                    movieEntity.getId(),
                    movieEntity.getStudioName(),
                    movieEntity.getTitle()
            ); //stored procedure
            return saveMovieEntity;
        } catch (Exception ex) {
            throw new BadRequestException("Can not update movie");
        }
    }
}
