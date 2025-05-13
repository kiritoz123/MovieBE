package com.hcmute.myanime.mapper;

import com.hcmute.myanime.dto.SubscriptionPackageDTO;
import com.hcmute.myanime.model.SubscriptionPackageEntity;

public class SubscriptionPackageMapper {
    public static SubscriptionPackageDTO toDTO (SubscriptionPackageEntity subscriptionPackageEntity)
    {
        SubscriptionPackageDTO subcriptionPackageDTO = new SubscriptionPackageDTO();
        subcriptionPackageDTO.setId(subscriptionPackageEntity.getId());
        subcriptionPackageDTO.setDay(subscriptionPackageEntity.getDay());
        subcriptionPackageDTO.setDescription(subscriptionPackageEntity.getDescription());
        subcriptionPackageDTO.setEnable(subscriptionPackageEntity.getEnable());
        subcriptionPackageDTO.setName(subscriptionPackageEntity.getName());
        subcriptionPackageDTO.setPrice(subscriptionPackageEntity.getPrice());
        return subcriptionPackageDTO;
    }

    public static SubscriptionPackageEntity toEntity (SubscriptionPackageDTO subcriptionPackageDTO)
    {
        SubscriptionPackageEntity subscriptionPackageEntity = new SubscriptionPackageEntity();

        subscriptionPackageEntity.setDay(subcriptionPackageDTO.getDay());
        subscriptionPackageEntity.setDescription(subcriptionPackageDTO.getDescription());
        subscriptionPackageEntity.setEnable(subcriptionPackageDTO.getEnable());
        subscriptionPackageEntity.setName(subcriptionPackageDTO.getName());
        subscriptionPackageEntity.setPrice(subcriptionPackageDTO.getPrice());

        return subscriptionPackageEntity;
    }

    public static SubscriptionPackageEntity toEntity(SubscriptionPackageEntity subscriptionPackageEntity, SubscriptionPackageDTO subcriptionPackageDTO)
    {
        subscriptionPackageEntity.setDay(subcriptionPackageDTO.getDay());
        subscriptionPackageEntity.setDescription(subcriptionPackageDTO.getDescription());
        subscriptionPackageEntity.setEnable(subcriptionPackageDTO.getEnable());
        subscriptionPackageEntity.setName(subcriptionPackageDTO.getName());
        subscriptionPackageEntity.setPrice(subcriptionPackageDTO.getPrice());
        return subscriptionPackageEntity;
    }
}
