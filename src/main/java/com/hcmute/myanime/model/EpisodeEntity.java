package com.hcmute.myanime.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "episodes", schema = "hcmutemyanime")
@DynamicUpdate
public class EpisodeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String title;
    @Column(columnDefinition = "int default 0") //default num of episode is -1 (Unset)
    @ColumnDefault(value = "0")
    private int numEpisodes;
    private String resource;
    private String resourcePublicId;
    private String resourceDo;
    @Column(columnDefinition = "Bigint default '0'")
    private Long totalView = Long.valueOf(0);
    @CreationTimestamp
    private Timestamp createAt;
    @Column(columnDefinition = "boolean default true")
    private Boolean enable = true;
    @OneToMany(mappedBy = "episodeByEpisodeId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Collection<CommentEntity> commentsById;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", referencedColumnName = "id")
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MovieSeriesEntity movieSeriesBySeriesId;

    @OneToMany(mappedBy = "episodeEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Collection<LogHistoriesEntity> logHistoriesEntityCollection;

    @OneToMany(mappedBy = "episode", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<ViewStatisticsEntity> viewStatisticsEntity;

    @Column(columnDefinition = "boolean default false")
    private Boolean premiumRequired = false;

    public int getNumEpisodes() {
        return numEpisodes;
    }

    public void setNumEpisodes(int numEpisodes) {
        this.numEpisodes = numEpisodes;
    }

    public String getResourceDo() {
        return resourceDo;
    }

    public void setResourceDo(String resourceDo) {
        this.resourceDo = resourceDo;
    }

    public Boolean getPremiumRequired() {
        return premiumRequired;
    }

    public void setPremiumRequired(Boolean premiumRequired) {
        this.premiumRequired = premiumRequired;
    }

    public EpisodeEntity() {
    }

    public Long getTotalView() {
        return totalView;
    }

    public void setTotalView(Long totalView) {
        this.totalView = totalView;
    }

    public String getResourcePublicId() {
        return resourcePublicId;
    }

    public void setResourcePublicId(String resourcePublicId) {
        this.resourcePublicId = resourcePublicId;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EpisodeEntity that = (EpisodeEntity) o;

        if (id != that.id) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (resource != null ? !resource.equals(that.resource) : that.resource != null) return false;
        if (createAt != null ? !createAt.equals(that.createAt) : that.createAt != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "EpisodeEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", resource='" + resource + '\'' +
                ", createAt=" + createAt +
                '}';
    }

    public Collection<CommentEntity> getCommentsById() {
        return commentsById;
    }

    public void setCommentsById(Collection<CommentEntity> commentsById) {
        this.commentsById = commentsById;
    }

    public MovieSeriesEntity getMovieSeriesBySeriesId() {
        return movieSeriesBySeriesId;
    }

    public void setMovieSeriesBySeriesId(MovieSeriesEntity movieSeriesBySeriesId) {
        this.movieSeriesBySeriesId = movieSeriesBySeriesId;
    }
}
