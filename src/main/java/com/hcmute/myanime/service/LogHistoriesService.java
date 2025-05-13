package com.hcmute.myanime.service;

import com.hcmute.myanime.dto.LogHistoryDTO;
import com.hcmute.myanime.exception.BadRequestException;
import com.hcmute.myanime.model.*;
import com.hcmute.myanime.repository.EpisodeRepository;
import com.hcmute.myanime.repository.LogHistoriesRepository;
import com.hcmute.myanime.repository.MovieSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LogHistoriesService {
    @Autowired
    private LogHistoriesRepository logHistoriesRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private MovieSeriesRepository movieSeriesRepository;

    public List<LogHistoriesEntity> getByUserLogging()
    {
        List<LogHistoriesEntity> historiesEntityList =
                userService.findUserLogging()
                .getLogHistoriesEntityCollection().stream().toList();
        return historiesEntityList;
    }

    public LogHistoriesEntity save(LogHistoryDTO logHistoryDTO) {
        Optional<EpisodeEntity> episodeEntityOptional = episodeRepository.findById(logHistoryDTO.getEpisode_id());
        if(!episodeEntityOptional.isPresent()) {
            throw new BadRequestException("Episode not found");
        }
        EpisodeEntity episodeEntity = episodeEntityOptional.get();
        UsersEntity userLogging = userService.findUserLogging();

        LogHistoriesEntity logHistoriesEntity = logHistoriesRepository
                .findByUserAndMovieSeriesEntity(userLogging, episodeEntity.getMovieSeriesBySeriesId());
        if(logHistoriesEntity == null) {
            logHistoriesEntity = new LogHistoriesEntity();
        }
        logHistoriesEntity.setLastSecond(logHistoryDTO.getLastSecond());
        logHistoriesEntity.setEpisodeEntity(episodeEntity);
        logHistoriesEntity.setMovieSeriesEntity(episodeEntity.getMovieSeriesBySeriesId());
        logHistoriesEntity.setUser(userLogging);
        Timestamp createAt = new Timestamp(System.currentTimeMillis());
        logHistoriesEntity.setCreateAt(createAt);
        try
        {
            LogHistoriesEntity save = logHistoriesRepository.save(logHistoriesEntity);
            return save;
        }
        catch (Exception ex)
        {
            throw new BadRequestException("create history fail");
        }
    }

    public boolean deleteById(int historyId) {
        try {
            logHistoriesRepository.deleteById(historyId);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Transactional
    public Boolean deleteAfterDay(int numberOfDate) {
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        currentDate.setDate(currentDate.getDate() - numberOfDate);
        try {
            logHistoriesRepository.deleteByCreateAtLessThan(currentDate);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
