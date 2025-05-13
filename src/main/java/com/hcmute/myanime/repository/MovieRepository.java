package com.hcmute.myanime.repository;

import com.hcmute.myanime.model.MovieEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Integer> {

    List<MovieEntity> findByTitleContaining(String keyword, Pageable pageable);
    //Stored procedure
    @Query(value = "{call hcmutemyanime.moviePageable(:currentPage, :productLimit)}", nativeQuery = true)
    List<MovieEntity> findAllByStoredProcedures(int currentPage, int productLimit);
    @Query(value = "{call hcmutemyanime.usp_InsertOrUpdateMovie(:id, :studioName, :title)}", nativeQuery = true)
    MovieEntity usp_InsertOrUpdateMovie(int id, String studioName, String title); //ID = 0 insert, id != 0 update
}
