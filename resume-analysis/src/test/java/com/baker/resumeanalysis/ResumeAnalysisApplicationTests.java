package com.baker.resumeanalysis;

import com.baker.domain.User;
import com.baker.pojo.ResumeInformation;
import com.baker.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;

@SpringBootTest
class ResumeAnalysisApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void t1(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String password = passwordEncoder.encode("1234");
        boolean f = passwordEncoder.matches("luodeng1112","$2a$10$YpIGCxxtFuSqJt3i0QZTgOgvwUKydaHEJejt.idMhJ8BbhFN7PNzO");
        System.out.println(f);
    }

    @Test
    public void t2(){
        String phone = "18583155673";

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        wrapper.eq(User::getPhoneNumber, phone);
        User one = userService.getOne(wrapper);
        byte[] avatarData = one.getAvatar();
        String base64Avatar = Base64.getEncoder().encodeToString(avatarData);
        System.out.println(base64Avatar);

    }

}
