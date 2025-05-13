package com.hcmute.myanime.repository;

import com.hcmute.myanime.model.GiftCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GiftcodeRepository extends JpaRepository<GiftCodeEntity, Integer> {
    List<GiftCodeEntity> findByRedemptionCode(String redemptionCode);

    //Call View
    @Query(value = "select * from hcmutemyanime.giftcodeview", nativeQuery = true)
    List<GiftCodeEntity> findAllByView();
}
