package com.hcmute.myanime.repository;

import com.hcmute.myanime.model.SubscriptionPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionPackageRepository extends JpaRepository<SubscriptionPackageEntity, Integer> {
    @Query(value = "SELECT * FROM hcmutemyanime.subscription_packages WHERE subscription_packages.enable = 1"
    , nativeQuery = true)
    List<SubscriptionPackageEntity> findAllByEnableActive();

    //Function SQL
    //Function MySql
    @Query(value = "select hcmutemyanime.countTopupPackage(:subscriptionPackageId, :paymentStatus)", nativeQuery = true)
    Integer countTopUpPackageByFunction(
            @Param("subscriptionPackageId") int packageId,
            @Param("paymentStatus") String paymentStatus
    );
}
