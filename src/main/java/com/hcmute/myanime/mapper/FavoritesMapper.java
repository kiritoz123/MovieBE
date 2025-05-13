package com.hcmute.myanime.mapper;

import com.hcmute.myanime.dto.FavoritesDTO;
import com.hcmute.myanime.model.FavoritesEntity;

public class FavoritesMapper {
    public static FavoritesEntity toEntity (FavoritesDTO favoritesDTO)
    {
        FavoritesEntity favoritesEntity = new FavoritesEntity();
        favoritesEntity.setCreateAt(favoritesDTO.getCreateAt());
        return favoritesEntity;
    }

    public static FavoritesDTO toDTO (FavoritesEntity favoritesEntity)
    {
        FavoritesDTO favoritesDTO = new FavoritesDTO();
        favoritesDTO.setId(favoritesEntity.getId());
        favoritesDTO.setCreateAt(favoritesEntity.getCreateAt());
        favoritesDTO.setImage(favoritesEntity.getMovieSeries().getImage());
        favoritesDTO.setName(favoritesEntity.getMovieSeries().getName());
        favoritesDTO.setMovieSeriesId(favoritesEntity.getMovieSeries().getId());
        favoritesDTO.setTotalEpisode(favoritesEntity.getMovieSeries().getTotalEpisode());
        return  favoritesDTO;
    }
}
