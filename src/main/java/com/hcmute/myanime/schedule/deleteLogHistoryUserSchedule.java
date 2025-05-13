package com.hcmute.myanime.schedule;

import com.hcmute.myanime.service.LogHistoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class deleteLogHistoryUserSchedule {
    @Autowired
    private LogHistoriesService logHistoriesService;

    @Scheduled(cron = "0 6 */30 * * ?")
    public void scheduleTaskUsingCronExpression() {
//        System.out.println("This is crone job");
        logHistoriesService.deleteAfterDay(30);
    }
}
