package com.hcmute.myanime.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "view_statistics", schema = "hcmutemyanime")
public class ViewStatisticsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name = "episode_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private EpisodeEntity episode;
    private String ipAddress;
    @CreationTimestamp
    private Timestamp createAt;

    public EpisodeEntity getEpisode() {
        return episode;
    }

    public void setEpisode(EpisodeEntity episode) {
        this.episode = episode;
    }

    public ViewStatisticsEntity(int id, EpisodeEntity episode, String ipAddress, Timestamp createAt) {
        this.id = id;
        this.episode = episode;
        this.ipAddress = ipAddress;
        this.createAt = createAt;
    }

    public ViewStatisticsEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
