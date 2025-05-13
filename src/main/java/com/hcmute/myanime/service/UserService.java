package com.hcmute.myanime.service;

import com.hcmute.myanime.auth.ApplicationUserService;
import com.hcmute.myanime.common.GlobalVariable;
import com.hcmute.myanime.config.EmailTemplate;
import com.hcmute.myanime.dto.UserDTO;
import com.hcmute.myanime.exception.BadRequestException;
import com.hcmute.myanime.model.*;
import com.hcmute.myanime.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ApplicationUserService applicationUserService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private EmailConfirmationRepository emailConfirmationRepository;
    @Autowired
    private SubscriptionPackageRepository subscriptionPackageRepository;
    @Autowired
    private UserPremiumRepository userPremiumRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AttemptLogService attemptLogService;

    private Boolean isNumber(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Boolean isValidPage(String page) {
        return page != null && !page.equals("") && isNumber(page) && Long.parseLong(page) >= 0;
    }

    public Long countByUsername(String keywordSearch) {
        Long totalUsers;
        if(keywordSearch != null) {
            totalUsers = usersRepository.countByUsernameContaining(keywordSearch);
        } else {
            totalUsers = usersRepository.count();
        }
        return totalUsers;
    }

    public List<UsersEntity> findAll(String page, String limit, String keywordSearch) {
        limit = (limit == null || limit.equals("")
                || !isNumber(limit) || Long.parseLong(limit) < 0) ? GlobalVariable.DEFAULT_LIMIT : limit;

        page = (!isValidPage(page)) ? GlobalVariable.DEFAULT_PAGE : page;
        Pageable pageable = PageRequest.of((Integer.parseInt(page) - 1), Integer.parseInt(limit));

        List<UsersEntity> usersEntityList = new ArrayList<>();
        if(keywordSearch != null) {
            usersEntityList = usersRepository.findByUsernameContaining(keywordSearch,pageable);
        }
        else{
            usersEntityList = usersRepository
                    .findAllByStoredProcedures(Integer.parseInt(page), Integer.parseInt(limit)); //Use stored procedures
        }

        return usersEntityList;
    }

    public List<UsersEntity> findAllPremiumUser(String page, String limit, String keywordSearch) {
        limit = (limit == null || limit.equals("")
                || !isNumber(limit) || Long.parseLong(limit) < 0) ? GlobalVariable.DEFAULT_LIMIT : limit;

        page = (!isValidPage(page)) ? GlobalVariable.DEFAULT_PAGE : page;
        Pageable pageable = PageRequest.of((Integer.parseInt(page)), Integer.parseInt(limit));

        List<UsersEntity> usersEntityList = new ArrayList<>();
        if(keywordSearch != null) {
            usersEntityList = usersRepository.findAllUserPremiumByStoredProcedures(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    keywordSearch
            );
        }
        else{
            usersEntityList = usersRepository
                    .findAllUserPremiumByStoredProcedures(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            ""
                    ); //Use stored procedures
        }

        return usersEntityList;
    }

    public List<UsersEntity> findAllNormalUser(String page, String limit, String keywordSearch) {
        limit = (limit == null || limit.equals("")
                || !isNumber(limit) || Long.parseLong(limit) < 0) ? GlobalVariable.DEFAULT_LIMIT : limit;

        page = (!isValidPage(page)) ? GlobalVariable.DEFAULT_PAGE : page;
        Pageable pageable = PageRequest.of((Integer.parseInt(page)), Integer.parseInt(limit));

        List<UsersEntity> usersEntityList = new ArrayList<>();
        if(keywordSearch != null) {
            usersEntityList = usersRepository.findAllNormalUserByStoredProcedures(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    keywordSearch
            );
        }
        else{
            usersEntityList = usersRepository
                    .findAllNormalUserByStoredProcedures(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            ""
                    ); //Use stored procedures
        }

        return usersEntityList;
    }

    public UsersEntity findByUsername(String username) {
        Optional<UsersEntity> usersEntityOptional = usersRepository.findByUsername(username);
        if(!usersEntityOptional.isPresent()) {
            return null;
        }
        UsersEntity usersEntity = usersEntityOptional.get();
        return usersEntity;
    }

    public Boolean uploadAvatar(MultipartFile avatar, String username) throws IOException {
        Optional<UsersEntity> usersOptional = usersRepository.findByUsername(username);
        System.out.println(username);
        if (!usersOptional.isPresent()) {
            return false;
        }
        UsersEntity userLogin = usersOptional.get();
        String url = cloudinaryService.uploadFile(
                avatar.getBytes(),
                String.valueOf(userLogin.getId()),
                "MyAnimeProject_TLCN/user/avatar");
        if (url.equals("-1")) {
            return false;
        }
        userLogin.setAvatar(url);
        try {
            usersRepository.save(userLogin);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public UsersEntity findUserLogging() {
        String usernameLoggedIn = applicationUserService.getUsernameLoggedIn();
        Optional<UsersEntity> userByUsername = usersRepository.findByUsername(usernameLoggedIn);
        if (!userByUsername.isPresent()) {
            throw new BadRequestException("User not found");
        }
        UsersEntity usersEntity = userByUsername.get();
        return usersEntity;
    }

    public boolean disableUserByUserId(int userId) {
        Optional<UsersEntity> usersEntityOptional = usersRepository.findById(userId);
        if (!usersEntityOptional.isPresent()) {
            return false;
        }
        UsersEntity userEntity = usersEntityOptional.get();
        userEntity.setEnable(false);
        try {
            usersRepository.save(userEntity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean enableUserByUserId(int userId) {
        Optional<UsersEntity> usersEntityOptional = usersRepository.findById(userId);
        if (!usersEntityOptional.isPresent()) {
            return false;
        }
        UsersEntity userEntity = usersEntityOptional.get();
        userEntity.setEnable(true);
        try {
            usersRepository.save(userEntity);
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    public ResponseEntity<?> updateInfoUserLogging(UserDTO userDTO) {
        String usernameLoggedIn = applicationUserService.getUsernameLoggedIn();
        UsersEntity userEntity = usersRepository.findByUsername(usernameLoggedIn).get();
        String otpCode = GlobalVariable.GetOTP();
        Optional<UsersEntity> usersEntityOptional = usersRepository.findByEmail(userDTO.getEmail());
        if (usersEntityOptional.isPresent()) { //Check if mail has been used in other mail
            if(userEntity.getEmail() == null || !usersEntityOptional.get().equals(userEntity)) {
                return ResponseEntity.badRequest().body("Another account has used this email, please try another email");
            }
        }
        userEntity.setFullName(userDTO.getFullName());
        if(userEntity.getEmail() == null || !userEntity.getEmail().equals(userDTO.getEmail())) {
            System.out.println("change mail");
            try {
                sendRecoveryEmail(userDTO.getEmail(), usernameLoggedIn, otpCode);
                Timestamp expDate = new Timestamp(System.currentTimeMillis());
                expDate.setMinutes(expDate.getMinutes() + 10);
                Timestamp createAt = new Timestamp(System.currentTimeMillis());
                System.out.println(expDate);
                System.out.println(createAt);
                EmailConfirmationEntity emailConfirmationEntity = new
                        EmailConfirmationEntity(
                        otpCode,
                        GlobalVariable.EMAIL_CONFIRMATION_STATUS_PENDING,
                        userDTO.getEmail(),
                        "SetMail",
                        expDate,
                        createAt,
                        userEntity);
                emailConfirmationRepository.save(emailConfirmationEntity);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("This gmail is not valid");
            }
        }

        try {
            usersRepository.save(userEntity);
            return ResponseEntity.ok("Update user info success");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Update user fail");
        }
    }

    public boolean requestSendEmailForgetPassword(Map<String, Object> request, StringBuilder message)
    {
        if(!request.containsKey("email")) {
            message.append("Key email missing");
            return false;
        }

        String email = request.get("email").toString();
        Optional<UsersEntity> usersEntityOptional = usersRepository.findByEmail(email);
        if (!usersEntityOptional.isPresent()) {
            message.append("Email is not found");
            return false;
        }
        UsersEntity usersEntity = usersEntityOptional.get();

        try {
            String otpCode = GlobalVariable.GetOTP();

            sendRecoveryEmail(email, usersEntity.getUsername(), otpCode);
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            Timestamp expiredTime = new Timestamp(currentTime.getTime() + 600*1000);

            String confirmationType = "ForgetPassword";

            // Destroy code old
            List<EmailConfirmationEntity> emailConfirmationEntityListOld = emailConfirmationRepository.findByUsersEntityByUserIdAndExpiredAndStatusPending(confirmationType, usersEntity);
            System.out.println(emailConfirmationEntityListOld.size());
            emailConfirmationRepository.deleteAll(emailConfirmationEntityListOld);

            // Block 5 times reset password on each 10 minutes
//            List<EmailConfirmationEntity> emailConfirmationEntityList = emailConfirmationRepository.findByConfirmationTypeAndUserEntity(confirmationType, usersEntity);
//            System.out.println(emailConfirmationEntityList.size());

            EmailConfirmationEntity emailConfirmationEntity = new
                    EmailConfirmationEntity(
                    otpCode,
                    GlobalVariable.EMAIL_CONFIRMATION_STATUS_PENDING,
                    email,
                    confirmationType,
                    expiredTime,
                    currentTime,
                    usersEntity);
            emailConfirmationRepository.save(emailConfirmationEntity);
            message.append("Send email forgot password success");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            message.append("Send email forgot password error for exception");
            return false;
        }
    }

    @Transactional
    public boolean requestResetPassword(Map<String, Object> request, String ipClient, StringBuilder message)
    {
        if(!request.containsKey("email")) {
            message.append("Key email missing");
            return false;
        } else if (!request.containsKey("code_confirmation")) {
            message.append("Key code_confirmation missing");
            return false;
        } else if (!request.containsKey("new_password")) {
            message.append("Key new_password missing");
            return false;
        }

        // Check Max Attempt
        if(!attemptLogService.isValid(ipClient, GlobalVariable.ATTEMPT_LOGS_RESET_PASSWORD_FAIL, GlobalVariable.MAX_ATTEMPT_RESET_PASSWORD_ALLOW)) {
            message.append("Max atempt reset password allow. Please try after 10 minutes!");
            return false;
        }

        String email = request.get("email").toString();
        String codeConfirmation = request.get("code_confirmation").toString();
        String newPassword = request.get("new_password").toString();

        // check email is valid user
        Optional<UsersEntity> usersEntityOptional = usersRepository.findByEmail(email);
        if (!usersEntityOptional.isPresent()) {
            message.append("Email is not found");
            return false;
        }
        UsersEntity usersEntity = usersEntityOptional.get();

        // Check email and code confirmation correct and is valid
        Optional<EmailConfirmationEntity> emailConfirmationEntityOptional = emailConfirmationRepository.findByUserAndCodeIsValid(usersEntity, codeConfirmation);
        if(!emailConfirmationEntityOptional.isPresent()) {
            // Reset fail save attempt reset password fail
            attemptLogService.store(ipClient, GlobalVariable.ATTEMPT_LOGS_RESET_PASSWORD_FAIL);

            message.append("Code is not valid");
            return false;
        }

        usersEntity.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(usersEntity);

        EmailConfirmationEntity emailConfirmationEntity = emailConfirmationEntityOptional.get();
        emailConfirmationEntity.setStatus(GlobalVariable.EMAIL_CONFIRMATION_STATUS_USED);
        emailConfirmationRepository.save(emailConfirmationEntity);

        // Destroy Attempt Fail
        attemptLogService.destroy(ipClient, GlobalVariable.ATTEMPT_LOGS_RESET_PASSWORD_FAIL);

        message.append("Update password success");
        return true;
    }

    private void sendRecoveryEmail(String addressGmail, String username, String otpCode) throws MessagingException {
        emailSenderService.sendAsHTML(
                addressGmail,
                "[My anime corporation] You have request for adding new gmail for" + username,
                EmailTemplate.TemplateRecoveryPassword(username, otpCode)
        );
    }

    public ResponseEntity<?> checkUserMailOTPCode(String otpCode) {
        String usernameLoggedIn = applicationUserService.getUsernameLoggedIn();
        UsersEntity userEntity = usersRepository.findByUsername(usernameLoggedIn).get();
        return updateMailFromEmailConfirm(otpCode, userEntity);
    }

    @Transactional
    public ResponseEntity<?> updateMailFromEmailConfirm (String otpCode, UsersEntity userEntity) {
        Optional<EmailConfirmationEntity> emailConfirmationEntityOptional
                = emailConfirmationRepository.findByOtpCodeAndUsersEntityByUserId(otpCode, userEntity);
        if(emailConfirmationEntityOptional.isPresent()) {
            EmailConfirmationEntity userEmailConfirmEntity = emailConfirmationEntityOptional.get();
            Timestamp currentDate = new Timestamp(System.currentTimeMillis());
            if(userEmailConfirmEntity.getExpiredDate().after(currentDate)) {
                userEntity.setEmail(userEmailConfirmEntity.getEmail());
                try {
                    usersRepository.save(userEntity);
                    userEmailConfirmEntity.setStatus(GlobalVariable.EMAIL_CONFIRMATION_STATUS_USED);
                    emailConfirmationRepository.save(userEmailConfirmEntity);
                    return ResponseEntity.ok("Update user mail success");
                } catch (Exception ex) {
                    return ResponseEntity.badRequest().body("Update user mail fail");
                }
            } else {
                return ResponseEntity.badRequest().body("OTP code has been expired");
            }
        } else {
            return ResponseEntity.badRequest().body("OTP code not valid, try again");
        }
    }

    public boolean isPremiumMember()
    {
        // Check user is logged
        String usernameLoggedIn = applicationUserService.getUsernameLoggedIn();
        Optional<UsersEntity> userByUsername = usersRepository.findByUsername(usernameLoggedIn);
        if (!userByUsername.isPresent()) {
            return false;
        }
        // Check record expired
        List<UserPremiumEntity> userPremiumEntityList = userPremiumRepository.findByUserIdAndExpired(userByUsername.get());
        if(userPremiumEntityList.isEmpty())
            return false;

        return true;
    }

    public boolean isPremiumMember(UsersEntity usersEntity)
    {
        // Check record expired
        List<UserPremiumEntity> userPremiumEntityList = userPremiumRepository.findByUserIdAndExpired(usersEntity);
        if(userPremiumEntityList.isEmpty())
            return false;

        return true;
    }

    public long remainPremium()
    {
        if(!this.isPremiumMember())
            return -1;

        // Check user is logged
        String usernameLoggedIn = applicationUserService.getUsernameLoggedIn();
        Optional<UsersEntity> userByUsername = usersRepository.findByUsername(usernameLoggedIn);
        if (!userByUsername.isPresent()) {
            return -1;
        }

        List<Timestamp> list = userPremiumRepository.getTimeRemain(userByUsername.get());
        list.sort((a, b) -> b.compareTo(a));

        Timestamp remainTime = list.get(0);
        long diff = remainTime.getTime() - (new Timestamp(System.currentTimeMillis())).getTime();
        long day = TimeUnit.MILLISECONDS.toDays(diff);
        return day;
    }

    public List<UserPremiumEntity> getHistoryPremium()
    {
        // Check user is logged
        String usernameLoggedIn = applicationUserService.getUsernameLoggedIn();
        Optional<UsersEntity> userByUsername = usersRepository.findByUsername(usernameLoggedIn);
        if (!userByUsername.isPresent()) {
            return null;
        }

        List<UserPremiumEntity> list = userPremiumRepository.findByUsersEntityById(userByUsername.get());
        return list;
    }

    public boolean createPremium(int packageId)
    {
        // Check user is logged
        String usernameLoggedIn = applicationUserService.getUsernameLoggedIn();
        Optional<UsersEntity> userByUsername = usersRepository.findByUsername(usernameLoggedIn);
        if (!userByUsername.isPresent()) {
            return false;
        }

        // Check packageId is valid
        Optional<SubscriptionPackageEntity> subscriptionPackageEntityOptional = subscriptionPackageRepository.findById(packageId);
        if(!subscriptionPackageEntityOptional.isPresent())
            return false;

        UserPremiumEntity userPremiumEntity = new UserPremiumEntity();
        userPremiumEntity.setSubscriptionPackageBySubscriptionPackageId(subscriptionPackageEntityOptional.get());
        userPremiumEntity.setUsersEntityById(userByUsername.get());
        // Check this user have a UserPremium now
        if(this.isPremiumMember()) {
            Timestamp subscribeDate = userPremiumRepository.getExpiredAt(userByUsername.get());
            Timestamp expiredAt = new Timestamp(subscribeDate.getTime() + TimeUnit.DAYS.toMillis(subscriptionPackageEntityOptional.get().getDay()));

            userPremiumEntity.setSubscribeDate(subscribeDate);
            userPremiumEntity.setExpiredAt(expiredAt);

        } else {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            Timestamp expiredAt = new Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(subscriptionPackageEntityOptional.get().getDay()));

            userPremiumEntity.setSubscribeDate(currentTime);
            userPremiumEntity.setExpiredAt(expiredAt);
        }

        userPremiumRepository.save(userPremiumEntity);
        return true;
    }

    public boolean createPremium(int packageId, UsersEntity usersEntity)
    {
        // Check packageId is valid
        Optional<SubscriptionPackageEntity> subscriptionPackageEntityOptional = subscriptionPackageRepository.findById(packageId);
        if(!subscriptionPackageEntityOptional.isPresent())
            return false;

        UserPremiumEntity userPremiumEntity = new UserPremiumEntity();
        userPremiumEntity.setSubscriptionPackageBySubscriptionPackageId(subscriptionPackageEntityOptional.get());
        userPremiumEntity.setUsersEntityById(usersEntity);
        // Check this user have a UserPremium now
        if(this.isPremiumMember(usersEntity)) {
            Timestamp subscribeDate = userPremiumRepository.getExpiredAt(usersEntity);
            Timestamp expiredAt = new Timestamp(subscribeDate.getTime() + TimeUnit.DAYS.toMillis(subscriptionPackageEntityOptional.get().getDay()));

            userPremiumEntity.setSubscribeDate(subscribeDate);
            userPremiumEntity.setExpiredAt(expiredAt);

        } else {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            Timestamp expiredAt = new Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(subscriptionPackageEntityOptional.get().getDay()));

            userPremiumEntity.setSubscribeDate(currentTime);
            userPremiumEntity.setExpiredAt(expiredAt);
        }

        userPremiumRepository.save(userPremiumEntity);
        return true;
    }

    public boolean sendProductInvoiceMailUserLogging(
            String paymentId,
            String itemName,
            String price,
            String quantity,
            String paymentMethod,
            String username
    ) {
        boolean sendMailSuccess = false;
        try {
            UsersEntity usersEntity = findByUsername(username);
            if(usersEntity.getEmail() != null) {
                emailSenderService.sendAsHTML(
                        usersEntity.getEmail(),
                        "[My anime E-Bill] Thanks for your subscribe " + usersEntity.getUsername(),
                        EmailTemplate.TemplateProductInvoice(
                                paymentId,
                                itemName,
                                price,
                                quantity,
                                paymentMethod,
                                usersEntity.getUsername()
                        )
                );
                sendMailSuccess = true;
            }
            return sendMailSuccess;
        } catch (Exception e) {
            return sendMailSuccess;
        }
    }
}
