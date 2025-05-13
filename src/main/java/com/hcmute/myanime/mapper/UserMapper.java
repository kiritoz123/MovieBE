package com.hcmute.myanime.mapper;

import com.hcmute.myanime.dto.UserDTO;
import com.hcmute.myanime.model.UsersEntity;

public class UserMapper {

    public static UsersEntity toEntity (UserDTO userDTO){
        UsersEntity mappedUserEntity = new UsersEntity();
        mappedUserEntity.setId(userDTO.getId());
        mappedUserEntity.setUsername(userDTO.getUsername());
        mappedUserEntity.setPassword(userDTO.getPassword());
        mappedUserEntity.setFullName(userDTO.getFullName());
        mappedUserEntity.setEmail(userDTO.getEmail());
        mappedUserEntity.setAvatar(userDTO.getAvatar());
        mappedUserEntity.setCreateAt(userDTO.getCreateAt());
        return mappedUserEntity;
    }

    public static UserDTO toDto (UsersEntity userEntity){
        UserDTO mappedUserDto = new UserDTO();
        mappedUserDto.setId(userEntity.getId());
        mappedUserDto.setUsername(userEntity.getUsername());
//        mappedUserDto.setPassword(userEntity.getPassword());
        mappedUserDto.setFullName(userEntity.getFullName());
        mappedUserDto.setEmail(userEntity.getEmail());
        mappedUserDto.setAvatar(userEntity.getAvatar());
        mappedUserDto.setCreateAt(userEntity.getCreateAt());
        mappedUserDto.setEnable(userEntity.getEnable());
        return mappedUserDto;
    }
}
