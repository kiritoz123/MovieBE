package com.hcmute.myanime.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "movie_series", schema = "hcmutemyanime")
public class MovieSeriesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    @Column(columnDefinition="TEXT")
    private String description;
    private Timestamp dateAired;
    private int totalEpisode;
    private String image;
    @CreationTimestamp
    private Timestamp createAt;
    @OneToMany(mappedBy = "movieSeriesBySeriesId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Collection<EpisodeEntity> episodesById;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private MovieEntity movieByMovieId;
    @OneToMany(mappedBy = "movieSeries", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Collection<FavoritesEntity> favoritesEntityCollection;
    @OneToMany(mappedBy = "movieSeriesEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Collection<LogHistoriesEntity> logHistoriesEntityCollection;

    public Collection<LogHistoriesEntity> getLogHistoriesEntityCollection() {
        return logHistoriesEntityCollection;
    }

    public void setLogHistoriesEntityCollection(Collection<LogHistoriesEntity> logHistoriesEntityCollection) {
        this.logHistoriesEntityCollection = logHistoriesEntityCollection;
    }

    public Collection<FavoritesEntity> getFavoritesEntityCollection() {
        return favoritesEntityCollection;
    }

    public void setFavoritesEntityCollection(Collection<FavoritesEntity> favoritesEntityCollection) {
        this.favoritesEntityCollection = favoritesEntityCollection;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public Timestamp getDateAired() {
        return dateAired;
    }

    public void setDateAired(Timestamp dateAired) {
        this.dateAired = dateAired;
    }

    public int getTotalEpisode() {
        return totalEpisode;
    }

    public void setTotalEpisode(int totalEpisode) {
        this.totalEpisode = totalEpisode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MovieSeriesEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieSeriesEntity that = (MovieSeriesEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (createAt != null ? !createAt.equals(that.createAt) : that.createAt != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "MovieSeriesEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateAired=" + dateAired +
                ", totalEpisode=" + totalEpisode +
                ", image='" + image + '\'' +
                ", createAt=" + createAt +
                ", episodesById=" + episodesById +
                ", movieByMovieId=" + movieByMovieId +
                '}';
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
        return result;
    }

    public Collection<EpisodeEntity> getEpisodesById() {
        return episodesById;
    }

    public void setEpisodesById(Collection<EpisodeEntity> episodesById) {
        this.episodesById = episodesById;
    }

    public MovieEntity getMovieByMovieId() {
        return movieByMovieId;
    }

    public void setMovieByMovieId(MovieEntity movieByMovieId) {
        this.movieByMovieId = movieByMovieId;
    }
}
