package com.baker.Service.Impl;

import com.baker.Service.AcountService;
import com.baker.common.ResponseResult;
import com.baker.domain.LoginUser;
import com.baker.domain.User;
import com.baker.service.UserService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @Description:
 * @ClassName: AcountServiceImpl
 * @Author: luodeng
 * @Date: 2023/5/17 18:30
 * @Version: 1.0
 */
@Service
public class AcountServiceImpl implements AcountService {

    @Autowired
    private UserService userService;


    @Override
    public ResponseResult resetPassword(User user) {
        User user1 = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        String newPassword = user.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //将密码加密
        String password = passwordEncoder.encode(newPassword);

        user1.setPassword(password);
        userService.updateById(user1);

        return ResponseResult.ok("修改密码成功");
    }

    @Override
    public ResponseResult updateAllInfo(User user) {
        User user1 = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        //修改用户基本信息 （用户名、昵称、邮箱、性别）
        user1.setUserName(user.getUserName());
        user1.setNickName(user.getNickName());
        user1.setEmail(user.getEmail());
        user1.setSex(user.getSex());
        userService.updateById(user1);
        return ResponseResult.ok("修改个人信息成功");
    }

    //上传头像
    @Override
    public ResponseResult resetAvatar(byte[] data) {
        User user1 = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        user1.setAvatar(data);
        userService.updateById(user1);

        return ResponseResult.ok("头像上传成功");
    }


    //获取用户最新头像
    @Override
    public ResponseResult getAvatar() {
        User user1 = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        byte[] avatarData = user1.getAvatar();
        String base64Avatar = Base64.getEncoder().encodeToString(avatarData);
        return ResponseResult.ok(base64Avatar);
    }



}
