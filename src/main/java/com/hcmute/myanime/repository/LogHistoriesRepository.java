package com.hcmute.myanime.repository;

import com.hcmute.myanime.model.LogHistoriesEntity;
import com.hcmute.myanime.model.MovieSeriesEntity;
import com.hcmute.myanime.model.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;


@Repository
public interface LogHistoriesRepository extends JpaRepository<LogHistoriesEntity, Integer> {
    LogHistoriesEntity findByUserAndMovieSeriesEntity(UsersEntity usersEntity, MovieSeriesEntity movieSeriesEntity);
    @Query(value = "delete from LogHistoriesEntity h where h.createAt < :day")
    void deleteByCreateAtAfterDay(@Param("day") Timestamp deleteAfterDay);
    void deleteByCreateAtLessThan(Timestamp deleteAfterDay);
    List<LogHistoriesEntity> findByCreateAtLessThan(Timestamp deleteAfterDay);
}
