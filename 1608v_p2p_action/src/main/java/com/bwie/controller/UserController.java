package com.bwie.controller;

import com.bwie.cache.BaseCacheService;
import com.bwie.pojo.UserModel;
import com.bwie.service.UserAccountService;
import com.bwie.service.UserService;
import com.bwie.until.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author: admin
 * @Date: 2019/5/14 15:16
 * @Description:
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private BaseCacheService redisCache;

    @Autowired
    private UserAccountService userAccountService;

    @RequestMapping("uuid")
    public Response uuid() {
        String s = UUID.randomUUID().toString();
        redisCache.set(s, s);
        redisCache.expire(s, 3 * 60);
        return Response.build().setStatus(FrontStatusConstants.SUCCESS).setUuid(s);
    }

    @RequestMapping("validateUserName")
    public Response validateUserName(@RequestParam("username") String username) {
        UserModel userModel = userService.validateUserName(username);
        if (userModel == null) {
            return Response.build().setStatus(FrontStatusConstants.SUCCESS);
        } else {
            return Response.build().setStatus(FrontStatusConstants.ALREADY_EXIST_OF_OPTION_NAME);
        }
    }

    @RequestMapping("validatePhone")
    public Response validatePhone(@RequestParam("phone") String phone) {
        UserModel userModel = userService.validatePhone(phone);
        if (userModel == null) {
            return Response.build().setStatus(FrontStatusConstants.SUCCESS);
        } else {
            return Response.build().setStatus(FrontStatusConstants.ALREADY_EXIST_OF_OPTION_NAME);
        }
    }

    @RequestMapping("validateAuthPhone")
    public Response validateAuthPhone(@RequestParam("phone") String phone, HttpServletRequest request) {
        String token = GetHttpResponseHeader.getHeadersInfo(request);
        if (!StringUtils.isEmpty(token)) {
            Map<String, Object> hmap = redisCache.getHmap(token);
            if (hmap != null || hmap.size() == 0) {
                return Response.build().setStatus(FrontStatusConstants.SUCCESS);
            }
        }
        UserModel userModel = userService.validatePhone(phone);
        if (userModel == null) {
            return Response.build().setStatus(FrontStatusConstants.SUCCESS);
        } else {
            return Response.build().setStatus(FrontStatusConstants.ALREADY_EXIST_OF_OPTION_NAME);
        }

    }


    @RequestMapping("codeValidate")
    public Response codeValidate(@RequestParam("signUuid") String signUuid, @RequestParam("signCode") String signCode) {
        String s = redisCache.get(signUuid);
        if (s.isEmpty()) {
            return Response.build().setStatus(FrontStatusConstants.EROO).setMessage("超时了");
        }
        if (s.equalsIgnoreCase(signCode)) {
            return Response.build().setStatus(FrontStatusConstants.SUCCESS);
        } else {
            return Response.build().setStatus(FrontStatusConstants.INPUT_ERROR_OF_VALIDATE_CARD);
        }
    }

    @RequestMapping("signup")
    @Transactional
    public Response signup(@RequestBody UserModel userModel) {
        if (StringUtils.isEmpty(userModel.getSignUuid())) {
            return Response.build().setStatus(FrontStatusConstants.NULL_TOKEN);
        }
        if (StringUtils.isEmpty(userModel.getSignCode())) {
            return Response.build().setStatus(FrontStatusConstants.NULL_OF_VALIDATE_CARD);
        }
        // 根据uuid我们可以在redis中获取图片验证码
        String _signCode = redisCache.get(userModel.getSignUuid());
        if (StringUtils.isEmpty(_signCode)) {
            return Response.build().setStatus(FrontStatusConstants.NULL_OF_VALIDATE_CARD);
        }

        if (!_signCode.equalsIgnoreCase(userModel.getSignCode())) {
            return Response.build().setStatus(FrontStatusConstants.INPUT_ERROR_OF_VALIDATE_CARD);
        }
        String pwd = MD5Util.md5(userModel.getUsername().trim() + userModel.getPassword().trim().toLowerCase());
        userModel.setPassword(pwd);
        boolean flag = userService.addUser(userModel);
        // 添加用户成功
        if (flag) {
            // 生成令牌
            String token = generateUserToken(userModel.getUsername());
            Map<String, Object> map = redisCache.getHmap(token);
            int userId = Integer.parseInt(map.get("id").toString());
            // 注册操作--向t_account表中插入数据
            userAccountService.addUserAccount(userId);
            Map<String, Object> returnMap = new HashMap<String, Object>();
            returnMap.put("id", userId);
            return Response.build().setStatus("1").setData(returnMap).setToken(token);
        }
        return Response.build().setStatus("999");
    }


    @RequestMapping("login")
    public Response login(@RequestBody UserModel userModel) {
        UserModel user = new UserModel();
        if (StringUtils.isEmpty(userModel.getSignUuid())) {
            return Response.build().setStatus(FrontStatusConstants.NULL_TOKEN);
        }
        if (StringUtils.isEmpty(userModel.getSignCode())) {
            return Response.build().setStatus(FrontStatusConstants.NULL_OF_VALIDATE_CARD);
        }
        // 根据uuid我们可以在redis中获取图片验证码
        String _signCode = redisCache.get(userModel.getSignUuid());
        if (StringUtils.isEmpty(_signCode)) {
            return Response.build().setStatus(FrontStatusConstants.NULL_OF_VALIDATE_CARD);
        }

        if (!_signCode.equalsIgnoreCase(userModel.getSignCode())) {
            return Response.build().setStatus(FrontStatusConstants.INPUT_ERROR_OF_VALIDATE_CARD);
        }

        String password = userModel.getPassword();
        String pwd = MD5Util.md5(userModel.getUsername().trim() + password.trim().toLowerCase());
        if (CommomUtil.isMobile(userModel.getUsername())) {
            user = userService.validatePhone(userModel.getUsername());
            pwd = MD5Util.md5(user.getUsername().trim() + password.trim().toLowerCase());
        }

        UserModel u = userService.login(user.getUsername(), pwd);
        // 登录失败
        if (u == null) {
            return Response.build().setStatus(FrontStatusConstants.ERROR_OF_USERNAME_PASSWORD);
        } else {// 登录成功
            // 生成token
            String token = generateUserToken(u.getUsername());
            // 封装响应数据
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", u.getId());
            map.put("userName", u.getUsername());
            return Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(map).setToken(token);
        }

    }

    @RequestMapping("validateCode")
    public void validatecode(@RequestParam("tokenUuid") String tokenUuid, @RequestParam("time") String
            date, HttpServletResponse response) throws IOException {
        String s = redisCache.get(tokenUuid);
        if (s.isEmpty()) {
            response.getWriter().write(Response.build().setStatus("-999").toJSON());
            return;
        }
        String rundomStr = ImageUtil.getRundomStr();
        redisCache.del(tokenUuid);
        redisCache.set(tokenUuid, rundomStr);
        redisCache.expire(tokenUuid, 3 * 60);
        ImageUtil.getImage(rundomStr, response.getOutputStream());

    }

    @RequestMapping("userSecure")
    public Response userSecure(HttpServletRequest request) {
        String token = GetHttpResponseHeader.getHeadersInfo(request);
        Map<String, Object> hmap = redisCache.getHmap(token);
        int userid = (int) hmap.get("id");
        UserModel userModel = userService.findById(userid);
        List<Map<String, Object>> objects = new ArrayList<>();
        Map<String, Object> stringObjectHashMap = new HashMap<>(16);
        stringObjectHashMap.put("phoneStatus", userModel.getPhoneStatus());
        stringObjectHashMap.put("emailStatus", userModel.getEmailStatus());
        stringObjectHashMap.put("payPwdStatus", userModel.getPayPwdStatus());
        stringObjectHashMap.put("realNameStatus", userModel.getRealNameStatus());
        objects.add(stringObjectHashMap);
        return Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(objects);
    }


    @RequestMapping("logout")
    public Response logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            return Response.build().setStatus(FrontStatusConstants.NULL_TOKEN);
        }
        Map<String, Object> hmap = redisCache.getHmap(token);
        if (hmap == null) {
            return Response.build().setStatus(FrontStatusConstants.NULL_TOKEN);
        }
        redisCache.del(token);
        return Response.build().setStatus(FrontStatusConstants.SUCCESS);
    }


    /**
     * 分析用户认证完成度
     */
    @RequestMapping("userSecureDetailed")
    public Response userSecureDetailed(HttpServletRequest request) {
        String token = GetHttpResponseHeader.getHeadersInfo(request);
        Map<String, Object> hmap = redisCache.getHmap(token);
        int userid = (int) hmap.get("id");
        UserModel userModel = userService.findById(userid);
        List<Map<String, Object>> objects = new ArrayList<>();
        Map<String, Object> stringObjectHashMap = new HashMap<>(16);
        stringObjectHashMap.put("phoneStatus", userModel.getPhoneStatus());
        stringObjectHashMap.put("emailStatus", userModel.getEmailStatus());
        stringObjectHashMap.put("payPwdStatus", userModel.getPayPwdStatus());
        stringObjectHashMap.put("realNameStatus", userModel.getRealNameStatus());
        stringObjectHashMap.put("username", userModel.getUsername());
        stringObjectHashMap.put("phone", userModel.getPhone());
        objects.add(stringObjectHashMap);
        return Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(objects);

    }

    @RequestMapping("addPhone")
    public Response addPhone(@RequestParam("phoneCode") String phoneCode, @RequestParam("phone") String phone, HttpServletRequest request) {
        String token = GetHttpResponseHeader.getHeadersInfo(request);
        if (org.apache.commons.lang.StringUtils.isEmpty(token)) {
            return Response.build().setStatus(FrontStatusConstants.NULL_TOKEN);
        }
        Map<String, Object> hmap = redisCache.getHmap(token);
        if (hmap == null || hmap.size() == 0) {
            // 用户未登录
            return Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN);
        }
        // 2.判断验证码是否正确
        String _phoneCode = redisCache.get(phone);
        if (!phoneCode.equals(_phoneCode)) {
            // 说明不正确
            return Response.build().setStatus(FrontStatusConstants.INPUT_ERROR_OF_VALIDATE_CARD);
        }
        UserModel user = userService.findById((int) (hmap.get("id")));
        if (user.getPhoneStatus() == 1) {
            return Response.build().setStatus(FrontStatusConstants.MOBILE_ALREADY_REGISTER);
        }
        userService.updatePhoneStatus(phone, (int) (hmap.get("id")));
        return Response.build().setStatus(FrontStatusConstants.SUCCESS);
    }



    /**
     * 生成用户令牌  并把令牌和用户信息存入缓存
     *
     * @param userName
     * @return
     */
    private String generateUserToken(String userName) {

        try {
            // 生成令牌
            String token = TokenUtil.generateUserToken(userName);

            // 根据用户名获取用户
            UserModel user = userService.validateUserName(userName);
            // 将用户信息存储到map中。
            Map<String, Object> tokenMap = new HashMap<String, Object>();
            tokenMap.put("id", user.getId());
            tokenMap.put("userName", user.getUsername());
            tokenMap.put("phone", user.getPhone());
            tokenMap.put("userType", user.getUserType());
            tokenMap.put("payPwdStatus", user.getPayPwdStatus());
            tokenMap.put("emailStatus", user.getEmailStatus());
            tokenMap.put("realName", user.getRealName());
            tokenMap.put("identity", user.getIdentity());
            tokenMap.put("realNameStatus", user.getRealNameStatus());
            tokenMap.put("payPhoneStatus", user.getPhoneStatus());

            redisCache.del(token);
            // 将信息存储到redis中
            redisCache.setHmap(token, tokenMap);

            // 获取配置文件中用户的生命周期，如果没有，默认是30分钟
            String tokenValid = ConfigurableConstants.getProperty("token.validity", "30");
            tokenValid = tokenValid.trim();
            redisCache.expire(token, Long.valueOf(tokenValid) * 60);

            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return Response.build().setStatus("-9999").toJSON();
        }
    }
    /*    eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLmm7nkuIDluIY2OTI0MjQ0OTkwNTQ5NSJ9.ige6Wx0rurVadX7hbkovzq62tF9X76i2c3Kr_OdC6Fo*/
}
