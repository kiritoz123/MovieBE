package com.hcmute.myanime.repository;

import com.hcmute.myanime.model.UsersEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
    Optional<UsersEntity> findByUsername(String username);
    Optional<UsersEntity> findByEmail(String email);
    boolean deleteByUsername(String username);
    boolean existsByUsername(String username);
    List<UsersEntity> findByUsernameContaining(String keyword, Pageable pageable);
    Long countByUsernameContaining(String keyword);

    //function SQL
    @Query(value = "select hcmutemyanime.countNormalUser()", nativeQuery = true)
    Integer countNormalUserByFunction();

    //Stored Procedure
    @Query(value = "{call hcmutemyanime.usersPageable(:currentPage, :productLimit)}", nativeQuery = true)
    List<UsersEntity> findAllByStoredProcedures(int currentPage, int productLimit);
    @Query(value = "{call hcmutemyanime.getListPremiumUser(:currentPage, :productLimit, :searchString)}", nativeQuery = true)
    List<UsersEntity> findAllUserPremiumByStoredProcedures(int currentPage, int productLimit, String searchString);
    @Query(value = "{call hcmutemyanime.getListNormalUser(:currentPage, :productLimit, :searchString)}", nativeQuery = true)
    List<UsersEntity> findAllNormalUserByStoredProcedures(int currentPage, int productLimit, String searchString);
}
