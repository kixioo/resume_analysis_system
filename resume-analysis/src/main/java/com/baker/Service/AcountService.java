package com.baker.Service;

import com.baker.common.ResponseResult;
import com.baker.domain.User;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Description:
 * @ClassName: AcountService
 * @Author: luodeng
 * @Date: 2023/5/17 18:27
 * @Version: 1.0
 */
public interface AcountService {
      ResponseResult resetPassword(@RequestBody User user);

      ResponseResult updateAllInfo(@RequestBody User user);

      ResponseResult resetAvatar(byte[] data);

      ResponseResult getAvatar();
}
