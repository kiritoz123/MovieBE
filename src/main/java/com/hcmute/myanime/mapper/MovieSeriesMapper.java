package com.hcmute.myanime.mapper;


import com.hcmute.myanime.dto.MovieDTO;
import com.hcmute.myanime.dto.MovieSeriesDTO;
import com.hcmute.myanime.dto.StatisticsMovieSeriesDTO;
import com.hcmute.myanime.model.MovieSeriesEntity;
import com.hcmute.myanime.service.MovieSeriesService;

public class MovieSeriesMapper {


    public static MovieSeriesEntity toEntity (MovieSeriesDTO movieSeriesDTO)
    {
        MovieSeriesEntity movieSeriesEntity = new MovieSeriesEntity();
        movieSeriesEntity.setId(movieSeriesDTO.getId());
        movieSeriesEntity.setCreateAt(movieSeriesDTO.getCreateAt());
        movieSeriesEntity.setDateAired(movieSeriesDTO.getDateAired());
        movieSeriesEntity.setDescription(movieSeriesDTO.getDescription());
        movieSeriesEntity.setImage(movieSeriesDTO.getImage());
        movieSeriesEntity.setName(movieSeriesDTO.getName());
        movieSeriesEntity.setTotalEpisode(movieSeriesDTO.getTotalEpisode());
        return movieSeriesEntity;
    }

    public static MovieSeriesDTO toDTO (MovieSeriesEntity movieSeriesEntity, Long viewOfSeries)
    {
        MovieSeriesDTO movieSeriesDTO = new MovieSeriesDTO();
        movieSeriesDTO.setId(movieSeriesEntity.getId());
        movieSeriesDTO.setCreateAt(movieSeriesEntity.getCreateAt());
        movieSeriesDTO.setDateAired(movieSeriesEntity.getDateAired());
        movieSeriesDTO.setDescription(movieSeriesEntity.getDescription());
        movieSeriesDTO.setImage(movieSeriesEntity.getImage());
        movieSeriesDTO.setName(movieSeriesEntity.getName());
        movieSeriesDTO.setTotalEpisode(movieSeriesEntity.getTotalEpisode());
        movieSeriesDTO.setMovieId(movieSeriesEntity.getMovieByMovieId().getId());
        movieSeriesDTO.setMovieData(MovieMapper.toDTO(movieSeriesEntity.getMovieByMovieId()));
        movieSeriesDTO.setTotalViewOfSeries(viewOfSeries);

        return movieSeriesDTO;
    }

    public static StatisticsMovieSeriesDTO toDTO2 (MovieSeriesEntity movieSeriesEntity, long statisticsViewTotal)
    {
        StatisticsMovieSeriesDTO statisticsMovieSeriesDTO = new StatisticsMovieSeriesDTO();
        statisticsMovieSeriesDTO.setId(movieSeriesEntity.getId());
        statisticsMovieSeriesDTO.setCreateAt(movieSeriesEntity.getCreateAt());
        statisticsMovieSeriesDTO.setDateAired(movieSeriesEntity.getDateAired());
        statisticsMovieSeriesDTO.setDescription(movieSeriesEntity.getDescription());
        statisticsMovieSeriesDTO.setImage(movieSeriesEntity.getImage());
        statisticsMovieSeriesDTO.setName(movieSeriesEntity.getName());
        statisticsMovieSeriesDTO.setTotalEpisode(movieSeriesEntity.getTotalEpisode());
        statisticsMovieSeriesDTO.setMovieId(movieSeriesEntity.getMovieByMovieId().getId());
        statisticsMovieSeriesDTO.setMovieData(MovieMapper.toDTO(movieSeriesEntity.getMovieByMovieId()));
        statisticsMovieSeriesDTO.setStatisticsViewTotal(statisticsViewTotal);

        return statisticsMovieSeriesDTO;
    }
}
