package com.hcmute.myanime.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "hcmutemyanime")
public class UsersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String avatar;
    @CreationTimestamp
    private Timestamp createAt;
    @Column(columnDefinition = "boolean default true")
    private Boolean enable = true;
    @OneToMany(mappedBy = "usersByUserId", fetch = FetchType.LAZY)
    @JsonBackReference
    private Collection<CommentEntity> commentsById;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_role_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private RolesEntity userRoleByUserRoleId;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Collection<LogHistoriesEntity> logHistoriesEntityCollection;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Collection<FavoritesEntity> favoritesEntityCollection;

    @OneToMany(mappedBy = "usersEntityByUserId", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Collection<EmailConfirmationEntity> emailConfirmationEntityCollection;

    @OneToMany(mappedBy = "usersEntityById", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Collection<UserPremiumEntity> userPremiumCollection;

    @OneToMany(mappedBy = "usersEntityById", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Collection<OrderPremiumEntity> orderPremiumEntityCollection;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity) o;
        return id == that.id && Objects.equals(fullName, that.fullName) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(email, that.email) && Objects.equals(avatar, that.avatar) && Objects.equals(createAt, that.createAt) && Objects.equals(enable, that.enable) && Objects.equals(commentsById, that.commentsById) && Objects.equals(userRoleByUserRoleId, that.userRoleByUserRoleId) && Objects.equals(logHistoriesEntityCollection, that.logHistoriesEntityCollection) && Objects.equals(favoritesEntityCollection, that.favoritesEntityCollection) && Objects.equals(emailConfirmationEntityCollection, that.emailConfirmationEntityCollection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, username, password, email, avatar, createAt, enable, commentsById, userRoleByUserRoleId, logHistoriesEntityCollection, favoritesEntityCollection, emailConfirmationEntityCollection);
    }

    public Collection<EmailConfirmationEntity> getEmailConfirmationEntityCollection() {
        return emailConfirmationEntityCollection;
    }

    public void setEmailConfirmationEntityCollection(Collection<EmailConfirmationEntity> emailConfirmationEntityCollection) {
        this.emailConfirmationEntityCollection = emailConfirmationEntityCollection;
    }

    public UsersEntity() {
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Collection<CommentEntity> getCommentsById() {
        return commentsById;
    }

    public void setCommentsById(Collection<CommentEntity> commentsById) {
        this.commentsById = commentsById;
    }

    public RolesEntity getUserRoleByUserRoleId() {
        return userRoleByUserRoleId;
    }

    public void setUserRoleByUserRoleId(RolesEntity userRoleByUserRoleId) {
        this.userRoleByUserRoleId = userRoleByUserRoleId;
    }
}
