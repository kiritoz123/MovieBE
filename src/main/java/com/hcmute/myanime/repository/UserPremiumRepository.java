package com.hcmute.myanime.repository;

import com.hcmute.myanime.model.MovieSeriesEntity;
import com.hcmute.myanime.model.UserPremiumEntity;
import com.hcmute.myanime.model.UsersEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface UserPremiumRepository extends JpaRepository<UserPremiumEntity, Integer> {
    List<UserPremiumEntity> findByUsersEntityById(UsersEntity usersEntity);

    @Query("SELECT u FROM UserPremiumEntity u WHERE u.usersEntityById = :usersEntity AND u.expiredAt > current_timestamp()")
    List<UserPremiumEntity> findByUserIdAndExpired(UsersEntity usersEntity);

//    @Query("SELECT TIME(TIMEDIFF(MAX(u.expiredAt), current_timestamp())) AS time_remain FROM UserPremiumEntity u WHERE u.usersEntityById = :usersEntity")
//    List<Object> getTimeRemain(UsersEntity usersEntity);

    @Query("SELECT u.expiredAt FROM UserPremiumEntity u WHERE u.usersEntityById = :usersEntity")
    List<Timestamp> getTimeRemain(UsersEntity usersEntity);

    @Query("SELECT MAX(u.expiredAt) AS expired_at FROM UserPremiumEntity u WHERE u.usersEntityById = :usersEntity")
    Timestamp getExpiredAt(UsersEntity usersEntity);
    List<UserPremiumEntity> findAllByExpiredAtAfter(Timestamp day);
    int countByExpiredAtAfter(Timestamp day);
    
    //Function MySql
    @Query(value = "select hcmutemyanime.countPremiumUser()", nativeQuery = true)
    Integer countPremiumUserByFunction();
}
