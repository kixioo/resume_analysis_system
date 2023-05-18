package com.baker.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baker.common.ResponseResult;
import com.baker.domain.FormCheck;
import com.baker.domain.LoginUser;
import com.baker.domain.User;
import com.baker.service.LoginServcie;
import com.baker.service.SendMessage;
import com.baker.service.SignupService;
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
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginServcie {

    private FormCheck formCheck = new FormCheck();

    public FormCheck getFormCheck() {
        return formCheck;
    }

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;
    @Autowired
    private UserService userService;
    @Autowired
    private SignupService signupService;
    @Autowired
    private SendMessage sendMessage;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getPhoneNumber(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            return ResponseResult.fail("用户名或密码错误");
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
        return new ResponseResult<Map<String ,String>>(200,"登陆成功",map);
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        redisCache.deleteObject("login:"+userid);
        return new ResponseResult(200,"退出成功");
    }


}
