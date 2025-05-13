package com.hcmute.myanime.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", "dlmo3dq0p");
        config.put("api_key", "516191915913178");
        config.put("api_secret", "1fJgd3WbqitMHOXMDU3HiDPwqLU");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }
}
