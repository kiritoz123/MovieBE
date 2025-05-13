package com.hcmute.myanime.repository;

import com.hcmute.myanime.model.CategoryEntity;
import com.hcmute.myanime.model.FavoritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface FavoritesRepository extends JpaRepository<FavoritesEntity, Integer> {
    @Query( "SELECT o FROM FavoritesEntity o WHERE movie_series_id = :mvsid AND user_id = :uid" )
    FavoritesEntity findByMovieSeriesIdAndUserId(@Param("mvsid") Integer movieSeriesId, @Param("uid") Integer userId);
}
