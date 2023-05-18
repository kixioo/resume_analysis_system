package com.baker.Controller;

import com.baker.Service.AcountService;
import com.baker.Utils.Utils;
import com.baker.common.ResponseResult;
import com.baker.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Description:
 * @ClassName: AcountController
 * @Author: luodeng
 * @Date: 2023/5/17 18:27
 * @Version: 1.0
 */
@RestController
@RequestMapping("/acount")
public class AcountController {

    @Autowired
    AcountService acountService;

    @PostMapping("/updatePwd")
    public ResponseResult updatePwd(@RequestBody User user) {
        return acountService.resetPassword(user);
    }

    @PostMapping("/updateAll")
    public ResponseResult updateAll(@RequestBody User user){
        return acountService.updateAllInfo(user);
    }

    @PostMapping("/updateAvatar")
    public ResponseResult updateAvatar(@RequestPart("avatar") MultipartFile image) throws IOException {

        return acountService.resetAvatar(Utils.compressAndResize(image));
    }

    @GetMapping("/getAvatar")
    public ResponseResult getAvatar(){
        return acountService.getAvatar();
    }

}
