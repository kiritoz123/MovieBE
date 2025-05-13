package com.hcmute.myanime.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmute.myanime.auth.ApplicationUserService;
import com.hcmute.myanime.dto.EpisodeDTO;
import com.hcmute.myanime.dto.ResponseDTO;
import com.hcmute.myanime.exception.BadRequestException;
import com.hcmute.myanime.mapper.EpisodeMapper;
import com.hcmute.myanime.model.EpisodeEntity;
import com.hcmute.myanime.model.UsersEntity;
import com.hcmute.myanime.repository.UsersRepository;
import com.hcmute.myanime.service.CloudinaryService;
import com.hcmute.myanime.service.EpisodeService;
import com.hcmute.myanime.service.UserService;
import com.hcmute.myanime.utils.FileUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class EpisodeController {

    @Autowired
    private EpisodeService episodeService;
    @Autowired
    private UserService userService;


    //region Module Admin
    @PostMapping("/admin/episode/series/{seriesId}")
    public ResponseEntity<?> createEpisodeOfSeries(
            @RequestParam String model,
            @RequestParam(value = "sourceFile", required = false) MultipartFile sourceFile,
            @RequestParam List<String> servers,
            @PathVariable int seriesId
    ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        EpisodeDTO episodeDTO = mapper.readValue(model, EpisodeDTO.class);
        EpisodeEntity saveEpisodeEntity = episodeService.save(episodeDTO, seriesId);
        if (saveEpisodeEntity == null) {
            return ResponseEntity.badRequest().body("add episode fail");
        }
        for (String server : servers) {
            switch (server) {
                case "do" -> {
                    try {
                        episodeService.uploadSourceFileToDigitalOcean(sourceFile.getInputStream(), sourceFile.getContentType(), saveEpisodeEntity.getId());
                    } catch (Exception e) {
                        return ResponseEntity.badRequest().body("Episode create success, source DO add fail");
                    }
                }
                case "cd" -> {
                    try {
                        episodeService.uploadSourceFileToCloudinary(sourceFile.getBytes(), saveEpisodeEntity.getId());
                    } catch (Exception e) {
                        return ResponseEntity.badRequest().body("Episode create success, source CD add fail");
                    }
                }
            }
        }
        return ResponseEntity.ok(
                new ResponseDTO(
                        HttpStatus.OK, "add episode success")
        );
    }

    @PutMapping("/admin/episode/{episodeId}")
    public ResponseEntity<?> updateEpisodeOfSeries(
            @RequestParam String model,
            @RequestParam(value = "sourceFile", required = false) MultipartFile sourceFile,
            @RequestParam List<String> servers,
            @PathVariable int episodeId
    ) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        EpisodeDTO episodeDTO = mapper.readValue(model, EpisodeDTO.class);
        if (episodeService.updateByEpisodeId(episodeId, episodeDTO, sourceFile, servers)) {
            return ResponseEntity.ok(
                    new ResponseDTO(HttpStatus.OK, "Update episode success")
            );
        } else {
            return ResponseEntity.badRequest().body("Update episode fail");
        }
    }



    @DeleteMapping("/admin/episode/{episodeId}")
    public ResponseEntity<?> deleteEpisodeById(@PathVariable int episodeId) {
        if (episodeService.deleteById(episodeId)) {
            return ResponseEntity.ok(
                    new ResponseDTO(HttpStatus.OK, "Delete episode success")
            );
        } else {
            return ResponseEntity.badRequest().body("Delete episode fail");
        }
    }
    //endregion

    //region Module Client
    @GetMapping(value = "/episode/series/{seriesId}")
    public ResponseEntity<?> getEpisodeOfSeries(@PathVariable int seriesId) {

        // Check user logged is a premium member
        boolean isPremium = userService.isPremiumMember();

        List<EpisodeEntity> listEpisodeBySeriesId = episodeService.findBySeriesId(seriesId);
        List<EpisodeDTO> episodeDTOList = new ArrayList<>();
        listEpisodeBySeriesId.forEach((episode) -> {
            EpisodeDTO episodeDTO = EpisodeMapper.toDTO(episode);

            // If episode is a premium , required user logged also premium member
            if(episodeDTO.getPremiumRequired()) {
                if (!isPremium) {
                    episodeDTO.setResourceCD("Sorry you is not a premium member");
                    episodeDTO.setResourceDO("Sorry you is not a premium member");
                }
            }

            episodeDTOList.add(episodeDTO);
        });
        return ResponseEntity.ok(episodeDTOList);
    }

    @GetMapping(value = "/episode/{episodeId}")
    public ResponseEntity<?> getEpisodeById(@PathVariable int episodeId) {
        EpisodeEntity episodeEntity = episodeService.findById(episodeId);
        EpisodeDTO episodeDTO = EpisodeMapper.toDTO(episodeEntity);
        return ResponseEntity.ok(episodeDTO);
    }

    @PatchMapping("/episode/increaseview/{episodeId}")
    public ResponseEntity<?> increaseView(@PathVariable int episodeId, HttpServletRequest request) {
        String ipClient = request.getRemoteAddr();
        if (episodeService.increaseView(episodeId, ipClient)) {
            return ResponseEntity.ok(
                    new ResponseDTO(HttpStatus.OK, "Increase view success")
            );
        } else {
            return ResponseEntity.badRequest().body("Increase view fail");
        }
    }
    //endregion

}
