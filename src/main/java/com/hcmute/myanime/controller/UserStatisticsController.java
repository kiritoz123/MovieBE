package com.hcmute.myanime.controller;

import com.hcmute.myanime.common.GlobalVariable;
import com.hcmute.myanime.dto.StatisticsNumberUserDTO;
import com.hcmute.myanime.model.UserPremiumEntity;
import com.hcmute.myanime.repository.UserPremiumRepository;
import com.hcmute.myanime.service.UserStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("statistics/user")
public class UserStatisticsController {
    @Autowired
    private UserStatisticService userStatisticService;


    @GetMapping("/get-number-of-user")
    public ResponseEntity<?> getNumberOfUser()
    {
        int numberOfNormalUser = userStatisticService.getNumberOfUser(GlobalVariable.NORMAL_USER);
        int numberOfPremiumUser = userStatisticService.getNumberOfUser(GlobalVariable.PREMIUM_USER);
        Long allNumberOfUser = userStatisticService.countAllUser();
        StatisticsNumberUserDTO statisticsNumberUserDTO = new StatisticsNumberUserDTO(
                numberOfPremiumUser, numberOfNormalUser, allNumberOfUser
        );
        return ResponseEntity.ok(statisticsNumberUserDTO);
    }
}
