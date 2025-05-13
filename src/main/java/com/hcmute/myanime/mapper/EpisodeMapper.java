package com.hcmute.myanime.mapper;

import com.hcmute.myanime.dto.EpisodeDTO;
import com.hcmute.myanime.model.EpisodeEntity;

public class EpisodeMapper {
    public static EpisodeDTO toDTO (EpisodeEntity episodeEntity)
    {
        EpisodeDTO episodeDTO = new EpisodeDTO(
                episodeEntity.getId(),
                episodeEntity.getCreateAt(),
                episodeEntity.getResource(),
                episodeEntity.getResourceDo(),
                episodeEntity.getTitle(),
                episodeEntity.getPremiumRequired(),
                episodeEntity.getNumEpisodes()
        );
        return  episodeDTO;
    }
}
