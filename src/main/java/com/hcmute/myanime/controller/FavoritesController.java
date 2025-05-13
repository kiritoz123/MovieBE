package com.hcmute.myanime.controller;

import com.hcmute.myanime.auth.ApplicationUserService;
import com.hcmute.myanime.dto.FavoritesDTO;
import com.hcmute.myanime.dto.ResponseDTO;
import com.hcmute.myanime.exception.BadRequestException;
import com.hcmute.myanime.mapper.FavoritesMapper;
import com.hcmute.myanime.model.FavoritesEntity;
import com.hcmute.myanime.service.FavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class FavoritesController {
    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private ApplicationUserService applicationUserService;

    @GetMapping("/favorites")
    public ResponseEntity<?> findByUserLogin()
    {
        String usernameLoggedIn = applicationUserService.getUsernameLoggedIn();
        System.out.println(usernameLoggedIn);
        List<FavoritesEntity> favoritesEntityList = favoritesService.findByUserLogin(usernameLoggedIn);
        List<FavoritesDTO> favoritesDTOList = new ArrayList<>();
        favoritesEntityList.forEach((favoritesEntity -> {
            FavoritesDTO favoritesDTO = FavoritesMapper.toDTO(favoritesEntity);
            favoritesDTOList.add(favoritesDTO);
        }));
        return ResponseEntity.ok(favoritesDTOList);
    }

    @GetMapping("/favorites/{favoritesID}")
    public ResponseEntity<?> findByMovieSeriesIdAndUserLoggingId(@PathVariable int favoritesID)
    {
        FavoritesEntity favoritesEntity = favoritesService.findByMovieSeriesIdAndUserLoggingId(favoritesID);
        List<FavoritesDTO> favoritesDTOList = new ArrayList<>();
        if(favoritesEntity == null) {
            return ResponseEntity.ok(favoritesDTOList);
        }
        favoritesDTOList.add(FavoritesMapper.toDTO(favoritesEntity));
        return ResponseEntity.ok(favoritesDTOList);
    }

    @PostMapping("/favorites")
    public ResponseEntity<?> storage(@RequestBody FavoritesDTO favoritesDTO)
    {
        if(favoritesService.saveOrDelete(favoritesDTO)) {
            return ResponseEntity.ok(
                    new ResponseDTO(
                            HttpStatus.OK,
                            "Create new movie series success"
                    )
            );
        } else {
            throw new BadRequestException("create fail");
        }
    }

    @DeleteMapping("/favorites/{favoritesID}")
    public ResponseEntity<?> deleteSeriesById(@PathVariable int favoritesID) {
        if(favoritesService.deleteById(favoritesID)) {
            return ResponseEntity.ok(
                    new ResponseDTO(HttpStatus.OK, "Delete series success")
            );
        } else {
            return ResponseEntity.badRequest().body("Delete series fail");
        }
    }
}
