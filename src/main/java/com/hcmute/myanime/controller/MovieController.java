package com.hcmute.myanime.controller;

import com.hcmute.myanime.common.Common;
import com.hcmute.myanime.dto.MovieDTO;
import com.hcmute.myanime.dto.ResponseDTO;
import com.hcmute.myanime.exception.BadRequestException;
import com.hcmute.myanime.mapper.MovieMapper;
import com.hcmute.myanime.model.MovieEntity;
import com.hcmute.myanime.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class MovieController {
    @Autowired
    private MovieService movieService;
    @GetMapping("/movie")
    public ResponseEntity<?> getAllMovie(@RequestParam Map<String, String> requestParams)
    {
        String page = requestParams.get("page");
        String limit = requestParams.get("limit");
        String keywordSearch = requestParams.get("keyword");
        return ResponseEntity.ok(movieService.findAll(page, limit, keywordSearch));
    }
    @GetMapping("/movie/{movieID}")
    public ResponseEntity<?> getMovieById(@PathVariable int movieID)
    {
        return ResponseEntity.ok(movieService.findById(movieID));
    }
    @PostMapping("/movie")
    public ResponseEntity<?> createNewMovie(@RequestBody MovieDTO movieDTO)
    {
        MovieEntity movieEntity = movieService.save(movieDTO);
        return ResponseEntity.ok(
                    new ResponseDTO(
                            HttpStatus.OK,
                            Common.MessageRespone.StorageMovieSuccess,
                            movieEntity
                            )
            );
    }
    @PutMapping("/movie/{movieID}")
    public ResponseEntity<?> updateMovieByID(@RequestBody MovieDTO movieDTO, @PathVariable int movieID)
    {
        MovieEntity movieEntity = movieService.updateById(movieID, movieDTO);
        MovieDTO movieResponseDTO = MovieMapper.toDTO(movieEntity);
        return ResponseEntity.ok(
                new ResponseDTO(
                        HttpStatus.OK,
                        Common.MessageRespone.UpdateMovieSuccess,
                        movieResponseDTO)
        );
    }

    @DeleteMapping("/movie/{movieID}")
    public ResponseEntity<?> deleteMovieByID(@PathVariable int movieID) {
        if(movieService.deleteById(movieID)) {
            return ResponseEntity.ok(
                    new ResponseDTO(HttpStatus.OK, "Delete movie success")
            );
        } else {
            throw new BadRequestException("ID not found");
        }
    }
}
