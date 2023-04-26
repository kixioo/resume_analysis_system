package com.baker;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.baker.mapper")
public class ResumeSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResumeSecurityApplication.class, args);
    }

}
