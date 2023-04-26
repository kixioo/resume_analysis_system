package com.baker.controller;

import com.baker.common.ResponseResult;
import com.baker.domain.FormCheck;
import com.baker.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @ClassName: RegisterController
 * @Author: luodeng
 * @Date: 2023/4/17 19:54
 * @Version: 1.0
 */
@RestController
public class SignupController {

    private String phone;
    private String code;

    @Autowired
    private SignupService signupService;


    @PostMapping("/formcheck")
    public ResponseResult fromcheck(@RequestBody FormCheck formCheck){
        phone = formCheck.getPhone();
        code = signupService.code();
        return signupService.formCheck(phone,code);
    }

    @PostMapping("/signup")
    public ResponseResult Signup(@RequestBody FormCheck formCheck){
        return signupService.doSignup(formCheck,phone,code);
    }
}
