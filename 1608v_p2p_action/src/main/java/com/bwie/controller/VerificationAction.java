package com.bwie.controller;

import com.bwie.cache.BaseCacheService;
import com.bwie.service.UserService;
import com.bwie.until.AliyunUtils;
import com.bwie.until.FrontStatusConstants;
import com.bwie.until.GetHttpResponseHeader;
import com.bwie.until.Response;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: admin
 * @Date: 2019/5/16 08:42
 * @Description:
 */
@RestController
@RequestMapping("verification")
public class VerificationAction {

    @Autowired
    private BaseCacheService redisCache;
    @Autowired
    private UserService userService;
    @Autowired
    private AliyunUtils aliyunUtils;

    @RequestMapping("verifiRealName")
    public Response sendMessage(@RequestParam("realName") String realName, @RequestParam("identity") String identity, HttpServletRequest request) {

        String token = GetHttpResponseHeader.getHeadersInfo(request);
        if (org.apache.commons.lang.StringUtils.isEmpty(token)) {
            return Response.build().setStatus(FrontStatusConstants.NULL_TOKEN);
        }
        Map<String, Object> hmap = redisCache.getHmap(token);
        if (hmap == null || hmap.size() == 0) {
            // 用户未登录
            return Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN);
        }
        /*
            ############################对比
         */
        userService.updateRealName(realName,identity, (int)hmap.get("id"));
        return Response.build().setStatus(FrontStatusConstants.SUCCESS);
    }
    @RequestMapping("sendMessage")
    public Response sendMessage(@RequestParam("phone") String phone) {
        String s = RandomStringUtils.randomNumeric(6);
        System.out.println("验证码是：" + s);
        aliyunUtils.sendSms(phone, s);
        redisCache.set(phone, s);
        redisCache.expire(phone, 3 * 60);
        return Response.build().setStatus(FrontStatusConstants.SUCCESS);
    }



}
