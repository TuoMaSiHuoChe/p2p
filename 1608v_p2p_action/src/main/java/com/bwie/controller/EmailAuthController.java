package com.bwie.controller;

import com.bwie.cache.BaseCacheService;
import com.bwie.pojo.UserModel;
import com.bwie.service.UserService;
import com.bwie.until.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author: admin
 * @Date: 2019/5/16 10:41
 * @Description:
 */
@RestController
@RequestMapping("emailAuth")
public class EmailAuthController {

    @Autowired
    private BaseCacheService redisCache;
    @Autowired
    private UserService userService;
    @Autowired
    private AliyunUtils aliyunUtils;

    @Autowired
    private EmailUtils emailUtils;

    @RequestMapping("auth")
    public Response auth(@RequestBody UserModel userModel) throws Exception {
        if (userModel == null) {
            // 没有此用户
            return Response.build().setStatus("15");
        }
        if (0 != userModel.getId() && null != userModel.getUsername() && "" != userModel.getUsername()
                && userModel.getEmail() != null && "" != userModel.getEmail()) {
            String title = "P2P邮箱激活";
            String encrypt = SecretUtil.encrypt(userModel.getId() + "");
            boolean checkEmail = EmailUtils.checkEmail(userModel.getEmail());
            UserModel userModel1 = userService.findByEmail(userModel.getEmail());
            if (userModel1 == null) {
                if (checkEmail) {
                    try {
                        emailUtils.sendEmail(userModel.getEmail(), title, encrypt, userModel.getUsername());
                        return Response.build().setStatus(FrontStatusConstants.SUCCESS);
                    } catch (IOException e) {
                        System.out.println("邮件发送失败");
                    }
                }
            } else {
                System.out.println("邮箱已经存在!");
                return Response.build().setStatus("301").setMessage("邮箱已经存在！");
            }
        }
        return Response.build().setStatus("15").setMessage("用户不存在！");
    }
}
