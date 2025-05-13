package com.hcmute.myanime.repository;


import com.hcmute.myanime.model.EpisodeEntity;
import com.hcmute.myanime.model.ViewStatisticsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.*;

public interface ViewStatisticsRepository extends JpaRepository<ViewStatisticsEntity, Integer> {
    @Query(value = "SELECT v FROM ViewStatisticsEntity v WHERE v.ipAddress=:ipclient AND v.episode=:episode ORDER BY v.createAt DESC")
    List<ViewStatisticsEntity> findByIpAddressAndEpisode(String ipclient, EpisodeEntity episode, Pageable pageable);

//Query for MySQL
    @Query(value = "SELECT v.episode, COUNT(v.id) AS statisticsView " +
            "FROM ViewStatisticsEntity v " +
//            "WHERE DATEDIFF(current_timestamp, v.createAt) <= :numberOfDay " +
            "GROUP BY v.episode " +
            "ORDER BY statisticsView DESC")
//Query for MSSQL (Use findTopMostViewWithDay_StoredProcedures)
    List<Object[]> findTopMostViewWithDay(int numberOfDay, Pageable pageable);

    Optional<ViewStatisticsEntity> findByIpAddress(String ipAddress);
    Optional<ViewStatisticsEntity> findByEpisode(EpisodeEntity episodeEntity);

    //Function SQL
    @Query(value = "select hcmutemyanime.countViewStatisticsByYearAndMonth(:yearCreateAt, :monthCreateAt)", nativeQuery = true)
    Long countByYearCreateAtAndMonthCreateAt_FunctionSQL(int yearCreateAt, int monthCreateAt);
    @Query(value = "select hcmutemyanime.countTotalViewByYear(:yearCreateAt)", nativeQuery = true)
    Long countByYearCreateAt_FunctionSQL(int yearCreateAt);
    @Query(value = "select hcmutemyanime.countViewStatisticsByCategoryId(:categoryId)", nativeQuery = true)
    Long countByCategoryId_FunctionSQL(int categoryId);

    //Stored procedure (SQL server)
    @Query(value = "EXEC hcmutemyanime.findEpisodeTopMostViewWithDay :numberOfDay, :size", nativeQuery = true)
    List<Object[]> findTopMostViewWithDay_StoredProcedures(int numberOfDay, int size);
}
