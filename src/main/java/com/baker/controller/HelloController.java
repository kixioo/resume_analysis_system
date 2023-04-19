package com.baker.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @ClassName: HelloController
 * @Author: luodeng
 * @Date: 2023/4/16 14:09
 * @Version: 1.0
 */
@RestController

public class HelloController {

    @RequestMapping("/what")
    public String what(){
        return "what the fuck";
    }

    @RequestMapping("/hello")
    @PreAuthorize("hasAuthority('system:test:list')")
    public String hello(){
        return "hello little shit";
    }
}
