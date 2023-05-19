package com.baker.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baker.common.ResponseResult;
import com.baker.domain.FormCheck;
import com.baker.domain.LoginUser;
import com.baker.domain.User;
import com.baker.domain.UserAndForm;
import com.baker.service.LoginServcie;
import com.baker.service.SendMessage;
import com.baker.service.UserService;
import com.baker.until.JwtUtil;
import com.baker.until.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginServcie {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;
    @Autowired
    private UserService userService;
    @Autowired
    private SendMessage sendMessage;
    @Autowired
    private SignupServiceImpl signupService;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getPhoneNumber(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            System.out.println("密码错误");
            throw new RuntimeException("用户名或密码错误");
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //authenticate存入redis
        var s = JSON.toJSONString(loginUser);
        redisCache.setCacheObject("login:"+userId,s);
        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return new ResponseResult(200,"登陆成功",map);
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        redisCache.deleteObject("login:"+userid);
        return new ResponseResult(200,"退出成功");
    }

    @Override
    public ResponseResult beforeLogin(String phone) {
        UserAndForm userAndForm = new UserAndForm();

        FormCheck formCheck1 = new FormCheck();
        formCheck1.setPhone(phone);

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhoneNumber,phone);
        User one = userService.getOne(wrapper);
        if(Objects.isNull(one)){
            return ResponseResult.fail("该手机号未注册");
        }
        String code = signupService.code();
        formCheck1.setCode(code);

        userAndForm.setFormCheck(formCheck1);
        userAndForm.setUser(one);

        String data = "{\"code\":\""+code+"\"}";
        try {
            sendMessage.DoSend(phone,data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseResult.ok(userAndForm);
    }

    @Override
    public ResponseResult doLogin(UserAndForm before, FormCheck after) {
        if(!(before.getFormCheck().getCode().equals(after.getCode()))){
            return ResponseResult.fail("验证码有误，请重新验证");
        }
        String userId = before.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        LoginUser loginUser = new LoginUser();
        loginUser.setUser(before.getUser());
        //authenticate存入redis
        var s = JSON.toJSONString(loginUser);
        redisCache.setCacheObject("login:"+userId,s);
        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return new ResponseResult(200,"登陆成功",map);
    }


}
