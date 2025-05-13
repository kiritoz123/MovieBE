package com.hcmute.myanime.mapper;

import com.hcmute.myanime.dto.CommentUserDTO;
import com.hcmute.myanime.model.CommentEntity;

public class CommentUserMapper {
    public static CommentUserDTO toDTO(CommentEntity commentEntity)
    {
        CommentUserDTO commentUserDTO = new CommentUserDTO(
                commentEntity.getId(),
                commentEntity.getContent(),
                commentEntity.getCreateAt(),
                commentEntity.getEpisodeByEpisodeId().getId(),
                commentEntity.getUsersByUserId().getUsername(),
                commentEntity.getUsersByUserId().getAvatar()
        );
        return commentUserDTO;
    }
}
