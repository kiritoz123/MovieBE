package com.hcmute.myanime.controller;

import com.hcmute.myanime.dto.FavoritesDTO;
import com.hcmute.myanime.dto.LogHistoryDTO;
import com.hcmute.myanime.dto.ResponseDTO;
import com.hcmute.myanime.exception.BadRequestException;
import com.hcmute.myanime.mapper.LogHistoryMapper;
import com.hcmute.myanime.model.LogHistoriesEntity;
import com.hcmute.myanime.service.LogHistoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class LogHistoriesController {

    @Autowired
    private LogHistoriesService logHistoriesService;

    @GetMapping("/history")
    public ResponseEntity<?> getAllHistoryUserLogging() {
        List<LogHistoriesEntity> historyByUserLogging = logHistoriesService.getByUserLogging();
        List<LogHistoryDTO> logHistoryDTOList = new ArrayList<>();
        historyByUserLogging.forEach(history -> {
            logHistoryDTOList.add(LogHistoryMapper.toDTO(history));
        });
        return ResponseEntity.ok(logHistoryDTOList);
    }

    @PostMapping("/history") //Case if episode id and user is similar will be replaced
    public ResponseEntity<?> createHistoryUserLogging(@RequestBody LogHistoryDTO logHistoryDTO) {
        LogHistoriesEntity historyEntity = logHistoriesService.save(logHistoryDTO);
        LogHistoryDTO logHistoryResponseDTO = LogHistoryMapper.toDTO(historyEntity);
        return ResponseEntity.ok(
                    new ResponseDTO(
                            HttpStatus.OK,
                            "Create new history series success",
                            logHistoryResponseDTO
                    )
            );
    }

    @DeleteMapping("/history/{historyId}")
    public ResponseEntity<?> deleteHistoryById(@PathVariable int historyId) {
        if (logHistoriesService.deleteById(historyId)) {
            return ResponseEntity.ok(
                    new ResponseDTO(HttpStatus.OK, "Delete history success")
            );
        } else {
            return ResponseEntity.badRequest().body("Delete history fail");
        }
    }
}
