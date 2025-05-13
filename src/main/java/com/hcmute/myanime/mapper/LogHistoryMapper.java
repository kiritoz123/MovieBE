package com.hcmute.myanime.mapper;

import com.hcmute.myanime.dto.LogHistoryDTO;
import com.hcmute.myanime.model.LogHistoriesEntity;

public class LogHistoryMapper {
    public static LogHistoryDTO toDTO(LogHistoriesEntity logHistoriesEntity)
    {
        return new LogHistoryDTO(
                logHistoriesEntity.getId(),
                logHistoriesEntity.getLastSecond(),
                logHistoriesEntity.getCreateAt(),
                logHistoriesEntity.getEpisodeEntity().getId(),
                logHistoriesEntity.getMovieSeriesEntity().getId(),
                logHistoriesEntity.getMovieSeriesEntity().getImage(),
                logHistoriesEntity.getMovieSeriesEntity().getName(),
                logHistoriesEntity.getEpisodeEntity().getNumEpisodes()
        );
    }
}
