package com.baker.controller;

import com.baker.common.ResponseResult;
import com.baker.domain.FormCheck;
import com.baker.domain.User;
import com.baker.domain.UserAndForm;
import com.baker.service.LoginServcie;
import com.baker.service.impl.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

    UserAndForm data = new UserAndForm();

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

    @PostMapping("/user/beforeLogin")
    public ResponseResult before(@RequestBody FormCheck formCheck){
         data = (UserAndForm) loginServcie.beforeLogin(formCheck.getPhone()).getData();
         return ResponseResult.ok("验证码发送成功");
    }

    @PostMapping("/user/doLogin")
    public ResponseResult doLogin(@RequestBody FormCheck formCheck){
        return loginServcie.doLogin(data,formCheck);
    }

}
