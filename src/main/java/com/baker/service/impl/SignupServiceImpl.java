package com.baker.service.impl;

import com.baker.common.ResponseResult;
import com.baker.domain.FormCheck;
import com.baker.domain.User;
import com.baker.service.SendMessage;
import com.baker.service.SignupService;
import com.baker.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Description:
 * @ClassName: SignupServiceImpl
 * @Author: luodeng
 * @Date: 2023/4/18 20:44
 * @Version: 1.0
 */
@Service
public class SignupServiceImpl implements SignupService {

    @Autowired
    private UserService userService;

    @Autowired
    private SendMessage sendMessage;

    @Override
    public ResponseResult doSignup(FormCheck formCheck,String phone,String code) {

        if(!formCheck.getPhone().equals(phone)){
            return new ResponseResult(404,"手机号不一致，请重新输入");
        }

        if(!formCheck.getCode().equals(code)){
            return new ResponseResult(404,"验证码错误");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(formCheck.getPassword());

        int a = (int)(Math.random()*(9999-1000+1))+1000;
        String temp = Integer.toString(a);
        User newUser = new User();
        String newUser_name="user_"+temp;
        newUser.setUserName(newUser_name);
        newUser.setPassword(password);
        newUser.setPhonenumber(formCheck.getPhone());

        userService.save(newUser);
        return new ResponseResult(200,"注册成功");


    }

    @Override
    public ResponseResult formCheck(String phone,String code) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        wrapper.eq(User::getPhonenumber, phone);
        User one = userService.getOne(wrapper);
        if(!Objects.isNull(one)){
            return new ResponseResult(404,"该手机号已被注册");
        }

        String data = "{\"code\":\""+code+"\"}";
        try {
            sendMessage.DoSend(phone,data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return new ResponseResult(200,"短信发送成功");
    }

    @Override
    public String code() {
        int a = (int)(Math.random()*(9999-1000+1))+1000;//生成随机四位数
        String code = Integer.toString(a);
        return code;
    }
}
