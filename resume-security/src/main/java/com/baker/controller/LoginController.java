package com.baker.controller;

import com.baker.common.ResponseResult;
import com.baker.domain.FormCheck;
import com.baker.domain.User;
import com.baker.service.LoginServcie;
import com.baker.service.impl.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

    @Resource
    private LoginServiceImpl loginServcie;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        return loginServcie.login(user);
    }

    @GetMapping("/user/logout")
    public ResponseResult logout(){
        return loginServcie.logout();
    }


}
