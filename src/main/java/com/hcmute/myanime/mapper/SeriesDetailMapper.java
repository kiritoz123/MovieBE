package com.hcmute.myanime.mapper;

import com.hcmute.myanime.dto.SeriesDetailDTO;
import com.hcmute.myanime.model.EpisodeEntity;
import com.hcmute.myanime.model.MovieSeriesEntity;

import java.util.Optional;

public class SeriesDetailMapper {
    public static SeriesDetailDTO toDTO(MovieSeriesEntity movieSeriesEntity,
                                        Long seriesTotalView,
                                        Long seriesTotalComment) {
        SeriesDetailDTO seriesDetailDTO = new SeriesDetailDTO(
                movieSeriesEntity.getId(),
                movieSeriesEntity.getMovieByMovieId().getTitle(),
                movieSeriesEntity.getDescription(),
                movieSeriesEntity.getMovieByMovieId().getStudioName(),
                movieSeriesEntity.getImage(),
                movieSeriesEntity.getDateAired(),
                movieSeriesEntity.getMovieByMovieId().getCreateAt(),
                seriesTotalView,
                seriesTotalComment,
                movieSeriesEntity.getCreateAt(),
                movieSeriesEntity.getName(),
                movieSeriesEntity.getEpisodesById().size(),
                movieSeriesEntity.getTotalEpisode(),
                movieSeriesEntity.getMovieByMovieId().getId(),
                movieSeriesEntity.getMovieByMovieId().getCategoryEntityCollection().stream().toList()
        );

        // Check series have ep premium
        Optional<EpisodeEntity> episodeEntityOptional = movieSeriesEntity.getEpisodesById().stream().filter(ep -> ep.getPremiumRequired()).findFirst();
        if(episodeEntityOptional.isPresent())
            seriesDetailDTO.setIsPremium(true);
        return seriesDetailDTO;
    }
}
