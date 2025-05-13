package com.hcmute.myanime.repository;

import com.hcmute.myanime.model.MovieSeriesEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MovieSeriesRepository extends JpaRepository<MovieSeriesEntity, Integer> {
    List<MovieSeriesEntity> findByNameContaining(String keyword, Pageable pageable);
    Long countByNameContaining(String keyword);
    @Query(value = "select series from MovieSeriesEntity series join " +
            "series.movieByMovieId movie join " +
            "movie.categoryEntityCollection categoryMovie where categoryMovie.id = :categoryId")
    List<MovieSeriesEntity> findByCategoryId(@Param("categoryId") int categoryId, Pageable pageable);
    List<MovieSeriesEntity> findAllByCreateAtAfter(Timestamp day, Pageable pageable);
    //Call by stored procedures
    @Query(value = "{call hcmutemyanime.movieSeriesPageable(:currentPage, :productLimit)}", nativeQuery = true)
    List<MovieSeriesEntity> findAllByStoredProcedures(int currentPage, int productLimit);
    @Query(value = "{call hcmutemyanime.movieSeriesByCategoryIdPageable(:categoryId, :currentPage, :productLimit)}", nativeQuery = true)
    List<MovieSeriesEntity> findByCategoryIdByStoredProcedures(int categoryId,int currentPage, int productLimit);
    @Query(value = "{call hcmutemyanime.usp_InsertOrUpdateMovieSeries(:id, :name, :description, :dateAired, :totalEpisode, :movieId, :image)}", nativeQuery = true)
    MovieSeriesEntity InsertOrUpdateMovieSeriesByStoredProcedures(int id, String name, String description, Timestamp dateAired, int totalEpisode, int movieId, String image);
}
