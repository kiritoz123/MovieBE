package com.hcmute.myanime.repository;

import com.hcmute.myanime.model.OrderPremiumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPremiumRepository extends JpaRepository<OrderPremiumEntity, Integer> {

}
