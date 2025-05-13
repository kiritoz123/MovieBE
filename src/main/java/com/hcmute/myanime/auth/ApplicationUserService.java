package com.hcmute.myanime.auth;

import com.hcmute.myanime.common.GlobalVariable;
import com.hcmute.myanime.dto.UserDTO;
import com.hcmute.myanime.exception.BadRequestException;
import com.hcmute.myanime.mapper.UserMapper;
import com.hcmute.myanime.model.UsersEntity;
import com.hcmute.myanime.repository.RolesRepository;
import com.hcmute.myanime.repository.UsersRepository;
import com.hcmute.myanime.security.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.*;

@Service
@Transactional
public class ApplicationUserService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UsersEntity> user = usersRepository.findByUsername(username);

        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));

        return user.map(ApplicationUser::new).get();
    }

    public String getUsernameLoggedIn() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof ApplicationUser) {
            username = ((ApplicationUser)principal).getUsername();
        } else {
            username = principal.toString();
        }

        return username;
    }

    public Boolean save(UserDTO userDTO) {
        Optional<UsersEntity> usersEntityOptional = usersRepository.findByUsername(userDTO.getUsername());
        if(usersEntityOptional.isPresent()){
            throw new BadRequestException("username not found");
        }
        UsersEntity newUser = UserMapper.toEntity(userDTO);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setUserRoleByUserRoleId(rolesRepository.findByName("ROLE_" + ApplicationUserRole.USER.name()).get());
        newUser.setCreateAt(new Timestamp(System.currentTimeMillis()));
        newUser.setFullName("Guest#" + GlobalVariable.GetOTP());
        try {
            usersRepository.save(newUser);
        } catch (Exception exception) {
            return false;
        }
        return true;
    }
}
