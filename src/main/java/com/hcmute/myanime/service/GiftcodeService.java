package com.hcmute.myanime.service;

import com.hcmute.myanime.auth.ApplicationUserService;
import com.hcmute.myanime.common.RandomString;
import com.hcmute.myanime.dto.GiftcodeDTO;
import com.hcmute.myanime.model.GiftCodeEntity;
import com.hcmute.myanime.model.SubscriptionPackageEntity;
import com.hcmute.myanime.model.UsersEntity;
import com.hcmute.myanime.repository.GiftcodeRepository;
import com.hcmute.myanime.repository.SubscriptionPackageRepository;
import com.hcmute.myanime.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GiftcodeService {
    @Autowired
    private GiftcodeRepository giftcodeRepository;
    @Autowired
    private SubscriptionPackageRepository subcriptionPackageRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ApplicationUserService applicationUserService;
    @Autowired
    private UserService userService;

    public List<GiftCodeEntity> findAll() {
//        List<GiftCodeEntity> giftCodeEntityList = giftcodeRepository.findAll(); //jpa
        List<GiftCodeEntity> giftCodeEntityList = giftcodeRepository.findAllByView();

        return giftCodeEntityList;
    }
    public boolean save(String quantityString, int packageId)
    {
        // Check package is exist
        Optional<SubscriptionPackageEntity> subscriptionPackageEntityOptional = subcriptionPackageRepository.findById(packageId);
        if(!subscriptionPackageEntityOptional.isPresent())
            return false;

        int quantity = -1;
        try {
            quantity = Integer.parseInt(quantityString);
            quantity = quantity <= 0 ? 1 : quantity;
        } catch (Exception ex) {
            return false;
        }

        RandomString randomString = new RandomString(15, ThreadLocalRandom.current());
        for (int i = 1; i <= quantity; i++) {
            GiftCodeEntity giftCodeEntity = new GiftCodeEntity();
            String giftCode = subscriptionPackageEntityOptional.get().getDay().toString() + "D-" + randomString.nextString();
            giftCodeEntity.setRedemptionCode(giftCode);
            giftCodeEntity.setSubscriptionPackageById(subscriptionPackageEntityOptional.get());
            try {
                giftcodeRepository.save(giftCodeEntity);
            } catch (Exception ex) {
                return false;
            }
        }
        return true;
    }

    public boolean destroy(int giftcodeId)
    {
        Optional<GiftCodeEntity> giftCodeEntityOptional = giftcodeRepository.findById(giftcodeId);
        if(!giftCodeEntityOptional.isPresent())
            return false;
        try {
            giftcodeRepository.delete(giftCodeEntityOptional.get());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean destroyAll()
    {
        try {
            giftcodeRepository.deleteAll();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean redeem(GiftcodeDTO giftcodeDTO)
    {
        // Check user was logged
        String usernameLoggedIn = applicationUserService.getUsernameLoggedIn();
        Optional<UsersEntity> userLoggedIn = usersRepository.findByUsername(usernameLoggedIn);
        if(!userLoggedIn.isPresent()) {
            return false;
        }

        // check giftcode available
        List<GiftCodeEntity> giftCodeEntityList = giftcodeRepository.findByRedemptionCode(giftcodeDTO.getRedemption_code());
        if(giftCodeEntityList.isEmpty())
            return false;
        GiftCodeEntity giftCodeEntity = giftCodeEntityList.get(0);
        // Xu ly premium
        userService.createPremium(giftCodeEntity.getSubscriptionPackageById().getId());
        giftcodeRepository.delete(giftCodeEntity);

        return true;
    }
}
