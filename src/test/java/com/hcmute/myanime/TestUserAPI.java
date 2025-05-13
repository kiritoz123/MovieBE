package com.hcmute.myanime;

import com.hcmute.myanime.auth.ApplicationUserService;
import com.hcmute.myanime.dto.UserDTO;
import com.hcmute.myanime.model.UsersEntity;
import com.hcmute.myanime.repository.UsersRepository;
import com.hcmute.myanime.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestUserAPI {

    @Autowired
    private ApplicationUserService applicationUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private UsersRepository usersRepository;

    String testUsername = "quachkhanhtest2";
    @Test
    @Order(1)
    public void testCreateUser () {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(testUsername);
        userDTO.setPassword("1234567");
        Boolean saveSuccess = applicationUserService.save(userDTO);
        Assertions.assertEquals(true, saveSuccess);
    }

//    @Test
//    @Order(2)
//    public void testReadAllUser () {
//        List<UsersEntity> usersEntityList = userService.findAll();
//        assertThat(usersEntityList.size(), greaterThan(0));
//    }

    @Test
    @Order(3)
    public void testReadUserByUsername () {
        UsersEntity usersEntity = userService.findByUsername(testUsername);
        Assertions.assertEquals(testUsername, usersEntity.getUsername());
    }

    @Test
    @Order(4)
    public void testUpdateFullNameUser () {
        UsersEntity userEntity = userService.findByUsername(testUsername);
        String currentFullName = userEntity.getFullName();
        userEntity.setFullName("Yukinoshita yukino");
        UsersEntity savedUpdateUser = usersRepository.save(userEntity);
        Assertions.assertNotEquals(currentFullName, savedUpdateUser.getFullName());
        Assertions.assertEquals("Yukinoshita yukino", savedUpdateUser.getFullName());
    }

    @Test
    @Order(5)
    public void testDeleteUser () {
        UsersEntity userEntity = userService.findByUsername(testUsername);
        usersRepository.delete(userEntity);
        Assertions.assertFalse(usersRepository.existsById(userEntity.getId()));
    }
}
