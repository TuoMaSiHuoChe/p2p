package com.bwie.controller;

import com.bwie.pojo.AdminModel;
import com.bwie.service.AdminService;
import com.bwie.until.FrontStatusConstants;
import com.bwie.until.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: admin
 * @Date: 2019/5/13 19:11
 * @Description:
 */
@Controller
@RequestMapping("account")
public class AdminController extends BaseController {

    @Autowired
    private AdminService adminService;
    @RequestMapping("login")
    @ResponseBody
    public Response login(@RequestParam("username") String username, @RequestParam("password") String password){
       AdminModel adminModel= adminService.login(username,password);
       if(adminModel!=null){
           return new Response("登录成功",FrontStatusConstants.SUCCESS,null);
       }
        return new Response("登录失败",FrontStatusConstants.BREAK_DOWN,null);
    }
}
