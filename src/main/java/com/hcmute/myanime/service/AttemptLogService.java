package com.hcmute.myanime.service;

import com.hcmute.myanime.model.AttemptLogEntity;
import com.hcmute.myanime.repository.AttemptLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AttemptLogService {
    @Autowired
    private AttemptLogRepository attemptLogRepository;

    public boolean store(String ipAddress, String attemptType)
    {
        try {
            AttemptLogEntity attemptLogEntity = new AttemptLogEntity();
            attemptLogEntity.setAttemptType(attemptType);
            attemptLogEntity.setIpAddress(ipAddress);
            attemptLogRepository.save(attemptLogEntity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isValid(String ipAddress, String attemptType, int maxAttemptAllow)
    {
        Optional<Object> totals = attemptLogRepository.getCountWithIpAndAttemptType(ipAddress, attemptType);
        if (totals.isPresent()) {
            long total = (long)totals.get();
            if(total >= maxAttemptAllow)
                return false;
        }
        return true;
    }

    @Transactional
    public void destroy(String ipAddress, String attemptType)
    {
        try {
            attemptLogRepository.deleteByIpAddressAndAttemptType(ipAddress, attemptType);
        } catch (Exception ex) {
        }
    }

}
