package com.hcmute.myanime.service;

import com.hcmute.myanime.dto.SubscriptionPackageDTO;
import com.hcmute.myanime.mapper.SubscriptionPackageMapper;
import com.hcmute.myanime.model.SubscriptionPackageEntity;
import com.hcmute.myanime.repository.SubscriptionPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionPackageService {
    @Autowired
    private SubscriptionPackageRepository subscriptionPackageRepository;

    public List<SubscriptionPackageDTO> GetSubcriptionPackageActive()
    {
        List<SubscriptionPackageEntity> subscriptionPackageEntityList = subscriptionPackageRepository.findAllByEnableActive();
        List<SubscriptionPackageDTO> subcriptionPackageDTOList = new ArrayList<>();
        subscriptionPackageEntityList.forEach((subscriptionPackageEntity)->{
            SubscriptionPackageDTO subscriptionPackageDTO = SubscriptionPackageMapper.toDTO(subscriptionPackageEntity);
            subscriptionPackageDTO.setNumberOfTopUp(countPackageByPackageIdAndStatus(subscriptionPackageEntity.getId(), "paid"));
            subcriptionPackageDTOList.add(subscriptionPackageDTO);
        });
        return subcriptionPackageDTOList;
    }

    public boolean store(SubscriptionPackageDTO subcriptionPackageDTO)
    {
        SubscriptionPackageEntity subscriptionPackageEntity = SubscriptionPackageMapper.toEntity(subcriptionPackageDTO);
        try {
            subscriptionPackageRepository.save(subscriptionPackageEntity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean destroy(int packageID)
    {
        try {
            SubscriptionPackageEntity subscriptionPackageEntity = subscriptionPackageRepository.findById(packageID).isPresent() ? subscriptionPackageRepository.findById(packageID).get() : null;
            if(subscriptionPackageEntity == null)
                return false;
            subscriptionPackageRepository.delete(subscriptionPackageEntity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean update(SubscriptionPackageDTO subcriptionPackageDTO, int packageID)
    {
        Optional<SubscriptionPackageEntity> subscriptionPackageEntityOptional = subscriptionPackageRepository.findById(packageID);
        if (!subscriptionPackageEntityOptional.isPresent())
            return false;

        SubscriptionPackageEntity subscriptionPackageEntity = subscriptionPackageEntityOptional.get();
        try {
            subscriptionPackageEntity = SubscriptionPackageMapper.toEntity(subscriptionPackageEntity, subcriptionPackageDTO);
            subscriptionPackageRepository.save(subscriptionPackageEntity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public int countPackageByPackageIdAndStatus(int packageId, String status) {
        Integer numberOfTopUpPackage = subscriptionPackageRepository.countTopUpPackageByFunction(packageId, status);
        return numberOfTopUpPackage;
    }
}
