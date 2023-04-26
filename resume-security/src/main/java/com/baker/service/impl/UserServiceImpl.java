package com.baker.service.impl;

import com.baker.domain.User;
import com.baker.mapper.UserMapper;
import com.baker.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @ClassName: UserServiceImpl
 * @Author: luodeng
 * @Date: 2023/4/18 20:57
 * @Version: 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
