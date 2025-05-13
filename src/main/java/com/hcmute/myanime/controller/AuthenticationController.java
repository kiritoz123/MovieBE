package com.hcmute.myanime.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.hcmute.myanime.auth.ApplicationUser;
import com.hcmute.myanime.auth.ApplicationUserService;
import com.hcmute.myanime.model.RolesEntity;
import com.hcmute.myanime.model.UsersEntity;
import com.hcmute.myanime.repository.RolesRepository;
import com.hcmute.myanime.repository.UsersRepository;
import com.hcmute.myanime.security.ApplicationUserRole;
import com.hcmute.myanime.service.AttemptLogService;
import com.hcmute.myanime.common.GlobalVariable;
import com.hcmute.myanime.dto.AuthenticationRequestDTO;
import com.hcmute.myanime.dto.AuthenticationResponseDTO;
import com.hcmute.myanime.dto.ResponseDTO;
import com.hcmute.myanime.dto.UserDTO;
import com.hcmute.myanime.exception.BadRequestException;
import com.hcmute.myanime.model.AttemptLogEntity;
import com.hcmute.myanime.repository.AttemptLogRepository;
import com.hcmute.myanime.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.json.JsonFactory;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ApplicationUserService applicationUserService;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private AttemptLogService attemptLogService;

    @PostMapping(value = "/register")
    public ResponseEntity<?> saveUser(
            @RequestBody @Valid UserDTO user,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    new ResponseDTO(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage())
            );
        }
        try {
            if (applicationUserService.save(user)) {
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK,
                        "Create user " + user.getUsername() + " success"));
            }
        } catch (BadRequestException badRequestException) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(HttpStatus.BAD_REQUEST, "username is used"));
        }
        return ResponseEntity.badRequest().body(new ResponseDTO(HttpStatus.BAD_REQUEST, "Something when wrong, register fail"));
    }

    @PostMapping("/login")
    public Object authenticationToken(@RequestBody AuthenticationRequestDTO authenticationRequest, HttpServletRequest httpServletRequest) throws Exception {
        String ipClient = httpServletRequest.getRemoteAddr();
        try {

            // Check Attempt
//            if (!attemptLogService.isValid(ipClient, GlobalVariable.ATTEMPT_LOGS_LOGIN_FAIL, GlobalVariable.MAX_ATTEMPT_LOGIN_ALLOW)) {
//                return ResponseEntity.badRequest().body("Max atempt login allow. Please try after 10 minutes!");
//            }

            // Attempt Login
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken
                            (authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );

            // Destroy Attempt Fail
            attemptLogService.destroy(ipClient, GlobalVariable.ATTEMPT_LOGS_LOGIN_FAIL);
        } catch (BadCredentialsException exception) {

            // Login fail save attempt logs fail
            attemptLogService.store(ipClient, GlobalVariable.ATTEMPT_LOGS_LOGIN_FAIL);

            return ResponseEntity.badRequest().body("Username or password is invalid");
        } catch (DisabledException disabledException) {
            return ResponseEntity.badRequest().body("User is disabled");
        }


        final UserDetails userDetails = applicationUserService
                .loadUserByUsername(authenticationRequest.getUsername());
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity
                .ok(new AuthenticationResponseDTO(jwt,
                        authenticationRequest.getUsername(),
                        role));
    }
    @Autowired
    private UsersRepository userRepository;
    @Autowired
    private RolesRepository rolesRepository;

    @PostMapping("/login-google")
    public ResponseEntity<?> loginGoogle(@RequestBody Map<String, String> requestBody) {
        String accessToken = requestBody.get("token");
        if (accessToken == null || accessToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Token is empty or null");
        }

        try {
            // Sử dụng Google UserInfo API để xác thực access_token và lấy thông tin người dùng
            URL url = new URL("https://www.googleapis.com/oauth2/v3/userinfo");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google token: " + responseCode);
            }

            // Đọc phản hồi từ Google API
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            // Parse JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode userInfo = mapper.readTree(content.toString());

            // Lấy thông tin người dùng
            String email = userInfo.get("email").asText();
            boolean emailVerified = userInfo.has("email_verified") ? userInfo.get("email_verified").asBoolean() : false;
            String name = userInfo.has("name") ? userInfo.get("name").asText() : null;

            // Kiểm tra user trong DB
            Optional<UsersEntity> userOptional = userRepository.findByEmail(email);
            UsersEntity newUser = new UsersEntity();
            if (userOptional.isPresent()) {

            } else {
                // Nếu chưa có, tạo user mới
                newUser.setEmail(email);
                newUser.setUsername(email);
                newUser.setUserRoleByUserRoleId(rolesRepository.findByName("ROLE_" + ApplicationUserRole.USER.name()).get());
                newUser.setCreateAt(new Timestamp(System.currentTimeMillis()));
                newUser.setFullName("Guest#" + GlobalVariable.GetOTP());
                userRepository.save(newUser);
            }

            UserDetails userDetails = applicationUserService.loadUserByUsername(email);
            String jwt = jwtTokenUtil.generateToken(userDetails);
            String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();

            return ResponseEntity.ok(new AuthenticationResponseDTO(jwt, email, role));

        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Google login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Google login failed: " + e.getMessage());
        }
    }


}
