package com.baker.service;

import com.baker.common.ResponseResult;
import com.baker.domain.FormCheck;


/**
 * @Description:
 * @ClassName: SignupService
 * @Author: luodeng
 * @Date: 2023/4/18 20:44
 * @Version: 1.0
 */
public interface SignupService {



    ResponseResult doSignup(FormCheck formCheck,String phone,String code);

     ResponseResult formCheck(String phone,String data);

     String code();
}
