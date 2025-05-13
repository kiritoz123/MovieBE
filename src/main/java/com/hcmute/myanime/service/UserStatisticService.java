package com.hcmute.myanime.service;

import com.hcmute.myanime.common.GlobalVariable;
import com.hcmute.myanime.repository.UserPremiumRepository;
import com.hcmute.myanime.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStatisticService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserPremiumRepository userPremiumRepository;

    public int getNumberOfUser(String userType) {
        if(userType.equals(GlobalVariable.PREMIUM_USER)) {
            return userPremiumRepository.countPremiumUserByFunction(); //get number User premium from current day
        }
        return usersRepository.countNormalUserByFunction(); //normal user
    }

    public Long countAllUser() {
        return usersRepository.count(); //count all user
    }
}
