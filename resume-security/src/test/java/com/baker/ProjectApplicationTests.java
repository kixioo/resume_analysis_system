package com.baker;

import com.baker.domain.User;
import com.baker.mapper.MenuMapper;
import com.baker.mapper.UserMapper;
import com.baker.service.SendMessage;
import com.baker.until.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.List;

@SpringBootTest
class ProjectApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private SendMessage sendMessage;
    @Test
    public void testUserMapper(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    @Test
    public void Test1(){
        try {
            Claims claims = JwtUtil.parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJlZDIyZGIzNjk4OGI0NjYxOTY5MjQyODgxYzJiZWExMyIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY4MTczNDgzNCwiZXhwIjoxNjgxNzM4NDM0fQ.XOCxnxwrwPQxvmuKYvTuuu1JZ75URdVOPJSg7cKvh-g");
            String subject = claims.getSubject();
            System.out.println(subject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @Test
    public void TestBCryptPasswordEncoder(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("1234");
        System.out.println(password);
    }

    @Test
    public void testMessageNum(){
        int a = (int)(Math.random()*(9999-1000+1))+1000;//生成随机四位数
        String num = Integer.toString(a);
        String data = "{\"code\":\""+num+"\"}";
        System.out.println(data);
    }

    @Test
    public void testSendMessage(){
        int a = (int)(Math.random()*(9999-1000+1))+1000;
        String temp = Integer.toString(a);
        String data = "{\"code\":\"1111\"}";
        String phone1 = "18251928140";
        String phone = "18812711512";
        try {
            sendMessage.DoSend(phone1,data);
            System.out.println("发送成功");
        } catch (Exception e) {
            throw new RuntimeException("短信发送失败");
        }
    }
    @Test
    public void testSelectPerms(){
        List<String> strings = menuMapper.selectPermsByUserId(1L);
        System.out.println(strings);
    }
}
