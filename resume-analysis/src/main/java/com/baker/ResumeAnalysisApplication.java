package com.baker;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.baker.mapper")
public class ResumeAnalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResumeAnalysisApplication.class, args);
    }

}
