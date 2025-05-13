package com.hcmute.myanime.mapper;

import com.hcmute.myanime.dto.OrderPremiumDTO;
import com.hcmute.myanime.model.OrderPremiumEntity;

public class OrderPremiumMapper {
    public static OrderPremiumEntity toEntity(OrderPremiumDTO orderPremiumDTO)
    {
        OrderPremiumEntity orderPremiumEntity = new OrderPremiumEntity();

        orderPremiumEntity.setStatus(orderPremiumDTO.getStatus());
        orderPremiumEntity.setUsersEntityById(UserMapper.toEntity(orderPremiumDTO.getUserDTO()));
        orderPremiumEntity.setCreateAt(orderPremiumDTO.getCreateAt());
        orderPremiumEntity.setDescription(orderPremiumDTO.getDescription());
        orderPremiumEntity.setMethod(orderPremiumDTO.getMethod());
        orderPremiumEntity.setSubscriptionPackageById(SubscriptionPackageMapper.toEntity(orderPremiumDTO.getSubcriptionPackageDTO()));

        return orderPremiumEntity;
    }
}
