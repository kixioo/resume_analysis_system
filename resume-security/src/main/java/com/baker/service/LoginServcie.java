package com.baker.service;

import com.baker.common.ResponseResult;
import com.baker.domain.User;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @ClassName: LoginServcie
 * @Author: luodeng
 * @Date: 2023/4/14 17:41
 * @Version: 1.0
 */
public interface LoginServcie {
    public ResponseResult login(User user);
    public ResponseResult logout();
}
