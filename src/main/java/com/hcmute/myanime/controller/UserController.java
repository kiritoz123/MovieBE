package com.hcmute.myanime.controller;

import com.hcmute.myanime.auth.ApplicationUserService;
import com.hcmute.myanime.dto.*;
import com.hcmute.myanime.mapper.UserMapper;
import com.hcmute.myanime.model.OrderPremiumEntity;
import com.hcmute.myanime.model.UsersEntity;
import com.hcmute.myanime.service.EmailSenderService;
import com.hcmute.myanime.service.OrderPremiumService;
import com.hcmute.myanime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping()
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationUserService applicationUserService;

    //region Mudule Client
    @GetMapping("/admin/user/count")
    public ResponseEntity<?> countUsers(@RequestParam Map<String, String> requestParams)
    {
        String keywordSearch = requestParams.get("keyword");
        return ResponseEntity.ok(new TotalUsersCountDTO(userService.countByUsername(keywordSearch)));
    }

    //region Module Admin
    @GetMapping("/admin/get-all-user")
    public ResponseEntity<?> getAllUSer(@RequestParam Map<String, String> requestParams) {
        String page = requestParams.get("page");
        String limit = requestParams.get("limit");
        String keywordSearch = requestParams.get("keyword");
        List<UsersEntity> usersEntityList = userService.findAll(page, limit, keywordSearch);
        List<UserDTO> userDTOList = new ArrayList<>();
        usersEntityList.forEach((usersEntity -> {
            userDTOList.add(UserMapper.toDto(usersEntity));
        }));
        return ResponseEntity.ok(userDTOList);
    }

    @GetMapping("/admin/get-all-premium-user")
    public ResponseEntity<?> getAllPremiumUSer(@RequestParam Map<String, String> requestParams) {
        String page = requestParams.get("page");
        String limit = requestParams.get("limit");
        String keywordSearch = requestParams.get("keyword");
        List<UsersEntity> usersEntityList = userService.findAllPremiumUser(page, limit, keywordSearch);
        List<UserDTO> userDTOList = new ArrayList<>();
        usersEntityList.forEach((usersEntity -> {
            userDTOList.add(UserMapper.toDto(usersEntity));
        }));
        return ResponseEntity.ok(userDTOList);
    }

    @GetMapping("/admin/get-all-normal-user")
    public ResponseEntity<?> getAllNormalUSer(@RequestParam Map<String, String> requestParams) {
        String page = requestParams.get("page");
        String limit = requestParams.get("limit");
        String keywordSearch = requestParams.get("keyword");
        List<UsersEntity> usersEntityList = userService.findAllNormalUser(page, limit, keywordSearch);
        List<UserDTO> userDTOList = new ArrayList<>();
        usersEntityList.forEach((usersEntity -> {
            userDTOList.add(UserMapper.toDto(usersEntity));
        }));
        return ResponseEntity.ok(userDTOList);
    }

    @PutMapping("/admin/disable-user/{userId}")
    public ResponseEntity<?> disableUserByUserId(@PathVariable int userId) {

        if(userService.disableUserByUserId(userId)) {
            return ResponseEntity.ok(
                    new ResponseDTO(HttpStatus.OK, "Disable user id " + userId + " success")
            );
        } else {
            return ResponseEntity.badRequest().body("Disable user id " + userId + " fail");
        }
    }

    @PutMapping("/admin/enable-user/{userId}")
    public ResponseEntity<?> enableUserByUserId(@PathVariable int userId) {
        if(userService.enableUserByUserId(userId)) {
            return ResponseEntity.ok(
                    new ResponseDTO(HttpStatus.OK, "Enable user id " + userId + " success")
            );
        } else {
            return ResponseEntity.badRequest().body("Enable user id " + userId + " fail");
        }
    }
    //endregion

    //region Module Client
    @PostMapping("/user/avatar/upload")
    public ResponseEntity<?> uploadAvatar(@RequestParam(value = "avatar") MultipartFile avatar) throws IOException {
        if((!avatar.getContentType().equals("image/png") &&
                !avatar.getContentType().equals("image/jpeg")) || avatar.equals(null)) {
            return ResponseEntity.badRequest().body("file extension must be .jpeg or .png");
        }
        String username = applicationUserService.getUsernameLoggedIn();
        if(userService.uploadAvatar(avatar, username)) {
            return ResponseEntity.ok(
                    new ResponseDTO(
                            HttpStatus.OK,
                            "Update avatar user: " + username + " success"
                    )
            );
        } else {
            return ResponseEntity.badRequest().body("Update avatar user: " + username + " fail");
        }
    }

    @GetMapping("/user/user-detail")
    public ResponseEntity<?> findUserLogging()
    {
        UsersEntity usersEntity = userService.findUserLogging();
        UserDTO userDtoLogging = UserMapper.toDto(usersEntity);
        return ResponseEntity.ok(userDtoLogging);
    }
    @PutMapping("/user/user-detail")
    public ResponseEntity<?> updateInfoUserLogging(@RequestBody UserDTO userDTO) {
        ResponseEntity<?> responseEntity = userService.updateInfoUserLogging(userDTO);
        return responseEntity;
    }

    @GetMapping("/user/user-detail/mail/checkOTP/{otpCode}")
    public ResponseEntity<?> checkUserMailOTP(@PathVariable String otpCode) {
        ResponseEntity<?> responseEntity = userService.checkUserMailOTPCode(otpCode);
        return responseEntity;
    }

    // Premium member
    @GetMapping("user/user-detail/premium/check")
    public ResponseEntity<?> checkUserIsPremium()
    {
        if(userService.isPremiumMember()) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("user/user-detail/premium/remain")
    public ResponseEntity<?> checkRemainPremium()
    {
        return ResponseEntity.ok(userService.remainPremium());
    }

    @GetMapping("user/user-detail/premium/history")
    public ResponseEntity<?> getHistoryPremium()
    {
        return ResponseEntity.ok(userService.getHistoryPremium());
    }


    // Action này không được sử dụng bởi Client
    @PostMapping("user/user-detail/premium/package/{packageId}")
    public ResponseEntity<?> createPremium(@PathVariable int packageId)
    {
        if(userService.createPremium(packageId)) {
            return ResponseEntity.ok(
                    new ResponseDTO(HttpStatus.OK, "subcription premium success")
            );
        } else {
            return ResponseEntity.badRequest().body("subcription premium fail");
        }
    }

    @PostMapping("account/forget-password")
    public ResponseEntity<?> forgetPassword(@RequestBody Map<String, Object> request)
    {
        StringBuilder message = new StringBuilder();

        if(userService.requestSendEmailForgetPassword(request, message)) {
            return ResponseEntity.ok(
                    new ResponseDTO(HttpStatus.OK, message.toString())
            );
        } else {
            return ResponseEntity.badRequest().body(message.toString());
        }
    }

    @PostMapping("account/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, Object> request, HttpServletRequest httpServletRequest)
    {
        StringBuilder message = new StringBuilder();

        if(userService.requestResetPassword(request, httpServletRequest.getRemoteAddr(), message)) {
            return ResponseEntity.ok(
                    new ResponseDTO(HttpStatus.OK, message.toString())
            );
        } else {
            return ResponseEntity.badRequest().body(message.toString());
        }
    }

    // Create order premium
    @Autowired
    private OrderPremiumService orderPremiumService;
    @PostMapping("user/premium/create/order")
    public ResponseEntity<?> requestOrderPremium(@RequestBody Map<String, Object> request, HttpServletRequest httpServletRequest)
    {
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(httpServletRequest)
                .replacePath(null)
                .build()
                .toUriString();
        baseUrl += "/payment/premium/pay/order/";

        StringBuilder message = new StringBuilder();
        int orderPremiumId = orderPremiumService.Create(request, message);
        if(orderPremiumId >= 0) {
            return ResponseEntity.ok(baseUrl + orderPremiumId);
        } else {
            return ResponseEntity.badRequest().body(message.toString());
        }
    }

    @PostMapping("user/premium/cancel/order/{orderPremiumId}")
    public ResponseEntity<?> requestOrderPremium(@PathVariable int orderPremiumId)
    {
        StringBuilder message = new StringBuilder();
        if(orderPremiumService.Cancel(orderPremiumId, message)) {
            return ResponseEntity.ok(
                    new ResponseDTO(HttpStatus.OK, message.toString())
            );
        } else {
            return ResponseEntity.badRequest().body(message.toString());
        }
    }

    @GetMapping("payment/premium/pay/order/{orderPremiumId}")
    public ResponseEntity<?> payOrderPremium(@PathVariable int orderPremiumId, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException
    {
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(httpServletRequest)
                .replacePath(null)
                .build()
                .toUriString();
        baseUrl += "/payment/paypal/";

        System.out.println(baseUrl);

        StringBuilder message = new StringBuilder();
        if(orderPremiumService.CreatePay(orderPremiumId, baseUrl, message)) {
            response.sendRedirect(message.toString());
            return ResponseEntity.ok(
                    new ResponseDTO(HttpStatus.OK, message.toString())
            );
        } else {
            return ResponseEntity.badRequest().body(message.toString());
        }
    }

    //endregion
}
