package com.hcmute.myanime.mapper;

import com.hcmute.myanime.dto.GiftcodeDTO;
import com.hcmute.myanime.model.GiftCodeEntity;

public class GiftcodeMapper {
    public static GiftcodeDTO toDTO(GiftCodeEntity giftCodeEntity)
    {
        return new GiftcodeDTO(
                giftCodeEntity.getId(),
                giftCodeEntity.getRedemptionCode(),
                giftCodeEntity.getCreateAt()
        );
    }
}
