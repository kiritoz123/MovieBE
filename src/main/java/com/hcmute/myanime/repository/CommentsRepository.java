package com.hcmute.myanime.repository;

import com.hcmute.myanime.model.CommentEntity;
import com.hcmute.myanime.model.EpisodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<CommentEntity, Integer> {

    Long countByEpisodeByEpisodeId(EpisodeEntity episodeEntity);
    @Query(value = "select distinct * from (select ms.id, ms.name, ms.description, ms.date_aired, ms.total_episode, ms.image, ms.create_at, ms.movie_id\n" +
            "                         from movie_series ms\n" +
            "                                  join episodes e on ms.id = e.series_id\n" +
            "                                  join comments c on e.id = c.episode_id\n" +
            "                         group by ms.id, c.create_at\n" +
            "                         order by c.create_at desc) as newT\n" +
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> getEpisodeIDCommentRecentWithLimit(int limit);
    //Stored procedures
    @Query(value = "{call hcmutemyanime.getEpisodeIDCommentRecentWithLimit(:limitSeries)}", nativeQuery = true)
    List<Object[]> getEpisodeIDCommentRecentWithLimitByStoredProcedures(int limitSeries);
    @Query(value = "{call hcmutemyanime.commentsByEpisodeIdPageable(:episodeId, :currentPage, :productLimit)}", nativeQuery = true)
    List<CommentEntity> getByEpisodeIdPageable_sp(int episodeId,int currentPage, int productLimit);
    @Query(value = "{call hcmutemyanime.usp_InsertOrUpdateComment(:id, :content, :episodeId, :userId)}", nativeQuery = true)
    CommentEntity insertOrUpdateComment(int id, String content, int episodeId, int userId);
}