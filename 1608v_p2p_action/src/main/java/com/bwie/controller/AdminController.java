package com.bwie.controller;

import com.bwie.cache.BaseCacheService;
import com.bwie.pojo.AdminModel;
import com.bwie.pojo.UserAccountModel;
import com.bwie.service.AdminService;
import com.bwie.service.UserAccountService;
import com.bwie.until.FrontStatusConstants;
import com.bwie.until.GetHttpResponseHeader;
import com.bwie.until.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: admin
 * @Date: 2019/5/13 19:11
 * @Description:
 */
@RestController
@RequestMapping("account")
public class AdminController extends BaseController {

    @Autowired
    private BaseCacheService redisCache;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private AdminService adminService;



    @RequestMapping("login")
    public Response login(@RequestParam("username") String username, @RequestParam("password") String password){
       AdminModel adminModel= adminService.login(username,password);
       if(adminModel!=null){
           return new Response("登录成功",FrontStatusConstants.SUCCESS,null);
       }
        return new Response("登录失败",FrontStatusConstants.BREAK_DOWN,null);
    }

    @RequestMapping("accountHomepage")
    public Response accountHomepage(HttpServletRequest request){
        String token = GetHttpResponseHeader.getHeadersInfo(request);
        Map<String, Object> hmap = redisCache.getHmap(token);
        int userid= (int) hmap.get("id");
        UserAccountModel userAccountModel=userAccountService.findById(userid);
        List<Map<String,Object>> objects = new ArrayList<>();
        Map<String, Object> stringObjectHashMap = new HashMap<>(16);
        stringObjectHashMap.put("u_total",userAccountModel.getTotal());
        stringObjectHashMap.put("u_balance",userAccountModel.getBalance());
        stringObjectHashMap.put("u_interest_a",userAccountModel.getInterestA());
        objects.add(stringObjectHashMap);
        return Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(objects);
    }

}
