package com.hcmute.myanime.repository;

import com.hcmute.myanime.model.EmailConfirmationEntity;
import com.hcmute.myanime.model.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailConfirmationRepository extends JpaRepository<EmailConfirmationEntity, Integer> {
    Optional<EmailConfirmationEntity> findByOtpCodeAndUsersEntityByUserId(String otpCode, UsersEntity userEntity);

    @Query("SELECT e FROM EmailConfirmationEntity e WHERE e.confirmationType = :confirmationType AND e.usersEntityByUserId = :userEntity AND TIMESTAMPADD(MINUTE, 10, e.createAt) > current_timestamp")
    List<EmailConfirmationEntity> findByConfirmationTypeAndUserEntity(String confirmationType, UsersEntity userEntity);

    @Query("SELECT e FROM EmailConfirmationEntity e WHERE e.expiredDate > current_timestamp AND e.usersEntityByUserId = :userEntity AND e.confirmationType = :confirmationType AND e.status = 'pending'")
    List<EmailConfirmationEntity> findByUsersEntityByUserIdAndExpiredAndStatusPending(String confirmationType, UsersEntity userEntity);

    @Query("SELECT e FROM EmailConfirmationEntity e WHERE e.status = 'pending' AND e.expiredDate > current_timestamp AND e.otpCode = :optCode AND e.usersEntityByUserId = :userEntity")
    Optional<EmailConfirmationEntity> findByUserAndCodeIsValid(UsersEntity userEntity, String optCode);
}
