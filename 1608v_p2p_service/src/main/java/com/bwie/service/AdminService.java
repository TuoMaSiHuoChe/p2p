package com.bwie.service;

import com.bwie.pojo.AdminModel;

/**
 * @author: admin
 * @Date: 2019/5/13 19:40
 * @Description:
 */
public interface AdminService {
    AdminModel login(String username, String password);
}
