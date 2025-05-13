package com.hcmute.myanime.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "log_histories", schema = "hcmutemyanime")
public class LogHistoriesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private Long lastSecond;

    @CreationTimestamp
    private Timestamp createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private EpisodeEntity episodeEntity; // mappedBy in table

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private MovieSeriesEntity movieSeriesEntity; // mappedBy in table

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private UsersEntity user;

    public LogHistoriesEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getLastSecond() {
        return lastSecond;
    }

    public void setLastSecond(Long lastSecond) {
        this.lastSecond = lastSecond;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public EpisodeEntity getEpisodeEntity() {
        return episodeEntity;
    }

    public void setEpisodeEntity(EpisodeEntity episodeEntity) {
        this.episodeEntity = episodeEntity;
    }

    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }

    public MovieSeriesEntity getMovieSeriesEntity() {
        return movieSeriesEntity;
    }

    public void setMovieSeriesEntity(MovieSeriesEntity movieSeriesEntity) {
        this.movieSeriesEntity = movieSeriesEntity;
    }

    @Override
    public String toString() {
        return "LogHistoriesEntity{" +
                "id=" + id +
                ", lastSecond=" + lastSecond +
                ", createAt=" + createAt +
                '}';
    }
}
