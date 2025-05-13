package com.hcmute.myanime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.TimeZone;
import java.util.Date;

@SpringBootApplication
@EnableAsync
public class MyAnimeApplication {
    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));   // It will set UTC timezone
        System.out.println("Spring boot application running in Asia/Ho_Chi_Minh timezone :"+ new Date());   // It will print Asia/Ho_Chi_Minh timezone
    }

    public static void main(String[] args) {
        SpringApplication.run(MyAnimeApplication.class, args);
    }
}
