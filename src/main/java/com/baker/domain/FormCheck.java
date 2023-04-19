package com.baker.domain;

import lombok.Data;

/**
 * @Description:
 * @ClassName: SignupUser
 * @Author: luodeng
 * @Date: 2023/4/19 11:06
 * @Version: 1.0
 */
@Data
public class FormCheck {
    private String phone;
    private String password;
    private String code;

}
